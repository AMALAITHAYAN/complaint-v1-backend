package com.fotocapture.dms_backend.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class BatchCreateUpdateRequest {

    private String departmentName; // optional free text

    @NotBlank
    private String name;

    private String namingFormula;

    private Integer expectedScanTimeSec;

    @NotNull
    private List<@NotBlank String> workflow; // ["SCAN","INDEX","QUALITY","EXPORT"]

    @NotNull
    private Separation separation;

    @Min(0) @Max(100)
    private Integer qualityPercentage;

    private String autoImportPath;
    private Boolean autoProcessImported;

    @NotNull
    private List<@NotNull Integer> selectedDocumentTypeIds;

    public static class Separation {
        @NotBlank
        private String method; // NONE | DOCUMENT_SEPARATORS | NUMBER_OF_PAGES
        private String info;

        public Separation() {}
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getInfo() { return info; }
        public void setInfo(String info) { this.info = info; }
    }

    public BatchCreateUpdateRequest() {}

    // getters/setters
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

    public Separation getSeparation() { return separation; }
    public void setSeparation(Separation separation) { this.separation = separation; }

    public Integer getQualityPercentage() { return qualityPercentage; }
    public void setQualityPercentage(Integer qualityPercentage) { this.qualityPercentage = qualityPercentage; }

    public String getAutoImportPath() { return autoImportPath; }
    public void setAutoImportPath(String autoImportPath) { this.autoImportPath = autoImportPath; }

    public Boolean getAutoProcessImported() { return autoProcessImported; }
    public void setAutoProcessImported(Boolean autoProcessImported) { this.autoProcessImported = autoProcessImported; }

    public List<Integer> getSelectedDocumentTypeIds() { return selectedDocumentTypeIds; }
    public void setSelectedDocumentTypeIds(List<Integer> selectedDocumentTypeIds) { this.selectedDocumentTypeIds = selectedDocumentTypeIds; }
}
