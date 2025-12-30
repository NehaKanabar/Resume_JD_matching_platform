package com.resume.resumematching.matchJob.entity;

import com.resume.resumematching.common.audit.Auditable;
import com.resume.resumematching.enums.MatchJobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "match_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchJob extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "jd_upload_id", nullable = false)
    private Long jdUploadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchJobStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
