package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController extends AbstractBaseController {
    private final MessageSourceService messageSourceService;

    @GetMapping
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok(messageSourceService.get("hi"));
    }
}
