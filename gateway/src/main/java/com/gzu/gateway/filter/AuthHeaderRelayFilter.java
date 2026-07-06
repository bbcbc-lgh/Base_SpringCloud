package com.gzu.gateway.filter;

import com.gzu.common.core.constant.AuthConstants;
import com.gzu.common.core.security.TokenPayload;
import com.gzu.common.core.security.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class AuthHeaderRelayFilter implements GlobalFilter, Ordered {
    private static final List<String> PUBLIC_EXACT_PATHS = List.of(
            "/",
            "/index.html",
            "/favicon.ico");

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/stock",
            "/api/messages",
            "/actuator",
            "/error");

    @Value("${app.security.secret:gzu-demo-secret}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().value();
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(AuthConstants.HEADER_AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            return writeUnauthorized(exchange, "missing token");
        }

        String token = authHeader.substring(AuthConstants.BEARER_PREFIX.length()).trim();
        Optional<TokenPayload> payloadOpt = TokenUtil.verify(token, secret);
        if (payloadOpt.isEmpty()) {
            return writeUnauthorized(exchange, "invalid token");
        }

        TokenPayload payload = payloadOpt.get();
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(AuthConstants.HEADER_USER_ID, String.valueOf(payload.userId()))
                .header(AuthConstants.HEADER_USER_NAME, payload.username())
                .header(AuthConstants.HEADER_USER_ROLE, payload.role())
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private boolean isPublicPath(String path) {
        if (PUBLIC_EXACT_PATHS.contains(path)) {
            return true;
        }
        for (String item : PUBLIC_PATHS) {
            if (path.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> writeUnauthorized(ServerWebExchange exchange, String message) {
        byte[] bytes = ("{\"code\":401,\"message\":\"" + message + "\",\"data\":null}")
                .getBytes(StandardCharsets.UTF_8);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
