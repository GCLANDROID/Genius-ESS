package com.genius.employee.model;

public class LeaveModel {
    String leaveName,leaveCount;

    public LeaveModel(String leaveName, String leaveCount) {
        this.leaveName = leaveName;
        this.leaveCount = leaveCount;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public String getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(String leaveCount) {
        this.leaveCount = leaveCount;
    }
}
