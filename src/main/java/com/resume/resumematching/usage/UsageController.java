package com.resume.resumematching.usage;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.usage.dto.UsageResponse;
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
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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
