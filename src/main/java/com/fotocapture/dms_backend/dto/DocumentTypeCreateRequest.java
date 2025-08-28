package com.fotocapture.dms_backend.dto;

import java.util.List;

public class DocumentTypeCreateRequest {
    private Integer departmentId;
    private String name;

    private Integer avgIndexTime;
    private Integer avgQualityTime;

    private List<IndexingFieldDTO> indexingFields;

    private String folderTemplate;  // e.g., "[Department]/[YYYY]/[DocType]"
    private String fileTemplate;    // e.g., "DOC_[RandomId]_[passportNumber]"

    private String exportType;      // FILESYSTEM
    private String exportFormat;    // PDF | PDFA | TIFF | AS_IMPORTED
    private String colorFormat;     // HIGH_16B | MEDIUM_8B | GRAYSCALE

    public DocumentTypeCreateRequest() {}

    // Getters/Setters
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
}
