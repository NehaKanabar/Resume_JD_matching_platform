package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private static final String BASE_DIR = "uploads";

    private final UploadRepository uploadRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final UsageCounterService usageCounterService;
    private final BillingAccessValidator billingAccessValidator;


    //   UPLOAD FILE (MULTIPART)
    public UploadResponse uploadFile(
            MultipartFile file,
            FileType fileType,
            String email
    ) {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new RuntimeException("Tenant context missing");
        }

        // Billing check
        billingAccessValidator.validateUploadAccess();

        // PLAN LIMIT CHECK
        if (fileType == FileType.RESUME) {
            usageCounterService.checkResumeLimit(tenantId);
        } else {
            usageCounterService.checkJdLimit(tenantId);
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validateFile(file);

        // Save file to filesystem
        String filePath = storeFile(file, tenantId, fileType);

        Upload upload = Upload.builder()
                .tenant(tenant)
                .user(user)
                .fileType(fileType)
                .filePath(filePath)
                .fileSize(file.getSize())
                .status(UploadStatus.UPLOADED)
                .build();

        Upload saved = uploadRepository.save(upload);

        // Increment usage AFTER success
        if (fileType == FileType.RESUME) {
            usageCounterService.incrementResume(tenantId);
        } else {
            usageCounterService.incrementJd(tenantId);
        }

        return UploadResponse.builder()
                .id(saved.getId())
                .fileType(saved.getFileType())
                .filePath(saved.getFilePath())
                .fileSize(saved.getFileSize())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null ||
                !(filename.endsWith(".pdf")
                        || filename.endsWith(".doc")
                        || filename.endsWith(".docx"))) {
            throw new RuntimeException("Invalid file type");
        }
    }

    private String storeFile(MultipartFile file, Long tenantId, FileType fileType) {
        try {
            String extension = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            String fileName = UUID.randomUUID() + extension;

            Path dir = Paths.get(BASE_DIR, tenantId.toString(), fileType.name());
            Files.createDirectories(dir);

            Path fullPath = dir.resolve(fileName);
            Files.copy(
                    file.getInputStream(),
                    fullPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // store relative path
            return BASE_DIR + "/" + tenantId + "/" + fileType.name() + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("File storage failed", e);
        }
    }

    //   GET UPLOADS (TENANT-WISE)
    public List<UploadResponse> getTenantUploads() {

        Long tenantId = TenantContext.getTenantId();

        return uploadRepository.findByTenantId(tenantId)
                .stream()
                .map(upload -> new UploadResponse(
                        upload.getId(),
                        upload.getFilePath(),
                        upload.getFileSize(),
                        upload.getFileType(),
                        upload.getStatus(),
                        upload.getCreatedAt()
                ))
                .toList();
    }
}
