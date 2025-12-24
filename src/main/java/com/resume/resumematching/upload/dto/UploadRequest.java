package com.resume.resumematching.upload.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadRequest {

    @NotNull
    @Pattern(regexp = "RESUME|JD", message = "fileType must be RESUME or JD")
    private String fileType;   // RESUME / JD

    @NotNull
    private String filePath;

    @NotNull
    private Long fileSize;
}

