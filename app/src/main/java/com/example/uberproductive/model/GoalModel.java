package com.example.uberproductive.model;

public class GoalModel {
    private String title;
    private String dateAdded;

    public GoalModel(){}

    public GoalModel(String goalName, String dateAdded){
        this.title = goalName;
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
