package com.fotocapture.dms_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentTypeAdminResponse {
    private Integer id;
    private Integer departmentId;
    private String name;
    private Integer avgIndexTime;
    private Integer avgQualityTime;
    private List<IndexingFieldDTO> indexingFields;
    private String folderTemplate;
    private String fileTemplate;
    private String exportType;
    private String exportFormat;
    private String colorFormat;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public DocumentTypeAdminResponse() {}

    // Getters/Setters
    // ... (generate all manually)
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
    public List<IndexingFieldDTO> getIndexingFields() { return indexingFields; }
    public void setIndexingFields(List<IndexingFieldDTO> indexingFields) { this.indexingFields = indexingFields; }
    public String getFolderTemplate() { return folderTemplate; }
    public void setFolderTemplate(String folderTemplate) { this.folderTemplate = folderTemplate; }
    public String getFileTemplate() { return fileTemplate; }
    public void setFileTemplate(String fileTemplate) { this.fileTemplate = fileTemplate; }
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
