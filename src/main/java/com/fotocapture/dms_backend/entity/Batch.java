package com.fotocapture.dms_backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "batch")
public class Batch {

    public enum SeparationMethod { NONE, DOCUMENT_SEPARATORS, NUMBER_OF_PAGES }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** optional free text to match your current style */
    @Column(length = 255)
    private String departmentName;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 512)
    private String namingFormula;

    private Integer expectedScanTimeSec;

    /** Store as JSON string: ["SCAN","INDEX","QUALITY","EXPORT"] */
    @Column(columnDefinition = "json")
    private String workflowJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SeparationMethod separationMethod = SeparationMethod.NONE;

    @Column(length = 255)
    private String separationInfo;

    private Integer qualityPercentage; // 0..100

    @Column(length = 512)
    private String autoImportPath;

    @Column(nullable = false)
    private boolean autoProcessImported = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public Batch() {}

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // getters/setters
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

    public String getWorkflowJson() { return workflowJson; }
    public void setWorkflowJson(String workflowJson) { this.workflowJson = workflowJson; }

    public SeparationMethod getSeparationMethod() { return separationMethod; }
    public void setSeparationMethod(SeparationMethod separationMethod) { this.separationMethod = separationMethod; }

    public String getSeparationInfo() { return separationInfo; }
    public void setSeparationInfo(String separationInfo) { this.separationInfo = separationInfo; }

    public Integer getQualityPercentage() { return qualityPercentage; }
    public void setQualityPercentage(Integer qualityPercentage) { this.qualityPercentage = qualityPercentage; }

    public String getAutoImportPath() { return autoImportPath; }
    public void setAutoImportPath(String autoImportPath) { this.autoImportPath = autoImportPath; }

    public boolean isAutoProcessImported() { return autoProcessImported; }
    public void setAutoProcessImported(boolean autoProcessImported) { this.autoProcessImported = autoProcessImported; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
