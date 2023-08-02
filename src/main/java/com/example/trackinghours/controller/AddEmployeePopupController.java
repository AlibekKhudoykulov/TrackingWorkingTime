package com.example.trackinghours.controller;

import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.service.EmployeeService;
import com.example.trackinghours.service.impl.EmployeeServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeePopupController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    private EmployeeService employeeService;

    public AddEmployeePopupController() {
        // Initialize the EmployeeService (you may use Dependency Injection here)
        employeeService = new EmployeeServiceImpl();
    }

    @FXML
    public void addEmployee() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showErrorMessage("Please enter first name and last name.");
            return;
        }

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(firstName);
        newEmployee.setLastName(lastName);
        employeeService.addEmployee(newEmployee);

        // Close the pop-up window after adding the employee
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
