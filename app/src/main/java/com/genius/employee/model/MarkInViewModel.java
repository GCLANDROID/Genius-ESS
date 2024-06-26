package com.genius.employee.model;

public class MarkInViewModel {
    String location,time,imageUrl,imgeName,remarks,date;
    String accessForm,punchOut,PunchOutTime;
    String flag;

    public MarkInViewModel(String location, String time, String imageUrl,String imageName,String remarks,String date) {
        this.location = location;
        this.time = time;
        this.imageUrl = imageUrl;
        this.imgeName=imageName;
        this.remarks=remarks;
        this.date=date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgeName() {
        return imgeName;
    }

    public void setImgeName(String imgeName) {
        this.imgeName = imgeName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccessForm() {
        return accessForm;
    }

    public void setAccessForm(String accessForm) {
        this.accessForm = accessForm;
    }

    public String getPunchOut() {
        return punchOut;
    }

    public void setPunchOut(String punchOut) {
        this.punchOut = punchOut;
    }

    public String getPunchOutTime() {
        return PunchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        PunchOutTime = punchOutTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
