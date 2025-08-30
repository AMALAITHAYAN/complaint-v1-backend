package com.fotocapture.dms_backend.dto;

public class BatchPermissionDTO {
    private Integer batchId;
    private String batchName; // for UI convenience
    private boolean scan;
    private boolean index;
    private boolean quality;

    public BatchPermissionDTO() { }

    public BatchPermissionDTO(Integer batchId, String batchName,
                              boolean scan, boolean index, boolean quality) {
        this.batchId = batchId;
        this.batchName = batchName;
        this.scan = scan;
        this.index = index;
        this.quality = quality;
    }

    public Integer getBatchId() { return batchId; }
    public void setBatchId(Integer batchId) { this.batchId = batchId; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public boolean isScan() { return scan; }
    public void setScan(boolean scan) { this.scan = scan; }

    public boolean isIndex() { return index; }
    public void setIndex(boolean index) { this.index = index; }

    public boolean isQuality() { return quality; }
    public void setQuality(boolean quality) { this.quality = quality; }
}
