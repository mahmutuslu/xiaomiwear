package com.example.mihealthalias;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class AliasRulesTest {
    @Test
    public void rewritesOnlyExactAlias() {
        Object[] result = AliasRules.rewriteAliasArguments(
                Arrays.asList("com.mi.health", 42, "com.mi.health.other", null));
        assertArrayEquals(new Object[] {
                "com.xiaomi.wearable", 42, "com.mi.health.other", null
        }, result);
    }

    @Test
    public void preservesUnrelatedCalls() {
        assertNull(AliasRules.rewriteAliasArguments(
                Arrays.asList("com.xiaomi.wearable", "com.miui.personalassistant")));
        assertNull(AliasRules.rewriteAliasArguments(Collections.emptyList()));
    }

    @Test
    public void rewritesEveryExactAliasWithoutChangingInput() {
        java.util.List<Object> args = Arrays.asList("com.mi.health", "com.mi.health");
        assertArrayEquals(new Object[] {"com.xiaomi.wearable", "com.xiaomi.wearable"},
                AliasRules.rewriteAliasArguments(args));
        assertArrayEquals(new Object[] {"com.mi.health", "com.mi.health"}, args.toArray());
    }
}
