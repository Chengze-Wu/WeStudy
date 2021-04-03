package com.appliedproject.westudy.database;

public class QuestionEntity {
    private String questionid;
    private String questionimage;
    private String questioncontent;
    private String description;
    private String publisher;

    public QuestionEntity(String questionid, String questionimage, String questioncontent, String description, String publisher) {
        this.questionid = questionid;
        this.questionimage = questionimage;
        this.questioncontent = questioncontent;
        this.description = description;
        this.publisher = publisher;
    }
    public QuestionEntity(){}

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getQuestionimage() {
        return questionimage;
    }

    public void setQuestionimage(String questionimage) {
        this.questionimage = questionimage;
    }

    public String getQuestioncontent() {
        return questioncontent;
    }

    public void setQuestioncontent(String questioncontent) {
        this.questioncontent = questioncontent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
