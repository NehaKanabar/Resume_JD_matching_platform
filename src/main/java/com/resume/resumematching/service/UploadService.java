package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.upload.UploadRequest;
import com.resume.resumematching.dto.upload.UploadResponse;
import com.resume.resumematching.entity.Tenant;
import com.resume.resumematching.entity.Upload;
import com.resume.resumematching.entity.User;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import com.resume.resumematching.repository.TenantRepository;
import com.resume.resumematching.repository.UploadRepository;
import com.resume.resumematching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final UploadRepository uploadRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public UploadResponse uploadFile(UploadRequest request, String email) {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Tenant context missing");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FileType fileType;
        try {
            fileType = FileType.valueOf(request.getFileType().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid file type: " + request.getFileType());
        }

        Upload upload = Upload.builder()
                .tenant(tenant)
                .user(user)
                .fileType(fileType)
                .filePath(request.getFilePath())
                .fileSize(request.getFileSize())
                .status(UploadStatus.UPLOADED) // initial state
                .build();

        Upload saved = uploadRepository.save(upload);

        return UploadResponse.builder()
                .id(saved.getId())
                .fileType(saved.getFileType().name())
                .filePath(saved.getFilePath())
                .fileSize(saved.getFileSize())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<UploadResponse> getTenantUploads() {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Tenant context missing");
        }

        return uploadRepository.findByTenantId(tenantId)
                .stream()
                .map(upload -> UploadResponse.builder()
                        .id(upload.getId())
                        .fileType(upload.getFileType().name())
                        .filePath(upload.getFilePath())
                        .fileSize(upload.getFileSize())
                        .status(upload.getStatus().name())
                        .createdAt(upload.getCreatedAt())
                        .build()
                )
                .toList();
    }
}
