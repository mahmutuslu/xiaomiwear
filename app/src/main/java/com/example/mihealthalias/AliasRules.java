package com.example.mihealthalias;

import java.util.List;

/** Pure-Java alias rules, shared by the hook layer and JVM unit tests. */
final class AliasRules {
    static final String REAL = "com.xiaomi.wearable";
    static final String ALIAS = "com.mi.health";

    private AliasRules() {
    }

    /**
     * Returns null if the original argument list should be left untouched.
     * The original (immutable) libxposed Chain arguments are never modified.
     */
    static Object[] rewriteAliasArguments(List<?> args) {
        Object[] rewritten = null;
        for (int i = 0; i < args.size(); i++) {
            if (ALIAS.equals(args.get(i))) {
                if (rewritten == null) {
                    rewritten = args.toArray();
                }
                rewritten[i] = REAL;
            }
        }
        return rewritten;
    }
}
