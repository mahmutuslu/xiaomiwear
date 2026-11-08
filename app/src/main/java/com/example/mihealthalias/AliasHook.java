package com.example.mihealthalias;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.libxposed.api.XposedInterface.Chain;
import io.github.libxposed.api.XposedInterface.Hooker;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam;

/**
 * Process-local aliases for App Vault's widget picker.
 *
 * The installed Mi Fitness package is never renamed or replaced. Only App Vault
 * can see selected package-list results under the com.mi.health alias; lookups
 * for that alias are redirected to the genuine com.xiaomi.wearable package.
 */
public final class AliasHook extends XposedModule {
    private static final String TAG = "MiHealthAlias";
    private static final String TARGET = "com.miui.personalassistant";
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public void onPackageReady(PackageReadyParam param) {
        if (!TARGET.equals(param.getPackageName())
                || !initialized.compareAndSet(false, true)) {
            return;
        }

        int count = 0;
        ClassLoader loader = param.getClassLoader();
        // Resolve the virtual package name against the real installed package.
        count += hookMethods("android.app.ApplicationPackageManager", loader,
                new String[] {
                        "getPackageInfo", "getPackageInfoAsUser",
                        "getApplicationInfo", "getApplicationInfoAsUser",
                        "getApplicationIcon", "getLaunchIntentForPackage", "getPackageUid"
                }, true, this::rewriteIncoming);
        count += hookMethods("android.content.pm.LauncherApps", loader,
                new String[] {
                        "getActivityList", "isPackageEnabled", "getApplicationInfo"
                }, true, this::rewriteIncoming);
        count += hookMethods("android.appwidget.AppWidgetManager", loader,
                new String[] {
                        "getInstalledProvidersForPackage", "getInstalledProvidersForProfile"
                }, true, this::rewriteIncoming);

        // Return an alias only for Mi Fitness entries in package/activity lists.
        count += hookMethods("android.app.ApplicationPackageManager", loader,
                new String[] {
                        "getInstalledPackages", "getInstalledPackagesAsUser",
                        "queryIntentActivities", "queryIntentActivitiesAsUser"
                }, false, this::renameOutgoingResults);

        // Server-provided ComponentName(alias, className) must resolve to real.
        count += hookComponentNameConstructors();

        log(Log.INFO, TAG, "API " + getApiVersion()
                + ": " + count + " App Vault hooks installed");
    }

    private Object rewriteIncoming(Chain chain) throws Throwable {
        Object[] replacement = AliasRules.rewriteAliasArguments(chain.getArgs());
        return replacement == null ? chain.proceed() : chain.proceed(replacement);
    }

    private Object renameOutgoingResults(Chain chain) throws Throwable {
        Object result = chain.proceed();
        if (result instanceof List<?>) {
            for (Object item : (List<?>) result) {
                try {
                    renameResult(item);
                } catch (Throwable t) {
                    // Never fail a PackageManager call on account of aliasing.
                    log(Log.WARN, TAG, "Skipping incompatible package item: " + t);
                }
            }
        }
        return result;
    }

    private static void renameResult(Object item) {
        if (item instanceof PackageInfo) {
            PackageInfo info = (PackageInfo) item;
            if (AliasRules.REAL.equals(info.packageName)) {
                info.packageName = AliasRules.ALIAS;
                renameApplicationInfo(info.applicationInfo);
            }
        } else if (item instanceof ResolveInfo) {
            ResolveInfo info = (ResolveInfo) item;
            ActivityInfo activity = info.activityInfo;
            if (activity != null && AliasRules.REAL.equals(activity.packageName)) {
                activity.packageName = AliasRules.ALIAS;
                renameApplicationInfo(activity.applicationInfo);
            }
        }
    }

    private static void renameApplicationInfo(ApplicationInfo info) {
        if (info != null && AliasRules.REAL.equals(info.packageName)) {
            info.packageName = AliasRules.ALIAS;
        }
    }

    private int hookMethods(String className, ClassLoader loader, String[] names,
                            boolean needsStringArgument, Hooker callback) {
        final Class<?> clazz;
        try {
            clazz = Class.forName(className, false, loader);
        } catch (Throwable t) {
            log(Log.WARN, TAG, "Class unavailable: " + className + ": " + t);
            return 0;
        }

        int installed = 0;
        List<String> wanted = Arrays.asList(names);
        for (Method method : clazz.getDeclaredMethods()) {
            if (!wanted.contains(method.getName())) {
                continue;
            }
            if (needsStringArgument && !Arrays.asList(method.getParameterTypes())
                    .contains(String.class)) {
                continue;
            }
            try {
                hook(method).setId("mihealth:" + method.toGenericString()).intercept(callback);
                installed++;
            } catch (Throwable t) {
                log(Log.WARN, TAG, "Hook unavailable: " + method + ": " + t);
            }
        }
        return installed;
    }

    private int hookComponentNameConstructors() {
        int installed = 0;
        for (Constructor<?> constructor : ComponentName.class.getDeclaredConstructors()) {
            Class<?>[] args = constructor.getParameterTypes();
            if (args.length == 0 || args[0] != String.class) {
                continue;
            }
            try {
                hook(constructor).setId("mihealth:" + constructor.toGenericString())
                        .intercept(this::rewriteIncoming);
                installed++;
            } catch (Throwable t) {
                log(Log.WARN, TAG, "ComponentName hook unavailable: " + t);
            }
        }
        return installed;
    }
}
