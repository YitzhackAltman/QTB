package org.example.Survey;

import org.example.User.MyUser;
import org.example.User.Question;

import java.util.ArrayList;
import java.util.List;

public class SurveyTracker {
    private final Long chatId;
    private final MyUser creator;
    private int numberOfQuestions;
    private final List<Question> questions = new ArrayList<>();
    private boolean waitingForAnswerCount;
    private boolean waitingForQuestionText;
    private static boolean surveyStarted;
    private ArrayList<String> questionId;

    public boolean isWaitingForAnswerText() {
        return waitingForAnswerText;
    }

    public int getAnswersCount() {
        return questions.get(questions.size() - 1).getAnswerCount();
    }

    public void setWaitingForAnswerText(boolean waitingForAnswerText) {
        this.waitingForAnswerText = waitingForAnswerText;
    }

    public static void setSurveyStart(boolean created) {
        surveyStarted = created;
    }

    public static boolean isSurveyIsStarted() {
        return surveyStarted;
    }

    private boolean waitingForAnswerText;

    public SurveyTracker(Long chatId, MyUser creator) {
        this.chatId = chatId;
        this.creator = creator;
    }


    public Survey toSurvey() {
        Survey survey = new Survey();
        survey.setQuestions(new ArrayList<>(questions));
        survey.setCreator(creator);
        return survey;
    }

    public Long getChatId() {
        return chatId;
    }


    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public boolean isWaitingForAnswerCount() {
        return waitingForAnswerCount;
    }

    public void setWaitingForAnswerCount(boolean waitingForAnswerCount) {
        this.waitingForAnswerCount = waitingForAnswerCount;
    }

    public boolean isWaitingForQuestionText() {
        return waitingForQuestionText;
    }

    public int getCurrentQuestionNumber() {
        return questions.size();
    }

    public void addQuestion(String question, List<String> options) {
        Question currentQuestion = questions.get(getCurrentQuestionNumber() - 1);
        currentQuestion.setOptions(options);
        currentQuestion.setText(question);
    }

    public void setAnswerCountForCurrentQuestion(int answerCount) {
        Question question = new Question();
        question.setAnswerCount(answerCount);
        questions.add(question);
    }

    public void setWaitingForQuestionText(boolean waitingForQuestionText) {
        this.waitingForQuestionText = waitingForQuestionText;
    }


    public void setQuestionWantToAnswer(String questionNumber) {
        if(!questionId.contains(questionNumber)) {
            questionId.add(questionNumber);
        }
    }

    public boolean isAlreadyAnswered(String questionNumber) {
        return questionId.contains(questionNumber);
    }
}
