package com.genius.employee.model;

public class AnsReportModule {
    String answer,rate;

    public AnsReportModule(String answer, String rate) {
        this.answer = answer;
        this.rate=rate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
