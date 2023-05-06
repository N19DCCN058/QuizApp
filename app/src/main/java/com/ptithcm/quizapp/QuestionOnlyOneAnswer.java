package com.ptithcm.quizapp;

public class QuestionOnlyOneAnswer {
    private String orderNumber;
    private String questionContent;
    private String aAnswer;
    private String bAnswer;
    private String cAnswer;
    private String dAnswer;

    public QuestionOnlyOneAnswer() {
    }

    public QuestionOnlyOneAnswer(String questionContent, String aAnswer, String bAnswer, String cAnswer, String dAnswer) {
        this.questionContent = questionContent;
        this.aAnswer = aAnswer;
        this.bAnswer = bAnswer;
        this.cAnswer = cAnswer;
        this.dAnswer = dAnswer;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getaAnswer() {
        return aAnswer;
    }

    public void setaAnswer(String aAnswer) {
        this.aAnswer = aAnswer;
    }

    public String getbAnswer() {
        return bAnswer;
    }

    public void setbAnswer(String bAnswer) {
        this.bAnswer = bAnswer;
    }

    public String getcAnswer() {
        return cAnswer;
    }

    public void setcAnswer(String cAnswer) {
        this.cAnswer = cAnswer;
    }

    public String getdAnswer() {
        return dAnswer;
    }

    public void setdAnswer(String dAnswer) {
        this.dAnswer = dAnswer;
    }
}
