package com.gzu.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RootController {
    @GetMapping("/")
    public Mono<ResponseEntity<Void>> root() {
        return Mono.just(ResponseEntity.notFound().build());
    }
}
