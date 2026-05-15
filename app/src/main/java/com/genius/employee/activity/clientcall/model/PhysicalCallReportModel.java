package com.genius.employee.activity.clientcall.model;

public class PhysicalCallReportModel {
    String ClientName,Distance,Date;
    int foreignTrip;

    public PhysicalCallReportModel(String clientName, String distance, String date, int foreignTrip) {
        ClientName = clientName;
        Distance = distance;
        Date = date;
        this.foreignTrip = foreignTrip;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getForeignTrip() {
        return foreignTrip;
    }

    public void setForeignTrip(int foreignTrip) {
        this.foreignTrip = foreignTrip;
    }
}
