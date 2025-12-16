package com.resume.resumematching.repository;

import com.resume.resumematching.entity.ParsedDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParsedDocumentRepository extends JpaRepository<ParsedDocument, Long> {

    Optional<ParsedDocument> findByUploadIdAndTenantId(Long uploadId, Long tenantId);

    List<ParsedDocument> findByTenantIdAndParsedTrueAndFileType(
            Long tenantId,
            com.resume.resumematching.enums.FileType fileType
    );

    default List<ParsedDocument> findParsedResumesByTenantId(Long tenantId) {
        return findByTenantIdAndParsedTrueAndFileType(
                tenantId,
                com.resume.resumematching.enums.FileType.RESUME
        );
    }
}
