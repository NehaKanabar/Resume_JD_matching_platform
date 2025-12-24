package com.resume.resumematching.controller;

import com.resume.resumematching.dto.common.ApiResponse;
import com.resume.resumematching.dto.usage.UsageResponse;
import com.resume.resumematching.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    // ADMIN → own tenant usage
    // SUPERUSER → any tenant usage (based on context)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<ApiResponse<UsageResponse>> getUsage() {

        UsageResponse usage = usageService.getCurrentUsage();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Usage details fetched successfully",
                        usage
                )
        );
    }
}
