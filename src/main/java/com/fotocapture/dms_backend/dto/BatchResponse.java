package com.fotocapture.dms_backend.dto;

import java.util.List;

public class BatchResponse {
    private Integer id;
    private String departmentName;
    private String name;
    private String namingFormula;
    private Integer expectedScanTimeSec;
    private List<String> workflow;
    private String separationMethod;
    private String separationInfo;
    private Integer qualityPercentage;
    private String autoImportPath;
    private Boolean autoProcessImported;
    private List<DocTypeBrief> selectedDocumentTypes;

    public static class DocTypeBrief {
        private Integer id;
        private String name;
        public DocTypeBrief() {}
        public DocTypeBrief(Integer id, String name) { this.id = id; this.name = name; }
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public BatchResponse() {}

    // getters/setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNamingFormula() { return namingFormula; }
    public void setNamingFormula(String namingFormula) { this.namingFormula = namingFormula; }
    public Integer getExpectedScanTimeSec() { return expectedScanTimeSec; }
    public void setExpectedScanTimeSec(Integer expectedScanTimeSec) { this.expectedScanTimeSec = expectedScanTimeSec; }
    public List<String> getWorkflow() { return workflow; }
    public void setWorkflow(List<String> workflow) { this.workflow = workflow; }
    public String getSeparationMethod() { return separationMethod; }
    public void setSeparationMethod(String separationMethod) { this.separationMethod = separationMethod; }
    public String getSeparationInfo() { return separationInfo; }
    public void setSeparationInfo(String separationInfo) { this.separationInfo = separationInfo; }
    public Integer getQualityPercentage() { return qualityPercentage; }
    public void setQualityPercentage(Integer qualityPercentage) { this.qualityPercentage = qualityPercentage; }
    public String getAutoImportPath() { return autoImportPath; }
    public void setAutoImportPath(String autoImportPath) { this.autoImportPath = autoImportPath; }
    public Boolean getAutoProcessImported() { return autoProcessImported; }
    public void setAutoProcessImported(Boolean autoProcessImported) { this.autoProcessImported = autoProcessImported; }
    public List<DocTypeBrief> getSelectedDocumentTypes() { return selectedDocumentTypes; }
    public void setSelectedDocumentTypes(List<DocTypeBrief> selectedDocumentTypes) { this.selectedDocumentTypes = selectedDocumentTypes; }
}
