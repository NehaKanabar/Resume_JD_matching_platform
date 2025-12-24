package com.resume.resumematching.upload.dto;

import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponse {

    private Long id;
    private String filePath;
    private Long fileSize;
    private FileType fileType;
    private UploadStatus status;
    private LocalDateTime createdAt;
}