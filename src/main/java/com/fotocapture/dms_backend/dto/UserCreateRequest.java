package com.fotocapture.dms_backend.dto;

import java.util.List;

public class UserCreateRequest {
    private String username;
    private String fullName;
    private String password;
    private Integer dailyTargetMinutes;
    private List<String> roles;  // role names like ROLE_ADMIN, ROLE_SCANNER...
    private List<Long> groupIds; // optional

    public UserCreateRequest() { }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getDailyTargetMinutes() { return dailyTargetMinutes; }
    public void setDailyTargetMinutes(Integer dailyTargetMinutes) { this.dailyTargetMinutes = dailyTargetMinutes; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<Long> getGroupIds() { return groupIds; }
    public void setGroupIds(List<Long> groupIds) { this.groupIds = groupIds; }
}
