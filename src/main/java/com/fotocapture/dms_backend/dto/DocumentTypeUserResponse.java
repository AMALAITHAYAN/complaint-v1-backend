package com.fotocapture.dms_backend.dto;

import java.util.List;

public class DocumentTypeUserResponse {
    private Integer id;
    private String name;
    private List<IndexingFieldDTO> fieldsVisibleToUser;
    private String folderTemplate;
    private String fileTemplate;
    private String exportFormat;
    private String colorFormat;

    public DocumentTypeUserResponse() {}

    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<IndexingFieldDTO> getFieldsVisibleToUser() { return fieldsVisibleToUser; }
    public void setFieldsVisibleToUser(List<IndexingFieldDTO> fieldsVisibleToUser) { this.fieldsVisibleToUser = fieldsVisibleToUser; }

    public String getFolderTemplate() { return folderTemplate; }
    public void setFolderTemplate(String folderTemplate) { this.folderTemplate = folderTemplate; }

    public String getFileTemplate() { return fileTemplate; }
    public void setFileTemplate(String fileTemplate) { this.fileTemplate = fileTemplate; }

    public String getExportFormat() { return exportFormat; }
    public void setExportFormat(String exportFormat) { this.exportFormat = exportFormat; }

    public String getColorFormat() { return colorFormat; }
    public void setColorFormat(String colorFormat) { this.colorFormat = colorFormat; }
}
