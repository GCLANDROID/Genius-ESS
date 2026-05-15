package com.genius.employee.activity.clientcall.model;

public class VirtualCallReportModel {
    String ClientName,callTime,Date;


    public VirtualCallReportModel(String clientName, String callTime, String date) {
        ClientName = clientName;
        this.callTime = callTime;
        Date = date;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


}
