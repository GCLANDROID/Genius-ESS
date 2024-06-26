package com.genius.employee.model;

public class EmpDetailsModel {
    String empId,empName,dept,location,Mobile;
    boolean status;
    String SecureID;

    public EmpDetailsModel(String empId, String empName, String dept, String location,boolean status,String Mobile) {
        this.empId = empId;
        this.empName = empName;
        this.dept = dept;
        this.location = location;
        this.status=status;
        this.Mobile=Mobile;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getSecureID() {
        return SecureID;
    }

    public void setSecureID(String secureID) {
        SecureID = secureID;
    }
}
