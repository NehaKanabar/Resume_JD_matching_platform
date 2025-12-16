package com.resume.resumematching.controller;

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
    public ResponseEntity<MatchJob> runMatch(@PathVariable Long jdUploadId) {
        return ResponseEntity.ok(matchService.runMatch(jdUploadId));
    }
}

