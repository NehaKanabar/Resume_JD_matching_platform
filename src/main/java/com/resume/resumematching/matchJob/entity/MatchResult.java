package com.resume.resumematching.matchJob.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode breakdown;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

