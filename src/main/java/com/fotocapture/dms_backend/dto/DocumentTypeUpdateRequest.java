package com.fotocapture.dms_backend.dto;

import java.util.List;

public class DocumentTypeUpdateRequest {
    private String name;
    private Integer avgIndexTime;
    private Integer avgQualityTime;
    private List<IndexingFieldDTO> indexingFields;
    private String folderTemplate;
    private String fileTemplate;
    private String exportType;
    private String exportFormat;
    private String colorFormat;
    private String status; // ACTIVE / INACTIVE

    public DocumentTypeUpdateRequest() {}

    // Getters/Setters
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
}
