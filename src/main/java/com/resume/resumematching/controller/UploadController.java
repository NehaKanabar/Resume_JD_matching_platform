package com.resume.resumematching.controller;

import com.resume.resumematching.dto.upload.UploadResponse;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    /* -----------------------------------------
       UPLOAD RESUME / JD (MULTIPART)
    ----------------------------------------- */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileType fileType,
            Authentication authentication
    ) {
        UploadResponse response = uploadService.uploadFile(
                file,
                fileType,
                authentication.getName()   // logged-in user's email
        );

        return ResponseEntity.ok(response);
    }

    /* -----------------------------------------
       GET ALL UPLOADS (TENANT-WISE)
    ----------------------------------------- */
    @GetMapping
    public ResponseEntity<List<UploadResponse>> getUploads() {
        return ResponseEntity.ok(uploadService.getTenantUploads());
    }
}
