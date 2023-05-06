package com.ptithcm.quizapp;

import java.util.ArrayList;
import java.util.List;

public class BlankQuestion {
    private List<String> strings;
    private String orderNumber;
    public BlankQuestion() {

    }

    public BlankQuestion(String string) {
        strings = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < string.length(); index++){
            char c = string.charAt(index);
            if ((int) c == 95){
                if (stringBuilder.length() > 0){
                    String str = stringBuilder.toString();
                    strings.add(str);
                    stringBuilder.setLength(0);
                }
                strings.add(String.valueOf(c));
            }
            else{
                stringBuilder.append(c);
            }
        }
        if (stringBuilder.length() > 0)
            strings.add(stringBuilder.toString());
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public BlankQuestion(List<String> strings) {
        this.strings = strings;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
