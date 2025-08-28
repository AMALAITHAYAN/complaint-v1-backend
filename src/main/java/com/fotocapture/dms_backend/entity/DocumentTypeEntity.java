package com.fotocapture.dms_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_type",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"department_id", "name"}) })
public class DocumentTypeEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "avg_index_time")
    private Integer avgIndexTime; // minutes

    @Column(name = "avg_quality_time")
    private Integer avgQualityTime; // minutes

    /** JSON string of List<IndexingFieldDTO> */
    @Column(name = "indexing_field", columnDefinition = "TEXT")
    private String indexingField;

    /** Folder formula template, e.g. "[Department]/[YYYY]/[DocType]" */
    @Column(name = "folder_format", columnDefinition = "TEXT")
    private String folderFormat;

    /** File name template, e.g. "DOC_[RandomId]_[Passport]" */
    @Column(name = "file_format", columnDefinition = "TEXT")
    private String fileFormat;

    /** FILESYSTEM (now), SHAREPOINT (future) */
    @Column(name = "export_type", nullable = false, length = 30)
    private String exportType;

    /** PDF, PDFA, TIFF, AS_IMPORTED */
    @Column(name = "export_format", nullable = false, length = 30)
    private String exportFormat;

    /** HIGH_16B, MEDIUM_8B, GRAYSCALE */
    @Column(name = "color_format", nullable = false, length = 30)
    private String colorFormat;

    /** ACTIVE / INACTIVE (soft delete) */
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 120)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 120)
    private String updatedBy;

    public DocumentTypeEntity() { }

    public DocumentTypeEntity(Integer departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters & Setters (manual)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAvgIndexTime() { return avgIndexTime; }
    public void setAvgIndexTime(Integer avgIndexTime) { this.avgIndexTime = avgIndexTime; }

    public Integer getAvgQualityTime() { return avgQualityTime; }
    public void setAvgQualityTime(Integer avgQualityTime) { this.avgQualityTime = avgQualityTime; }

    public String getIndexingField() { return indexingField; }
    public void setIndexingField(String indexingField) { this.indexingField = indexingField; }

    public String getFolderFormat() { return folderFormat; }
    public void setFolderFormat(String folderFormat) { this.folderFormat = folderFormat; }

    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }

    public String getExportType() { return exportType; }
    public void setExportType(String exportType) { this.exportType = exportType; }

    public String getExportFormat() { return exportFormat; }
    public void setExportFormat(String exportFormat) { this.exportFormat = exportFormat; }

    public String getColorFormat() { return colorFormat; }
    public void setColorFormat(String colorFormat) { this.colorFormat = colorFormat; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
