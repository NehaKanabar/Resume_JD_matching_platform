package com.resume.resumematching.repository;

import com.resume.resumematching.upload.entity.ParsedDocument;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParsedDocumentRepository extends JpaRepository<ParsedDocument, Long> {

    Optional<ParsedDocument> findByUploadIdAndTenantIdAndStatusAndFileType(
            Long uploadId,
            Long tenantId,
            UploadStatus status,
            FileType fileType
    );

    List<ParsedDocument> findByTenantIdAndStatusAndFileType(
            Long tenantId,
            UploadStatus status,
            FileType fileType
    );

    default List<ParsedDocument> findParsedResumesByTenantId(Long tenantId) {
        return findByTenantIdAndStatusAndFileType(
                tenantId,
                UploadStatus.PARSED,
                FileType.RESUME
        );
    }
}
