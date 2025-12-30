package com.resume.resumematching.upload.entity;

import com.resume.resumematching.parse.entity.ParsedDocument;
import com.resume.resumematching.user.entity.User;
import com.resume.resumematching.enums.FileType;
import com.resume.resumematching.enums.UploadStatus;
import com.resume.resumematching.tenant.entity.Tenant;
import jakarta.persistence.*;
import com.resume.resumematching.common.audit.Auditable;
import lombok.*;

@Entity
@Table(name = "upload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Upload extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FileType fileType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadStatus status;

    @OneToOne(mappedBy = "upload", cascade = CascadeType.ALL)
    private ParsedDocument parsedDocument;
}
