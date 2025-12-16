package com.resume.resumematching.controller;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.match.MatchResultResponse;
import com.resume.resumematching.repository.MatchResultRepository;
import com.resume.resumematching.service.MatchService;
import lombok.RequiredArgsConstructor;
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
    public List<MatchResultResponse> getResults(@PathVariable Long jobId) {
        return matchService.getResults(jobId);
    }
}
