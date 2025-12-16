package com.resume.resumematching.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.dto.match.MatchResultResponse;
import com.resume.resumematching.entity.MatchJob;
import com.resume.resumematching.entity.MatchResult;
import com.resume.resumematching.entity.ParsedDocument;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.MatchJobStatus;
import com.resume.resumematching.enums.UploadStatus;
import com.resume.resumematching.repository.MatchJobRepository;
import com.resume.resumematching.repository.MatchResultRepository;
import com.resume.resumematching.repository.ParsedDocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchJobRepository matchJobRepository;
    private final MatchResultRepository matchResultRepository;
    private final ParsedDocumentRepository parsedDocumentRepository;
    private final UsageCounterService usageCounterService;
    private final ObjectMapper objectMapper;

    public MatchJob runMatch(Long jdUploadId) {

        Long tenantId = TenantContext.getTenantId();

        // 🔐 PLAN LIMIT CHECK
        usageCounterService.checkMatchLimit(tenantId);

        // ✅ Validate JD is PARSED
        ParsedDocument jd = parsedDocumentRepository
                .findByUploadIdAndTenantIdAndStatusAndFileType(
                        jdUploadId,
                        tenantId,
                        UploadStatus.PARSED,
                        FileType.JD
                )
                .orElseThrow(() -> new RuntimeException("JD not parsed"));

        // ✅ Fetch parsed resumes
        List<ParsedDocument> resumes =
                parsedDocumentRepository.findParsedResumesByTenantId(tenantId);

        if (resumes.isEmpty()) {
            throw new RuntimeException("No parsed resumes available");
        }

        // ✅ Create match job
        MatchJob job = MatchJob.builder()
                .tenantId(tenantId)
                .jdUploadId(jdUploadId)
                .status(MatchJobStatus.RUNNING)
                .startedAt(LocalDateTime.now())
                .build();

        MatchJob savedJob = matchJobRepository.save(job);

        Random random = new Random();

        for (ParsedDocument resume : resumes) {

            JsonNode breakdownJson = createDummyBreakdown();

            MatchResult result = MatchResult.builder()
                    .matchJobId(savedJob.getId())
                    .tenantId(tenantId)
                    .resumeUploadId(resume.getUpload().getId())
                    .overallScore(BigDecimal.valueOf(50 + random.nextInt(50)))
                    .breakdown(breakdownJson)   // ✅ JSONB SAFE
                    .createdAt(LocalDateTime.now())
                    .build();

            matchResultRepository.save(result);
        }

        // ✅ Mark job completed
        savedJob.setStatus(MatchJobStatus.DONE);
        savedJob.setCompletedAt(LocalDateTime.now());
        matchJobRepository.save(savedJob);

        // ✅ Increment usage AFTER success
        usageCounterService.incrementMatch(tenantId);

        return savedJob;
    }

    public List<MatchResultResponse> getResults(Long jobId) {

        Long tenantId = TenantContext.getTenantId();

        return matchResultRepository
                .findByMatchJobIdAndTenantIdOrderByOverallScoreDesc(jobId, tenantId)
                .stream()
                .map(r -> new MatchResultResponse(
                        r.getResumeUploadId(),
                        r.getOverallScore(),
                        r.getBreakdown(),
                        r.getCreatedAt()
                ))
                .toList();
    }

    // 🔹 Temporary dummy breakdown (replace with ML later)
    private JsonNode createDummyBreakdown() {
        try {
            return objectMapper.readTree("""
                {
                  "skills_match": "70%",
                  "experience_match": "65%",
                  "education_match": "80%"
                }
                """);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create breakdown JSON", e);
        }
    }
}
