package org.example.Buttons;

import org.example.User.Question;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;

public class Button {
    private final InlineKeyboardMarkup container;
    private List<Question> question;
    private List<List<InlineKeyboardButton>> buttons;
    private Map<String, Integer> callbackToClickTimes;
    private ButtonData buttonData;

    public Button(InlineKeyboardMarkup container) {
        this.container = container;
    }

    public void setButtonData(ButtonData data) {
        buttonData = data;
    }

    private void addButtons(List<InlineKeyboardButton> button) {
        buttons.add(button);
    }

    public void handleButtonData() {
        switch (buttonData) {
            case BUTTON_DATA_ANSWER_COUNT:
            case BUTTON_DATA_QUESTION_COUNT:
            case BUTTON_DATA_ANSWER:

            case BUTTON_DATA_SEARCH:
            default:
                throw new Error("Unreachable ");
        }
    }
}
