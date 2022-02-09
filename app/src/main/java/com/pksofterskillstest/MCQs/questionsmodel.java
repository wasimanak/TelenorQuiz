package com.pksofterskillstest.MCQs;

public class questionsmodel {
    String question,answer;
    private String questiontitle;
    private String questiondescription;

    public questionsmodel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public String getQuestiondescription() {
        return questiondescription;
    }
}
