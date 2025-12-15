package com.resume.resumematching.repository;

import com.resume.resumematching.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {

    List<Upload> findByTenantId(Long tenantId);
}
