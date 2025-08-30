package com.fotocapture.dms_backend.dto;

import java.util.List;

public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private Integer dailyTargetMinutes;
    private List<String> roles;
    private List<String> groups; // group names for quick display

    public UserResponse() { }

    public UserResponse(Long id, String username, String fullName,
                        Integer dailyTargetMinutes, List<String> roles, List<String> groups) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.dailyTargetMinutes = dailyTargetMinutes;
        this.roles = roles;
        this.groups = groups;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getDailyTargetMinutes() { return dailyTargetMinutes; }
    public void setDailyTargetMinutes(Integer dailyTargetMinutes) { this.dailyTargetMinutes = dailyTargetMinutes; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }
}
