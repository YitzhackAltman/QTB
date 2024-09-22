package org.example.Buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnswerButtons {
    private final String callbackData;
    private static int value = 0;


    public AnswerButtons(String choice) {
        this.callbackData = choice.charAt(0) + generateCallbackData() +  choice.charAt(choice.length() - 1) + value;
        value++;
    }

    public String getCallbackData() {
        return callbackData;
    }

    private String generateCallbackData() {
        return String.valueOf(new Random().nextInt());
    }



}
