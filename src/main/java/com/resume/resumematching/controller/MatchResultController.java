package com.resume.resumematching.controller;

import com.resume.resumematching.dto.common.ApiResponse;
import com.resume.resumematching.dto.match.MatchResultResponse;
import com.resume.resumematching.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchService matchService;

    @GetMapping("/{jobId}/results")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<List<MatchResultResponse>>> getResults(
            @PathVariable Long jobId
    ) {

        List<MatchResultResponse> results = matchService.getResults(jobId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Match results fetched successfully",
                        results
                )
        );
    }
}
