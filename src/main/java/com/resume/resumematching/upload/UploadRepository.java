package com.resume.resumematching.upload;

import com.resume.resumematching.upload.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByTenantId(Long tenantId);
}