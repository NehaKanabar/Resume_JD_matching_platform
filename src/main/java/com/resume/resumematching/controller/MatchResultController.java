package com.resume.resumematching.controller;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.match.MatchResultResponse;
import com.resume.resumematching.repository.MatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchResultRepository matchResultRepository;

    @GetMapping("/{jobId}/results")
    public List<MatchResultResponse> getResults(@PathVariable Long jobId) {

        Long tenantId = TenantContext.getTenantId();

        return matchResultRepository
                .findByMatchJobIdAndTenantIdOrderByOverallScoreDesc(jobId, tenantId)
                .stream()
                .map(r -> new MatchResultResponse(
                        r.getResumeUploadId(),
                        r.getOverallScore(),
                        r.getBreakdown(),
                        r.getCreatedAt()
                ))
                .toList();
    }
}
