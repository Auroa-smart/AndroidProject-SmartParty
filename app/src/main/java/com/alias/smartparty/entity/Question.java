package com.alias.smartparty.entity;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String describe;
    private String level;
    private int score;
//    private List<String> options;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;

    public Question(Integer id,String describe, String level, int score, String optionA, String optionB, String optionC, String optionD, String answer) {
        this.id = id;
        this.describe = describe;
        this.level = level;
        this.score = score;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
    }

    public String getDescribe() {
        return describe;
    }

    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        options.add(optionA);
        options.add(optionB);
        options.add(optionC);
        options.add(optionD);
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public String getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
}