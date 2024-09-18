package org.example;

import java.util.*;
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
    private final Map<String, String> questionToAnswer = new HashMap<>();
    private final int[] integers = new int[3];

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

    private int result(int index) { return integers.length == 0 ? 0 : integers[index]; }

    public void setQuestionToAnswer(String questionIndex, String answerIndex) {
        final int index = Integer.parseInt(answerIndex) - 1;
        // adding one
        System.out.println("--------------------------------");
        System.out.println(integers[index]);
        System.out.println("-------------------------------");
        int result = result(index) + 1;
        integers[index] = result;
        this.questionToAnswer.put(questionIndex, answerIndex);
    }

    public int getMap() { return questionToAnswer.size(); }


}
