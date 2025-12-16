package com.resume.resumematching.dto.match;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MatchResultResponse(
        Long resumeUploadId,
        BigDecimal overallScore,
        String breakdown,
        LocalDateTime createdAt
) {}
