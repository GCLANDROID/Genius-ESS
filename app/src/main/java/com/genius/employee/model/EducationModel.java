package com.genius.employee.model;

public class EducationModel {
    String qualification,specification,institute;

    public EducationModel(String qualification, String specification, String institute) {
        this.qualification = qualification;
        this.specification = specification;
        this.institute = institute;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
