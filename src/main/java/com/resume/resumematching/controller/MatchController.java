package com.resume.resumematching.controller;

import com.resume.resumematching.dto.common.ApiResponse;
import com.resume.resumematching.entity.MatchJob;
import com.resume.resumematching.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/{jdUploadId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<MatchJob>> runMatch(
            @PathVariable Long jdUploadId
    ) {

        MatchJob matchJob = matchService.runMatch(jdUploadId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resume matching completed",
                        matchJob
                )
        );
    }
}
