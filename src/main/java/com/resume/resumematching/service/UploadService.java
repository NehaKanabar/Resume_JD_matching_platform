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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final UploadRepository uploadRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final UsageCounterService usageCounterService;

    /* -----------------------------------------
       UPLOAD FILE (RESUME / JD)
    ----------------------------------------- */

    public UploadResponse uploadFile(UploadRequest request, String email) {

        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Tenant context missing");
        }

        // 🔐 PLAN LIMIT CHECK (BEFORE UPLOAD)
        FileType fileType = FileType.valueOf(request.getFileType());

        if (fileType == FileType.RESUME) {
            usageCounterService.checkResumeLimit(tenantId);
        } else if (fileType == FileType.JD) {
            usageCounterService.checkJdLimit(tenantId);
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Upload upload = Upload.builder()
                .tenant(tenant)
                .user(user)
                .fileType(fileType)
                .filePath(request.getFilePath())
                .fileSize(request.getFileSize())
                .status(UploadStatus.UPLOADED)
                .build();

        Upload saved = uploadRepository.save(upload);

        // ✅ INCREMENT USAGE (AFTER SUCCESSFUL UPLOAD)
        if (fileType == FileType.RESUME) {
            usageCounterService.incrementResume(tenantId);
        } else if (fileType == FileType.JD) {
            usageCounterService.incrementJd(tenantId);
        }

        return UploadResponse.builder()
                .id(saved.getId())
                .fileType(saved.getFileType().name())
                .filePath(saved.getFilePath())
                .fileSize(saved.getFileSize())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    /* -----------------------------------------
       GET ALL UPLOADS (TENANT-WISE)
    ----------------------------------------- */

    public List<UploadResponse> getTenantUploads() {

        Long tenantId = TenantContext.getTenantId();

        return uploadRepository.findByTenantId(tenantId)
                .stream()
                .map(upload -> new UploadResponse(
                        upload.getId(),
                        upload.getFilePath(),
                        upload.getFileSize(),
                        upload.getFileType().name(),
                        upload.getStatus().name(),
                        upload.getCreatedAt()
                ))
                .toList();
    }
}
