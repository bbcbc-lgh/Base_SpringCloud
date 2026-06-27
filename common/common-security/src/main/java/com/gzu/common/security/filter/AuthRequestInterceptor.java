package com.gzu.common.security.filter;

import com.gzu.common.core.constant.AuthConstants;
import com.gzu.common.core.exception.ErrorCode;
import com.gzu.common.core.exception.TokenException;
import com.gzu.common.core.result.ApiResponse;
import com.gzu.common.core.security.TokenPayload;
import com.gzu.common.core.security.TokenUtil;
import com.gzu.common.security.context.AuthContextHolder;
import com.gzu.common.security.properties.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class AuthRequestInterceptor implements HandlerInterceptor {
    private final SecurityProperties properties;

    public AuthRequestInterceptor(SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            return true;
        }

        String header = request.getHeader(AuthConstants.HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(AuthConstants.BEARER_PREFIX)) {
            writeUnauthorized(response, "missing token");
            return false;
        }

        String token = header.substring(AuthConstants.BEARER_PREFIX.length()).trim();
        try {
            TokenPayload payload = TokenUtil.requireValid(token, properties.getSecret());
            AuthContextHolder.set(payload);
            request.setAttribute("authUser", payload);
            return true;
        } catch (TokenException ex) {
            writeUnauthorized(response, ex.getMessage());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContextHolder.clear();
    }

    private boolean isPublicPath(String path) {
        List<String> prefixes = properties.getPublicPathPrefixes();
        for (String prefix : prefixes) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(toJson(ApiResponse.fail(ErrorCode.UNAUTHORIZED, message)));
    }

    private String toJson(ApiResponse<?> response) {
        Object data = response.data();
        String dataText = data == null ? "null" : "\"" + data.toString().replace("\"", "\\\"") + "\"";
        return "{\"code\":" + response.code() + ",\"message\":\"" + response.message().replace("\"", "\\\"") + "\",\"data\":" + dataText + "}";
    }
}

