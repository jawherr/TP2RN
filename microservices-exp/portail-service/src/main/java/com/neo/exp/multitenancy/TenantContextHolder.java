package com.neo.exp.multitenancy;

public class TenantContextHolder {
    private static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal <>();

    public static void setTenantId(String tenant) {
        CONTEXT.set(tenant);
    }

    public static String getTenant() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
