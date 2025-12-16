package com.resume.resumematching.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_job_id", nullable = false)
    private Long matchJobId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "resume_upload_id", nullable = false)
    private Long resumeUploadId;

    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "breakdown_json", columnDefinition = "jsonb")
    private String breakdown;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

