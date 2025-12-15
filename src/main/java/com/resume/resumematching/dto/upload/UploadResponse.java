package com.resume.resumematching.dto.upload;

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
    private String fileType;
    private String status;
    private LocalDateTime createdAt;
}

