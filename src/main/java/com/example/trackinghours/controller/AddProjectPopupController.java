package com.example.trackinghours.controller;
import com.example.trackinghours.entity.Project;
import com.example.trackinghours.service.ProjectService;
import com.example.trackinghours.service.impl.ProjectServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProjectPopupController {
    @FXML
    private TextField projectNameField;
    @FXML
    private TextField projectNumberField;

    private ProjectService projectService;

    public AddProjectPopupController() {
        // Initialize the ProjectService (you may use Dependency Injection here)
        projectService = new ProjectServiceImpl();
    }

    @FXML
    public void addProject() {
        String projectName = projectNameField.getText();
        String projectNumber = projectNumberField.getText();

        if (projectName.isEmpty() || projectNumber.isEmpty()) {
            showErrorMessage("Please enter project name and number.");
            return;
        }

        Project newProject = new Project();
        newProject.setProjectName(projectName);
        newProject.setProjectNumber(projectNumber);

        projectService.addProject(newProject);

        // Close the pop-up window after adding the project
        Stage stage = (Stage) projectNameField.getScene().getWindow();
        stage.close();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
