package com.resume.resumematching.controller;

import com.resume.resumematching.dto.upload.UploadRequest;
import com.resume.resumematching.dto.upload.UploadResponse;
import com.resume.resumematching.entity.Upload;
import com.resume.resumematching.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public UploadResponse upload(
            @Valid @RequestBody UploadRequest request,
            Authentication authentication
    ) {
        return uploadService.uploadFile(request, authentication.getName());
    }

    @GetMapping
    public ResponseEntity<List<UploadResponse>> getUploads() {
        return ResponseEntity.ok(uploadService.getTenantUploads());
    }

}
