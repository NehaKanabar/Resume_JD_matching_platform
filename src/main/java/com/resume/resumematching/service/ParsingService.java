package com.resume.resumematching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.resumematching.upload.entity.ParsedDocument;
import com.resume.resumematching.upload.entity.Upload;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import com.resume.resumematching.repository.ParsedDocumentRepository;
import com.resume.resumematching.upload.UploadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ParsingService {

    private final UploadRepository uploadRepository;
    private final ParsedDocumentRepository parsedDocumentRepository;
    private final ObjectMapper objectMapper;

    public void parseUpload(Long uploadId) {

        Upload upload = uploadRepository.findById(uploadId)
                .orElseThrow(() -> new RuntimeException("Upload not found"));

        // Update upload status → PARSING
        upload.setStatus(UploadStatus.PARSING);

        try {
            // Simulated parser output (JSON)
            JsonNode parsedJsonNode = objectMapper.readTree(
                    upload.getFileType() == FileType.RESUME
                            ? """
                                {
                                  "name": "John Doe",
                                  "skills": ["Java", "Spring Boot", "SQL"],
                                  "experience": 3
                                }
                                """
                            : """
                                {
                                  "job_title": "Backend Developer",
                                  "required_skills": ["Java", "Spring Boot"],
                                  "experience_required": 2
                                }
                                """
            );

            // Save parsed document
            ParsedDocument parsedDocument = ParsedDocument.builder()
                    .upload(upload)
                    .tenantId(upload.getTenant().getId())
                    .fileType(upload.getFileType())
                    .parsedData(parsedJsonNode)
                    .status(UploadStatus.PARSED)
                    .createdAt(LocalDateTime.now())
                    .build();

            parsedDocumentRepository.save(parsedDocument);

            // Update upload status → PARSED
            upload.setStatus(UploadStatus.PARSED);

        } catch (JsonProcessingException e) {
            // If parsing fails
            upload.setStatus(UploadStatus.FAILED);
            throw new RuntimeException("Failed to parse uploaded document", e);
        }
    }
}
