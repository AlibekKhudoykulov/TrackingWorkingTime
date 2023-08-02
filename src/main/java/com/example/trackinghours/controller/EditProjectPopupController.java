package com.example.trackinghours.controller;

import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.entity.Project;
import com.example.trackinghours.service.EmployeeService;
import com.example.trackinghours.service.ProjectService;
import com.example.trackinghours.service.impl.EmployeeServiceImpl;
import com.example.trackinghours.service.impl.ProjectServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProjectPopupController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    private Project project;
    private ProjectService projectService;

    public EditProjectPopupController() {
        this.projectService = new ProjectServiceImpl();
    }

    public void setProject(Project project) {
        this.project = project;
        nameField.setText(project.getProjectName());
        numberField.setText(project.getProjectNumber());
    }

    @FXML
    public void handleSaveButton() {
        String name = nameField.getText();
        String number = numberField.getText();

        if (name.isEmpty() || number.isEmpty()) {
            showErrorMessage("Please enter both first name and last name.");
            return;
        }

        if (project != null) {
            project.setProjectName(name);
            project.setProjectNumber(number);

            // Update the employee in the database
            projectService.updateProject(project);
        }

        closePopup();
    }

    @FXML
    public void handleCancelButton() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) nameField.getScene().getWindow();
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
