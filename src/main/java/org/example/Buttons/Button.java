package org.example.Buttons;

import org.example.User.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class Button {
    protected String buttonQuestion;
    private List<String> options;
    // Map wich question to countOfClicksOfUsers
    private final Map<String, Integer> callbackToClickCount = new HashMap<>();
    private  ButtonData buttonData;
    private final String questionCallback;
    private final int arr[];
    private int index;
    private final List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();
    private final List<Question> questions = new ArrayList<>();

    private final List<AnswerButtons> answerButtons = new ArrayList<>();

    public Button(String question, List<String> options) {
        this.buttonQuestion = question;
        this.options = options;
        this.arr = new int[options.size()];

        SET_BUTTONS();
        questionCallback = answerButtons.get(1).getCallbackData();

        check();
    }

    public String getButtonQuestion() {
        return buttonQuestion;
    }


    public void SET_BUTTONS() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for(String option : options) {
            AnswerButtons button = new AnswerButtons(option);
            answerButtons.add(button);
            buttons.add(createButton(option, button.getCallbackData()));
        }
        addButton(buttons);
    }

    public void check() {
        if (options.size() != arr.length && arr.length != answerButtons.size()) throw new AssertionError();
    }

    public InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

//    public int sumAllAnswers() {
//        for (int i = 0; i < answerButtons.size(); ++i) {
//            arr[i] = answerButtons.get(i).getNumberOfOption();
//        }
//    }

    public SendMessage sendButtons(Long chatId) {
        InlineKeyboardMarkup container = new InlineKeyboardMarkup();
        container.setKeyboard(buttonsGrid);

        SendMessage message1 = new SendMessage();
        message1.setText("Question: " + buttonQuestion);
        message1.setChatId(String.valueOf(chatId));
        container.setKeyboard(buttonsGrid);
        message1.setReplyMarkup(container);

        return message1;
    }

    private void addButton(List<InlineKeyboardButton> button) {
        buttonsGrid.add(button);
    }

    public void setButtonData(ButtonData data) {
        buttonData = data;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setButtonQuestion(String question) {
        this.buttonQuestion = question;
    }


    public boolean isAnsweredOnQuestion() {
        assert questionCallback != null;

        return callbackToClickCount.get(questionCallback) != null;
    }

    private void markButtonAsClicked() {
        int currentCount = callbackToClickCount.getOrDefault(questionCallback, 0);
        callbackToClickCount.put(questionCallback, currentCount + 1);
        arr[index] = currentCount + 1;
    }

    private boolean isInCallback(String callback) {
        for (int i = 0; i < answerButtons.size(); ++i) {
            if(answerButtons.get(i).getCallbackData().equals(callback)) {
                answerButtons.get(i).setNumberOfOptions(i);
                index = i;
                return true;
            }
        }
        return false;
    }

    public List<String> calculatePresent(int totalUsers) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            double present = (double) (arr[i] / totalUsers) * 100;
            result.add(options.get(i) + " " + present + "%\n");
        }
        return result;
    }

    public void processButtonClick(String callbackData) {
        if(!isInCallback(callbackData)) return;

        if (isAnsweredOnQuestion()) {
            return;
        }

        markButtonAsClicked();

        // handleButtonData(callbackData);
    }


//    public void handleButtonData(String callback) {
//        switch (buttonData) {
//            case BUTTON_DATA_ANSWER_COUNT: break;
//            case BUTTON_DATA_QUESTION_COUNT: do_something(); break;
//            case BUTTON_DATA_QUESTION: do_sf(); break;
//            case BUTTON_DATA_ANSWER: check_button_data(); break;
//            case BUTTON_DATA_SEARCH:
//            default:
//                throw new Error("Unreachable ");
//        }
//    }
}