package com.example.mihealthalias;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Inside the HyperOS widget picker (com.miui.personalassistant) only, make the installed
 * Mi Fitness (com.xiaomi.wearable) look like the China health app (com.mi.health).
 *
 * Outgoing: the installed-app list the picker uploads names com.mi.health.
 * Incoming: every lookup of com.mi.health (icons, widget providers, ComponentName) is
 * translated back to the real package.
 */
public class AliasHook implements IXposedHookLoadPackage {

    private static final String TARGET = "com.miui.personalassistant";
    private static final String REAL = "com.xiaomi.wearable";
    private static final String ALIAS = "com.mi.health";

    /** Replace any String argument equal to the alias with the real package name. */
    private static final XC_MethodHook ARG_REWRITE = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) {
            Object[] a = param.args;
            if (a == null) {
                return;
            }
            for (int i = 0; i < a.length; i++) {
                if (ALIAS.equals(a[i])) {
                    a[i] = REAL;
                }
            }
        }
    };

    /** Rename the real package inside lists returned by PackageManager. */
    private static final XC_MethodHook RENAME_RESULT = new XC_MethodHook() {
        @Override
        protected void afterHookedMethod(MethodHookParam param) {
            Object r = param.getResult();
            if (!(r instanceof List)) {
                return;
            }
            for (Object o : (List<?>) r) {
                rename(o);
            }
        }
    };

    private static void rename(Object o) {
        try {
            if (o instanceof PackageInfo) {
                PackageInfo pi = (PackageInfo) o;
                if (REAL.equals(pi.packageName)) {
                    pi.packageName = ALIAS;
                }
            } else if (o instanceof ResolveInfo) {
                ResolveInfo ri = (ResolveInfo) o;
                if (ri.activityInfo != null && REAL.equals(ri.activityInfo.packageName)) {
                    ri.activityInfo.packageName = ALIAS;
                }
            }
        } catch (Throwable ignored) {
            // never break the host app
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lp) throws Throwable {
        if (!TARGET.equals(lp.packageName)) {
            return;
        }
        log("loaded in process " + lp.processName);

        // Lookups by package name: alias -> real
        hookMethods("android.app.ApplicationPackageManager", new String[] {
                "getPackageInfo", "getPackageInfoAsUser",
                "getApplicationInfo", "getApplicationInfoAsUser",
                "getApplicationIcon", "getLaunchIntentForPackage", "getPackageUid"
        }, ARG_REWRITE);
        hookMethods("android.content.pm.LauncherApps", new String[] {
                "getActivityList", "isPackageEnabled", "getApplicationInfo"
        }, ARG_REWRITE);
        hookMethods("android.appwidget.AppWidgetManager", new String[] {
                "getInstalledProvidersForPackage", "getInstalledProvidersForProfile"
        }, ARG_REWRITE);

        // Lists the picker reads (and later uploads): real -> alias
        hookMethods("android.app.ApplicationPackageManager", new String[] {
                "getInstalledPackages", "getInstalledPackagesAsUser",
                "queryIntentActivities", "queryIntentActivitiesAsUser"
        }, RENAME_RESULT);

        // Widget providers the server returns are built as new ComponentName(alias, class).
        try {
            XposedBridge.hookAllConstructors(ComponentName.class, ARG_REWRITE);
        } catch (Throwable t) {
            log("ComponentName hook failed: " + t);
        }
        log("hooks installed");
    }

    private static void hookMethods(String className, String[] names, XC_MethodHook callback) {
        Class<?> c;
        try {
            c = Class.forName(className);
        } catch (Throwable t) {
            log("class not found " + className + ": " + t);
            return;
        }
        for (String n : names) {
            try {
                XposedBridge.hookAllMethods(c, n, callback);
            } catch (Throwable t) {
                log("hook failed " + className + "." + n + ": " + t);
            }
        }
    }

    private static void log(String s) {
        XposedBridge.log("MiHealthAlias: " + s);
    }
}
