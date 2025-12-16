package com.resume.resumematching.controller;

import com.resume.resumematching.dto.usage.UsageResponse;
import com.resume.resumematching.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    /**
     * ADMIN → sees own tenant usage
     * SUPERUSER → can switch tenant via header/context
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public UsageResponse getUsage() {
        return usageService.getCurrentUsage();
    }
}

