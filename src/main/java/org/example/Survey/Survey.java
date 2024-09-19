package org.example.Survey;

import org.example.User.MyUser;
import org.example.User.Question;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.List;


public class Survey {
    private MyUser creator;
    private List<Question> questions;

//    public Survey(String creator, List<Question> questions) {
//        this.creator = creator;
//        this.questions = questions;
//    }

    public String getCreatorName() {
        return creator.getFirstName();
    }
    public Long getCreatorChatId() { return creator.getChatId(); }

    public void setCreator(MyUser creator) {
        this.creator = creator;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getIndexQuestion(int index) { return questions.get(index); }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }



    public List<SendMessage> sendButtonMessage(Long chatId) {
        List<SendMessage> sendMessages = new ArrayList<>();
        for(Question question : questions) {
            InlineKeyboardMarkup container = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> buttonsGrid = new ArrayList<>();
            buttonsGrid.add(question.toButton());

            SendMessage message1 = new SendMessage();
            message1.setText("Question: " + question.getQuestion());
            message1.setChatId(String.valueOf(chatId));
            container.setKeyboard(buttonsGrid);
            message1.setReplyMarkup(container);

            sendMessages.add(message1);
        }
        return sendMessages;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i =0; i < questions.size(); ++i) {
            sb.append("Question number ").append(i + 1).append(":\n");
            sb.append(questions.get(i).getQuestion()).append("\n").append(questions.get(i));

        }
        return sb.toString();
    }
}
