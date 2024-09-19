package org.example.Survey;

import org.example.User.MyUser;

import java.util.ArrayList;
import java.util.List;

public class SurveyAnswers {
    //private final List<String> questionId = new ArrayList<>(); //
    private final String[] questionId = new String[3]; // TODO: ADD TO MyUser
    private int current_question;
    private final List<String> answerId = new ArrayList<>(); // answerId.size()
    private MyUser user;
    private final Survey survey;
    private boolean handleQuestionNumber;
    private boolean handleAnswerNumber;
    public SurveyAnswers(Survey survey) {
        this.survey = survey;
    }

    public void setHandleQuestionNumber(boolean handle) {
        handleQuestionNumber = handle;
    }

    public int max_question_count() { return survey.getQuestions().size();}

    public boolean isHandleQuestionNumber() { return handleQuestionNumber; }

    public void setHandleAnswerNumber(boolean handle) {
        handleAnswerNumber = handle;
    }

    public boolean isHandleAnswerNumber() { return handleAnswerNumber; }

    //public void mapAnswerNumberForQuestion() {

   //}

    public boolean isUserAnsweredOnAllQuestion() {
        return user.getMap() >= survey.getQuestions().size() ;
    }

    // TODO: Always handling answers.get(answers.size() - 1)
    public void mapAnswerNumberForQuestion(String answerIndex) {
        if(user != null) {
            String questionIndex = questionId[current_question - 1];
            user.setQuestionToAnswer(questionIndex, answerIndex);
        }
    }

    // TODO: check index not overflow the array
    // index - 1 because user is input from 1-3 index - 1
    int max_answers_for_spec_question() {
        int index = Integer.parseInt(questionId[current_question - 1]);
        return survey.getQuestions().get(index - 1).getOptions().size();
    }






    // public void setAnswerId(String answerId) {
       // this.answerId.add(answerId);
    //}

    // index - 1 => question are listed from 1-3
    public boolean isAlreadyAnswered(int index) {
        // Not answer yet
        if(current_question == 0) return false;
        else return questionId[index - 1] != null;
    }

    public void setQuestionId(String questionId) {
        this.questionId[current_question++] = questionId;
    }

    public void setAnsweringUser(MyUser user) {
        this.user = user;
    }

}
