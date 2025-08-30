package com.fotocapture.dms_backend.dto;

import java.util.List;

public class GroupCreateUpdateRequest {
    private String name;
    private List<BatchPermissionDTO> batchPermissions;

    public GroupCreateUpdateRequest() { }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<BatchPermissionDTO> getBatchPermissions() { return batchPermissions; }
    public void setBatchPermissions(List<BatchPermissionDTO> batchPermissions) { this.batchPermissions = batchPermissions; }
}
