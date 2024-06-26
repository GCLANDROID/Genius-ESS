package com.genius.employee.model;

public class SalaryModel {
    String year,monthlyGross,actualGross,deduction,montlyNet,esiDeduction,pfDeduction,month,weburl;

    public SalaryModel(String year, String monthlyGross, String actualGross, String deduction, String montlyNet, String esiDeduction, String pfDeduction, String month,String weburl) {
        this.year = year;
        this.monthlyGross = monthlyGross;
        this.actualGross = actualGross;
        this.deduction = deduction;
        this.montlyNet = montlyNet;
        this.esiDeduction = esiDeduction;
        this.pfDeduction = pfDeduction;
        this.month = month;
        this.weburl=weburl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonthlyGross() {
        return monthlyGross;
    }

    public void setMonthlyGross(String monthlyGross) {
        this.monthlyGross = monthlyGross;
    }

    public String getActualGross() {
        return actualGross;
    }

    public void setActualGross(String actualGross) {
        this.actualGross = actualGross;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getMontlyNet() {
        return montlyNet;
    }

    public void setMontlyNet(String montlyNet) {
        this.montlyNet = montlyNet;
    }

    public String getEsiDeduction() {
        return esiDeduction;
    }

    public void setEsiDeduction(String esiDeduction) {
        this.esiDeduction = esiDeduction;
    }

    public String getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(String pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }
}
