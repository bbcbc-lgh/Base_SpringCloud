package com.gzu.common.security.context;

import com.gzu.common.core.security.TokenPayload;

public final class AuthContextHolder {
    private static final ThreadLocal<TokenPayload> HOLDER = new ThreadLocal<>();

    private AuthContextHolder() {
    }

    public static void set(TokenPayload payload) {
        HOLDER.set(payload);
    }

    public static TokenPayload get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}

