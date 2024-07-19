package com.example.projectwork.DataClasses;

public class TaskDataClass {
    private String taskTitle;
    private String taskDesc;
    private String taskImage;
    private String taskScore;

    public String getTaskTitle() {
        return taskTitle;
    }

    public TaskDataClass() {}

    public String getTaskDesc() {
        return taskDesc;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public String getTaskScore() {
        return taskScore;
    }

    public TaskDataClass(String taskTitle, String taskDesc, String taskImage, String taskScore) {
        this.taskTitle = taskTitle;
        this.taskDesc = taskDesc;
        this.taskImage = taskImage;
        this.taskScore = taskScore;
    }
}
