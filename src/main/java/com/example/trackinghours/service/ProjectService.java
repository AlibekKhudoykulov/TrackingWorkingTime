package com.example.trackinghours.service;

import com.example.trackinghours.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(int id);
    Project getProjectByName(String projectName);
    Project getProjectByNameAndNumber(String projectName, String projectNumber);
    void addProject(Project project);
    void updateProject(Project project);
    void deleteProject(int id);
}
