package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private String question;
    private List<String> options = new ArrayList<>();
    private int answerCount;

    public void setText(String text) {
        this.question= text;
    }



    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Answers:\n");
        for(int i =0 ; i < options.size(); ++i) {
            output.append("\t\t").append(i + 1).append(". ").append(options.get(i)).append("\n");
        }
        return output.toString();
    }

    public String getQuestion() {
        return question;
    }
}
