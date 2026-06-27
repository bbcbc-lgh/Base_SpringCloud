package com.gzu.common.core.security;

public record TokenPayload(Long userId, String username, String role, long issuedAt, long expiresAt) {
}

