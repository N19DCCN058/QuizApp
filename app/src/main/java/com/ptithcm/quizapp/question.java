package com.ptithcm.quizapp;

import java.io.Serializable;

public class question implements Comparable<question>, Serializable {
    public String questionID;
    public String questionContent;
    public String questionType;
    public String questionLevel;
    public String exactAnswer;

    public question(String questionID, String questionContent, String questionType, String questionLevel, String exactAnswer) {
        this.questionID = questionID;
        this.questionContent = questionContent;
        this.questionType = questionType;
        this.questionLevel = questionLevel;
        this.exactAnswer = exactAnswer;
    }

    public question() {
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getQuestionLevel() {
        return questionLevel;
    }

    public String getExactAnswer() {
        return exactAnswer;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public void setQuestionLevel(String questionLevel) {
        this.questionLevel = questionLevel;
    }

    public void setExactAnswer(String exactAnswer) {
        this.exactAnswer = exactAnswer;
    }

    @Override
    public int compareTo(question o) {
        return this.getQuestionID().compareTo(o.getQuestionID());
    }
}
