package com.gzu.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/config")
public class TestController {

    @Value("${config.service-name:unknown}")
    private String serviceName;

    @Value("${config.config-version:unknown}")
    private String configVersion;

    @Value("${config.config-source:unknown}")
    private String configSource;

    @Value("${config.direct-interfaces[0].method:unknown}")
    private String createOrderMethod;

    @Value("${config.direct-interfaces[0].path:unknown}")
    private String createOrderPath;

    @Value("${config.direct-interfaces[0].gateway-path:unknown}")
    private String createOrderGatewayPath;

    @Value("${config.direct-interfaces[0].description:unknown}")
    private String createOrderDescription;

    @GetMapping("/nacos")
    public Map<String, String> nacosConfig() {
        return Map.of(
                "serviceName", serviceName,
                "configVersion", configVersion,
                "configSource", configSource,
                "method", createOrderMethod,
                "path", createOrderPath,
                "gatewayPath", createOrderGatewayPath,
                "description", createOrderDescription
        );
    }
}
