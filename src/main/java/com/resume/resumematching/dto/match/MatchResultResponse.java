package com.resume.resumematching.dto.match;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MatchResultResponse(
        Long resumeUploadId,
        BigDecimal overallScore,
        JsonNode breakdown,
        LocalDateTime createdAt
) {}
