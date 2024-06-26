package com.genius.employee.model;

public class FamilyCovidModel {
    String realtionship,covidStatus,covidDate,covidFile,vaccineStatus,vaccineDate,vaccineFile,covidFileName,vaccineFileName,scndDoseFileName,scndDoseFile;
    String boosterDose,boosterFile;
    public FamilyCovidModel(String realtionship, String covidStatus, String covidDate, String covidFile, String vaccineStatus, String vaccineDate, String vaccineFile) {
        this.realtionship = realtionship;
        this.covidStatus = covidStatus;
        this.covidDate = covidDate;
        this.covidFile = covidFile;
        this.vaccineStatus = vaccineStatus;
        this.vaccineDate = vaccineDate;
        this.vaccineFile = vaccineFile;
    }

    public String getRealtionship() {
        return realtionship;
    }

    public void setRealtionship(String realtionship) {
        this.realtionship = realtionship;
    }

    public String getCovidStatus() {
        return covidStatus;
    }

    public void setCovidStatus(String covidStatus) {
        this.covidStatus = covidStatus;
    }

    public String getCovidDate() {
        return covidDate;
    }

    public void setCovidDate(String covidDate) {
        this.covidDate = covidDate;
    }

    public String getCovidFile() {
        return covidFile;
    }

    public void setCovidFile(String covidFile) {
        this.covidFile = covidFile;
    }

    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }

    public String getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(String vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getVaccineFile() {
        return vaccineFile;
    }

    public void setVaccineFile(String vaccineFile) {
        this.vaccineFile = vaccineFile;
    }

    public String getCovidFileName() {
        return covidFileName;
    }

    public void setCovidFileName(String covidFileName) {
        this.covidFileName = covidFileName;
    }

    public String getVaccineFileName() {
        return vaccineFileName;
    }

    public void setVaccineFileName(String vaccineFileName) {
        this.vaccineFileName = vaccineFileName;
    }

    public String getScndDoseFileName() {
        return scndDoseFileName;
    }

    public void setScndDoseFileName(String scndDoseFileName) {
        this.scndDoseFileName = scndDoseFileName;
    }

    public String getScndDoseFile() {
        return scndDoseFile;
    }

    public void setScndDoseFile(String scndDoseFile) {
        this.scndDoseFile = scndDoseFile;
    }

    public String getBoosterDose() {
        return boosterDose;
    }

    public void setBoosterDose(String boosterDose) {
        this.boosterDose = boosterDose;
    }

    public String getBoosterFile() {
        return boosterFile;
    }

    public void setBoosterFile(String boosterFile) {
        this.boosterFile = boosterFile;
    }
}
