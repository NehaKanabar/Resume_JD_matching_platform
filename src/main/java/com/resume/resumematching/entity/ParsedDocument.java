package com.resume.resumematching.entity;


import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parsed_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParsedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- RELATIONSHIP ---------- */

    @OneToOne(optional = false)
    @JoinColumn(name = "upload_id", nullable = false, unique = true)
    private Upload upload;

    /* ---------- MULTI TENANCY ---------- */

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    /* ---------- TYPE ---------- */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType; // RESUME / JD

    /* ---------- PARSED DATA ---------- */

    @Column(columnDefinition = "jsonb")
    private String parsedData;

    /* ---------- STATUS ---------- */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus status; // PARSED

    /* ---------- AUDIT ---------- */

    private LocalDateTime createdAt;
}
