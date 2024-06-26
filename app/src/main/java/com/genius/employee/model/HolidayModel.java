package com.genius.employee.model;

public class HolidayModel {
    String name,date,day;
    String IsColor;
    int count;

    public HolidayModel(String name, String date, String day) {
        this.name = name;
        this.date = date;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIsColor() {
        return IsColor;
    }

    public void setIsColor(String isColor) {
        IsColor = isColor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
