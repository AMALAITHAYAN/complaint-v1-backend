package com.fotocapture.dms_backend.dto;

import java.util.List;

public class GroupResponse {
    private Long id;
    private String name;
    private List<BatchPermissionDTO> batchPermissions;

    public GroupResponse() { }

    public GroupResponse(Long id, String name, List<BatchPermissionDTO> batchPermissions) {
        this.id = id;
        this.name = name;
        this.batchPermissions = batchPermissions;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<BatchPermissionDTO> getBatchPermissions() { return batchPermissions; }
    public void setBatchPermissions(List<BatchPermissionDTO> batchPermissions) { this.batchPermissions = batchPermissions; }
}
