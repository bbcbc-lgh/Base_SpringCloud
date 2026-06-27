package com.gzu.common.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private String secret = "gzu-demo-secret";
    private long ttlMinutes = 120;
    private List<String> publicPathPrefixes = List.of("/auth/login", "/auth/register", "/health", "/actuator/", "/error");

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getTtlMinutes() {
        return ttlMinutes;
    }

    public void setTtlMinutes(long ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
    }

    public List<String> getPublicPathPrefixes() {
        return publicPathPrefixes;
    }

    public void setPublicPathPrefixes(List<String> publicPathPrefixes) {
        this.publicPathPrefixes = publicPathPrefixes;
    }
}

