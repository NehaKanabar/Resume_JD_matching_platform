package com.resume.resumematching.upload;

import com.resume.resumematching.common.ApiResponse;
import com.resume.resumematching.upload.dto.UploadResponse;
import com.resume.resumematching.enums.FileType;
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

    // UPLOAD RESUME / JD
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadResponse>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileType fileType,
            Authentication authentication
    ) {

        UploadResponse uploadResponse = uploadService.uploadFile(
                file,
                fileType,
                authentication.getName()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "File uploaded successfully",
                        uploadResponse
                )
        );
    }

    // GET ALL UPLOADS (TENANT-WISE)
    @GetMapping
    public ResponseEntity<ApiResponse<List<UploadResponse>>> getUploads() {

        List<UploadResponse> uploads =
                uploadService.getTenantUploads();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Uploads fetched successfully",
                        uploads
                )
        );
    }
}
