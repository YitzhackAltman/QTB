package org.example;

import java.util.List;
import java.util.Map;

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
