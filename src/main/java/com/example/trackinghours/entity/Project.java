package com.example.trackinghours.entity;

public class Project {
    private int id;
    private String projectName;
    private String projectNumber;

    public Project() {
    }

    public Project(int id, String projectName, String projectNumber) {
        this.id = id;
        this.projectName = projectName;
        this.projectNumber = projectNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }
}
