package com.example.trackinghours.controller;

import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.entity.Project;
import com.example.trackinghours.entity.WorkingTimeRecord;
import com.example.trackinghours.service.EmployeeService;
import com.example.trackinghours.service.ProjectService;
import com.example.trackinghours.service.WorkingTimeRecordService;
import com.example.trackinghours.service.impl.EmployeeServiceImpl;
import com.example.trackinghours.service.impl.ProjectServiceImpl;
import com.example.trackinghours.service.impl.WorkingTimeRecordServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class EditWorkingHourPopupController {
    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private ComboBox<String> employeeComboBox;
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private Label errorLabel;

    private WorkingTimeRecord workingTimeRecord;
    private ProjectService projectService = new ProjectServiceImpl();
    private EmployeeService employeeService = new EmployeeServiceImpl();
    private WorkingTimeRecordService workingTimeRecordService = new WorkingTimeRecordServiceImpl();
    public void setWorkingTimeRecord(WorkingTimeRecord workingTimeRecord) {
        this.workingTimeRecord = workingTimeRecord;

        projectComboBox.getItems().clear(); // Clear previous items if any
        projectService.getAllProjects().forEach(project -> projectComboBox.getItems().add(project.getProjectName()));
        projectComboBox.setValue(workingTimeRecord.getProject().getProjectName());

        employeeComboBox.getItems().clear(); // Clear previous items if any
        employeeService.getAllEmployees().forEach(employee -> employeeComboBox.getItems().add(employee.getFirstName() + " " + employee.getLastName()));
        employeeComboBox.setValue(workingTimeRecord.getEmployee().getFirstName() + " " + workingTimeRecord.getEmployee().getLastName());//        startTimePicker.setValue(workingTimeRecord.getStartTime());

        datePicker.setValue(workingTimeRecord.getDate());
        startTimeField.setText(workingTimeRecord.getStartTime().toString());
        endTimeField.setText(workingTimeRecord.getEndTime().toString());

    }

    @FXML
    public void handleSaveButton() {
        // Validate the input and update the workingTimeRecord object
        String selectedProject = projectComboBox.getValue();
        String selectedEmployee = employeeComboBox.getValue();
        LocalTime startTime = LocalTime.parse(startTimeField.getText());
        LocalTime endTime = LocalTime.parse(endTimeField.getText());
        LocalDate value = datePicker.getValue();

        if (selectedProject == null
                        || selectedEmployee == null
                        || datePicker.getValue() == null
                        || startTimeField.getText().isEmpty()
                        || endTimeField.getText().isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        Project projectByName = projectService.getProjectByName(selectedProject);
        Employee employeeByName = employeeService.getEmployeeByName(selectedEmployee);
        workingTimeRecord.setProject(projectByName);
        workingTimeRecord.setEmployee(employeeByName);
        workingTimeRecord.setStartTime(startTime);
        workingTimeRecord.setEndTime(endTime);
        workingTimeRecord.setDate(value);

        workingTimeRecordService.updateWorkingTimeRecord(workingTimeRecord);

        // Close the popup window after editing
        closePopup();
    }

    @FXML
    public void handleCancelButton() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) projectComboBox.getScene().getWindow();
        stage.close();
    }
}
