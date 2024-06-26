package com.genius.employee.model;

public class TrackingDetailsModel {

    String entryMarkLat, entryMarkLon, entryAddress, outMarkAddress, outMarkLat, outMarkLon, registeredOn, enteredOn;

    public TrackingDetailsModel(String entryMarkLat, String entryMarkLon, String entryAddress, String outMarkAddress, String outMarkLat, String outMarkLon, String registeredOn, String enteredOn) {

        this.entryMarkLat = entryMarkLat;
        this.entryMarkLon = entryMarkLon;
        this.entryAddress = entryAddress;
        this.outMarkAddress = outMarkAddress;
        this.outMarkLat = outMarkLat;
        this.outMarkLon = outMarkLon;
        this.registeredOn = registeredOn;
        this.enteredOn = enteredOn;
    }


    public String getEntryMarkLat() {
        return entryMarkLat;
    }

    public void setEntryMarkLat(String entryMarkLat) {
        this.entryMarkLat = entryMarkLat;
    }

    public String getEntryMarkLon() {
        return entryMarkLon;
    }

    public void setEntryMarkLon(String entryMarkLon) {
        this.entryMarkLon = entryMarkLon;
    }

    public String getEntryAddress() {
        return entryAddress;
    }

    public void setEntryAddress(String entryAddress) {
        this.entryAddress = entryAddress;
    }

    public String getOutMarkAddress() {
        return outMarkAddress;
    }

    public void setOutMarkAddress(String outMarkAddress) {
        this.outMarkAddress = outMarkAddress;
    }

    public String getOutMarkLat() {
        return outMarkLat;
    }

    public void setOutMarkLat(String outMarkLat) {
        this.outMarkLat = outMarkLat;
    }

    public String getOutMarkLon() {
        return outMarkLon;
    }

    public void setOutMarkLon(String outMarkLon) {
        this.outMarkLon = outMarkLon;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getEnteredOn() {
        return enteredOn;
    }

    public void setEnteredOn(String enteredOn) {
        this.enteredOn = enteredOn;
    }
}


