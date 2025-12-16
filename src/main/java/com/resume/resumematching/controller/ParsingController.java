package com.resume.resumematching.controller;

import com.resume.resumematching.service.ParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parse")
@RequiredArgsConstructor
public class ParsingController {

    private final ParsingService parsingService;

    @PostMapping("/{uploadId}")
    public ResponseEntity<String> parse(@PathVariable Long uploadId) {
        parsingService.parseUpload(uploadId);
        return ResponseEntity.ok("Parsing completed");
    }
}

