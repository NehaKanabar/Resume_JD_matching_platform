package com.resume.resumematching.usage.dto;

public record UsageResponse(

        Long tenantId,

        String planName,

        int resumeUsed,
        int resumeLimit,

        int jdUsed,
        int jdLimit,

        int matchUsed,
        int matchLimit,

        boolean nearLimit // true if any usage >= 80%
) {}
