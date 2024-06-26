package com.genius.employee.model;

public class Declarition2Model {
    String question,questionid,answervalue;
    private boolean isSelected = false;


    public Declarition2Model(String question, String questionid) {
        this.question = question;
        this.questionid = questionid;


    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getAnswervalue() {
        return answervalue;
    }

    public void setAnswervalue(String answervalue) {
        this.answervalue = answervalue;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
