package com.resume.resumematching.entity;


import com.resume.resumematching.enums.FileType;
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

    @Column(name = "upload_id", nullable = false)
    private Long uploadId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType; // RESUME / JD

    @Column(columnDefinition = "jsonb")
    private String parsedData; // structured JSON from parser

    @Column(nullable = false)
    private boolean parsed;

    private LocalDateTime createdAt;
}
