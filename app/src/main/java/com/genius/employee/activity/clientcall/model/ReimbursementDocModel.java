package com.genius.employee.activity.clientcall.model;

public class ReimbursementDocModel {
    String docName;

    public ReimbursementDocModel(String docName) {
        this.docName = docName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
