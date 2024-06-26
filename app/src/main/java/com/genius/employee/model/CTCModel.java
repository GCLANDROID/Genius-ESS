package com.genius.employee.model;

public class CTCModel {
    String date,monthlyGross,yearlyGross,designation,yearlyCTC,monthlyCTC;

    public CTCModel(String date, String monthlyGross, String yearlyGross, String designation,String yearlyCTC,String monthlyCTC) {
        this.date = date;
        this.monthlyGross = monthlyGross;
        this.yearlyGross = yearlyGross;
        this.designation = designation;
        this.yearlyCTC=yearlyCTC;
        this.monthlyCTC=monthlyCTC;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthlyGross() {
        return monthlyGross;
    }

    public void setMonthlyGross(String monthlyGross) {
        this.monthlyGross = monthlyGross;
    }

    public String getYearlyGross() {
        return yearlyGross;
    }

    public void setYearlyGross(String yearlyGross) {
        this.yearlyGross = yearlyGross;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getYearlyCTC() {
        return yearlyCTC;
    }

    public void setYearlyCTC(String yearlyCTC) {
        this.yearlyCTC = yearlyCTC;
    }

    public String getMonthlyCTC() {
        return monthlyCTC;
    }

    public void setMonthlyCTC(String monthlyCTC) {
        this.monthlyCTC = monthlyCTC;
    }
}
