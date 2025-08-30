package com.fotocapture.dms_backend.dto;

import java.util.List;

public class UserUpdateRequest {
    private String fullName;
    private Integer dailyTargetMinutes;
    private List<String> roles;   // optional update
    private List<Long> groupIds;  // optional update (replace set)

    public UserUpdateRequest() { }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getDailyTargetMinutes() { return dailyTargetMinutes; }
    public void setDailyTargetMinutes(Integer dailyTargetMinutes) { this.dailyTargetMinutes = dailyTargetMinutes; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<Long> getGroupIds() { return groupIds; }
    public void setGroupIds(List<Long> groupIds) { this.groupIds = groupIds; }
}
