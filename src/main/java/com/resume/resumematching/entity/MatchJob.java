package com.resume.resumematching.entity;

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
public class MatchJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "jd_upload_id", nullable = false)
    private Long jdUploadId;

    @Enumerated(EnumType.STRING)
    private MatchJobStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
