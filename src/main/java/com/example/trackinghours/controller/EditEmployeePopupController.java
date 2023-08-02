package com.example.trackinghours.controller;

import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.service.EmployeeService;
import com.example.trackinghours.service.impl.EmployeeServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditEmployeePopupController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    private Employee employee;
    private EmployeeService employeeService;

    public EditEmployeePopupController() {
        this.employeeService = new EmployeeServiceImpl();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
    }

    @FXML
    public void handleSaveButton() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showErrorMessage("Please enter both first name and last name.");
            return;
        }

        if (employee != null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);

            // Update the employee in the database
            employeeService.updateEmployee(employee);
        }

        closePopup();
    }

    @FXML
    public void handleCancelButton() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
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
