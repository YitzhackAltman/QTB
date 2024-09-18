package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MyUser {
    private static int current_count_of_users = 0;
    private final String firstName;
    private final Long chatId;
    private boolean joined;
    // private final ArrayList<String> userMessages = new ArrayList<>();
    private boolean answered;
    // TODO: Track of buttonData user press
    // private final ArrayList<String> lastButtonData = new ArrayList<>();

    // Map the questionIndex to List<Answers> indicate how each user
    private final Map<String, List<Integer>> questionToAnswer = new HashMap<>();
    private final List<Integer> answerIndexes = new ArrayList<>(3);

    public MyUser(Long chatId, String firstName) {
        this.firstName = firstName;
        this.chatId = chatId;
        this.joined = false;

        current_count_of_users++;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAnswered() {
        return answered;
    }



    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }


    public String getFirstName() {
        return firstName;
    }


    public static int getAmountOfUsers() {
        return current_count_of_users;
    }

    public void setQuestionToAnswer(String questionIndex, String answerIndex) {
        final int index = Integer.parseInt(answerIndex) - 1;
        // adding one
        answerIndexes.add(index, answerIndexes.get(index) + 1);
        this.questionToAnswer.put(questionIndex, answerIndexes);
    }

    int amount_of_answers_on_question_one() {
        if(answered) {
            //Stream<String> value = questionToAnswer.values().stream().map();
        }
        return 0;
    }

    public int getMap() { return questionToAnswer.size(); }


}
