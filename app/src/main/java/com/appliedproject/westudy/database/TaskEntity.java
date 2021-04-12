package com.appliedproject.westudy.database;

public class TaskEntity {
    private String taskid;
    private String taskgoal;
    private String tasktime;
    private String publisher;
    private String description;
    private String topic;
    private int status;//-1: failed; 0: haven't started; 1: successful

    public TaskEntity(String taskid, String taskgoal, String tasktime, String publisher, String description, String topic, int status) {
        this.taskid = taskid;
        this.taskgoal = taskgoal;
        this.tasktime = tasktime;
        this.publisher = publisher;
        this.description = description;
        this.topic = topic;
        this.status = status;
    }
    public TaskEntity(){}

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTaskgoal() {
        return taskgoal;
    }

    public void setTaskgoal(String taskgoal) {
        this.taskgoal = taskgoal;
    }

    public String getTasktime() {
        return tasktime;
    }

    public void setTasktime(String tasktime) {
        this.tasktime = tasktime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}