package com.resume.resumematching.service;

import com.resume.resumematching.context.TenantContext;
import com.resume.resumematching.entity.MatchJob;
import com.resume.resumematching.entity.MatchResult;
import com.resume.resumematching.entity.ParsedDocument;
import com.resume.resumematching.enums.MatchJobStatus;
import com.resume.resumematching.repository.MatchJobRepository;
import com.resume.resumematching.repository.MatchResultRepository;
import com.resume.resumematching.repository.ParsedDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchJobRepository matchJobRepository;
    private final MatchResultRepository matchResultRepository;
    private final ParsedDocumentRepository parsedDocumentRepository;
    private final UsageCounterService usageCounterService;

    public MatchJob runMatch(Long jdUploadId) {

        Long tenantId = TenantContext.getTenantId();

        // 🔐 PLAN LIMIT CHECK
        usageCounterService.checkMatchLimit(tenantId);

        // Validate JD parsed
        parsedDocumentRepository
                .findByUploadIdAndTenantId(jdUploadId, tenantId)
                .orElseThrow(() -> new RuntimeException("JD not parsed"));

        MatchJob job = MatchJob.builder()
                .tenantId(tenantId)
                .jdUploadId(jdUploadId)
                .status(MatchJobStatus.RUNNING)
                .startedAt(LocalDateTime.now())
                .build();

        MatchJob savedJob = matchJobRepository.save(job);

        // Fetch parsed resumes
        List<ParsedDocument> resumes =
                parsedDocumentRepository.findParsedResumesByTenantId(tenantId);

        Random random = new Random();

        for (ParsedDocument resume : resumes) {

            MatchResult result = MatchResult.builder()
                    .matchJobId(savedJob.getId())
                    .tenantId(tenantId)
                    .resumeUploadId(resume.getUploadId())
                    .overallScore(BigDecimal.valueOf(50 + random.nextInt(50)))
                    .breakdown("""
                        {
                          "skills_match": "70%",
                          "experience_match": "65%",
                          "education_match": "80%"
                        }
                        """)
                    .createdAt(LocalDateTime.now())
                    .build();

            matchResultRepository.save(result);
        }

        savedJob.setStatus(MatchJobStatus.DONE);
        savedJob.setCompletedAt(LocalDateTime.now());
        matchJobRepository.save(savedJob);

        usageCounterService.incrementMatch(tenantId);

        return savedJob;
    }
}
