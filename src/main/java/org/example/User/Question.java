package org.example.User;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    private InlineKeyboardButton createOptionButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }


    private int max_options() { return options.size(); }

    public List<InlineKeyboardButton>  toButton() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for(int i =0 ; i < options.size(); ++i) {
            buttons.add(createOptionButton(options.get(i), "option" + i));
        }
        return buttons;
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
