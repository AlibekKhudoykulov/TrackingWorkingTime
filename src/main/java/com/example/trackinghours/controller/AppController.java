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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javafx.util.Duration;


public class AppController {

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button importButton;
    @FXML
    private Button exportButton;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab timeTrackingTab;
    @FXML
    private Tab employeeTab;
    @FXML
    private Tab projectTab;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextField projectNumberField;

    @FXML
    private TableView<WorkingTimeRecord> timeTableView;
    @FXML
    private TableColumn<WorkingTimeRecord, String> dayWorkingHour;
    @FXML
    private TableColumn<WorkingTimeRecord, String> startTimeWorkingHour;
    @FXML
    private TableColumn<WorkingTimeRecord, String> endTimeWorkingHour;
    @FXML
    private TableColumn<WorkingTimeRecord, Double> hoursWorkingTime;
    @FXML
    private TableColumn<WorkingTimeRecord, String> projectNameWorkingHour;
    @FXML
    private TableColumn<WorkingTimeRecord, String> employeeNameWorkingHour;


    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> firstNameEmployee;
    @FXML
    private TableColumn<Employee, String> lastNameEmployee;

    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> nameProject;
    @FXML
    private TableColumn<Project, String> numberProject;

    @FXML
    private MenuButton projectMenu;
    @FXML
    private MenuButton employeeMenu;
    private EmployeeService employeeService;
    private ProjectService projectService;
    private WorkingTimeRecordService workingTimeRecordService;
    private StringProperty selectedProjectName = new SimpleStringProperty("Project");
    private StringProperty selectedEmployeeName = new SimpleStringProperty("Employee");
    @FXML
    private Label timerLabel;

    @FXML
    private Label dateLabel;

    private Timeline timer;
    WorkingTimeRecord record = new WorkingTimeRecord();

    private LocalTime startTime;
    public AppController() {
        // Initialize DAOs (you may use Dependency Injection here)
        employeeService = new EmployeeServiceImpl();
        projectService = new ProjectServiceImpl();
        workingTimeRecordService = new WorkingTimeRecordServiceImpl();
    }

    @FXML
    public void initialize() {
        dayWorkingHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        startTimeWorkingHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        endTimeWorkingHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));
        hoursWorkingTime.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getHoursWorked()).asObject());
        projectNameWorkingHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProject().getProjectName()));
        employeeNameWorkingHour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getFirstName()+ " " + cellData.getValue().getEmployee().getLastName()));


        nameProject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProjectName()));
        numberProject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProjectNumber()));

        firstNameEmployee.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameEmployee.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));

        employeeTableView.getColumns().addAll(createDeleteButtonColumnEmployee(), createEditButtonColumnEmployee());
        projectTableView.getColumns().addAll(createDeleteButtonColumnProject(), createEditButtonColumnProject());
        timeTableView.getColumns().addAll(createDeleteButtonColumnTime(), createEditButtonColumnTime());

        projectMenu.textProperty().bind(selectedProjectName);
        employeeMenu.textProperty().bind(selectedEmployeeName);


        // Load project data into the table
        loadProjectTableData();
        loadEmployeeTableData();
        updateEmployeeMenuItems();
        updateProjectMenuItems();
        loadWorkingTimeTableData();
    }
    private void loadProjectTableData() {
        projectTableView.getItems().setAll(projectService.getAllProjects());
    }
    private void loadEmployeeTableData() {
        employeeTableView.getItems().setAll(employeeService.getAllEmployees());
    }
    private void loadWorkingTimeTableData() {
        timeTableView.getItems().setAll(workingTimeRecordService.getAllWorkingTimeRecords());
    }
    @FXML
    public void startTracking() {
        if (selectedProjectName.getValue() == "Project" || selectedEmployeeName.getValue() == "Employee") {
            showErrorMessage("Please choose a project and an employee before starting tracking.");
            return;
        }
        LocalDate currentDate = LocalDate.now();
        startTime = LocalTime.now();
        updateTimerLabel(); // Update the label immediately
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimerLabel()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();

        // Set the current date
        dateLabel.setText("Date: " + currentDate.toString());
        record.setDate(currentDate);
        record.setStartTime(startTime);
        record.setEmployee(employeeService.getEmployeeByName(selectedEmployeeName.getValue()));
        record.setProject(projectService.getProjectByName(selectedProjectName.getValue()));

        loadWorkingTimeTableData();

    }
    private void updateTimerLabel() {

        LocalDateTime now = LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(startTime, now); // Use the full package name
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    @FXML
    public void stopTracking() {
        if (timer != null) {
            record.setEndTime(LocalTime.now());
            workingTimeRecordService.addWorkingTimeRecord(record);
            loadWorkingTimeTableData();

            timer.stop();
        }
        // Reset labels
        timerLabel.setText("Timer: 00:00:00");
        dateLabel.setText("Date: ");
    }



    @FXML
    public void importFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        Stage stage = new Stage();

        try {
            // Show the FileChooser and get the selected file
            java.io.File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    // Skip the header line
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            String employeeData = parts[0];
                            String projectData = parts[1];
                            String dateData = parts[2];
                            String startTimeStr = parts[3];
                            String endTimeStr = parts[4];
                            String hoursWorkedStr = parts[5];

                            // Parse the data and create a WorkingTimeRecord object
                            String[] employeeParts = employeeData.split(" ");
                            String firstName = employeeParts[0];
                            String lastName = employeeParts[1];
                            Employee employee = employeeService.getEmployeeByFirstNameAndLastName(firstName, lastName);
                            if (employee==null) {
                                employee = new Employee();
                                employee.setFirstName(firstName);
                                employee.setLastName(lastName);
                                employeeService.addEmployee(employee);
                            }
                            employee = employeeService.getEmployeeByFirstNameAndLastName(firstName, lastName);
                            String[] projectParts = projectData.split(" ");
                            String projectName = projectParts[0];
                            String projectNumber = projectParts[1];
                            Project project = projectService.getProjectByName(projectName);
                            if (project==null) {
                                project = new Project();
                                project.setProjectName(projectName);
                                project.setProjectNumber(projectNumber);
                                projectService.addProject(project);
                            }
                            project = projectService.getProjectByName(projectName);

                            LocalDate date = LocalDate.parse(dateData);
                            LocalTime startTime = LocalTime.parse(startTimeStr);
                            LocalTime endTime = LocalTime.parse(endTimeStr);
                            double hoursWorked = Double.parseDouble(hoursWorkedStr);

                            WorkingTimeRecord record = new WorkingTimeRecord();
                            record.setEmployee(employee);
                            record.setProject(project);
                            record.setDate(date);
                            record.setHoursWorked(hoursWorked);
                            record.setStartTime(startTime);
                            record.setEndTime(endTime);

                            workingTimeRecordService.addWorkingTimeFromCSV(record);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error importing working time records from CSV.");
        }
        loadWorkingTimeTableData();
        loadProjectTableData();
        loadEmployeeTableData();
    }

    @FXML
    public void exportToCSV() {
        List<WorkingTimeRecord> records = workingTimeRecordService.getAllWorkingTimeRecords();

        // Define the CSV file path (change the file path and name as needed)
        String filePath = "file.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write the CSV header
            writer.println("Employee FirstName and LastName,Project Name and Number,Date,Start Time,End Time,Hours Worked");

            // Write each working time record to the CSV file
            for (WorkingTimeRecord record : records) {
                String employeeNumber = String.valueOf(record.getEmployee().getFirstName() + " " + record.getEmployee().getLastName());
                String projectNumber = String.valueOf(record.getProject().getProjectName() + " " + record.getProject().getProjectNumber());
                String date = record.getDate().toString();
                String startTime = record.getStartTime().toString();
                String endTime = record.getEndTime().toString();
                String hoursWorked = String.valueOf(record.getHoursWorked());

                // Combine the values into a CSV line
                String csvLine = employeeNumber + "," + projectNumber + "," + date + "," + startTime + "," + endTime + "," + hoursWorked;
                writer.println(csvLine);
            }

            System.out.println("Working time records exported to CSV successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error exporting working time records to CSV.");
        }
    }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void showAddEmployeePopup() {
        try {
            // Load the FXML file for the pop-up window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/trackinghours/AddEmployeePopup.fxml"));
            Parent root = loader.load();

            // Create a new stage for the pop-up window
            Stage stage = new Stage();
            stage.setTitle("Add Employee");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Show the pop-up window and wait for it to be closed
            stage.showAndWait();

            loadEmployeeTableData();
            updateEmployeeMenuItems();
            loadWorkingTimeTableData(); // After the pop-up window is closed, refresh the Employee menu items
            // After the pop-up window is closed, refresh the Employee menu items
        } catch (IOException e) {
            showErrorMessage("Error loading the Add Employee pop-up.");
            e.printStackTrace();
        }
    }
    private void showEditWorkingHourPopup(WorkingTimeRecord WorkingTimeRecord) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/trackinghours/EditWorkingHourPopup.fxml"));
            Parent root = loader.load();

            EditWorkingHourPopupController controller = loader.getController();
            controller.setWorkingTimeRecord(WorkingTimeRecord);
            Stage stage = new Stage();
            stage.setTitle("Edit Working Hours");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            // Reload employee table data after editing
            loadWorkingTimeTableData();
        } catch (IOException e) {
            showErrorMessage("Error loading the Edit Working hours popup.");
            e.printStackTrace();
        }
    }
    private void showEditEmployeePopup(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/trackinghours/EditEmployeePopup.fxml"));
            Parent root = loader.load();

            EditEmployeePopupController controller = loader.getController();
            controller.setEmployee(employee);

            Stage stage = new Stage();
            stage.setTitle("Edit Employee");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            // Reload employee table data after editing
            loadEmployeeTableData();
            updateEmployeeMenuItems();
            loadWorkingTimeTableData();
        } catch (IOException e) {
            showErrorMessage("Error loading the Edit Employee popup.");
            e.printStackTrace();
        }
    }
    @FXML
    public void showAddProjectPopup() {
        try {
            // Load the FXML file for the pop-up window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/trackinghours/AddProjectPopup.fxml"));
            Parent root = loader.load();

            // Create a new stage for the pop-up window
            Stage stage = new Stage();
            stage.setTitle("Add Project");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Show the pop-up window and wait for it to be closed
            stage.showAndWait();

            // After the pop-up window is closed, refresh the Employee menu items
            loadProjectTableData();
            updateProjectMenuItems();

       } catch (IOException e) {
            showErrorMessage("Error loading the Add Employee pop-up.");
            e.printStackTrace();
        }
    }
    private void showEditProjectPopup(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/trackinghours/EditProjectPopup.fxml"));
            Parent root = loader.load();

            EditProjectPopupController controller = loader.getController();
            controller.setProject(project);

            Stage stage = new Stage();
            stage.setTitle("Edit Project");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            // Reload employee table data after editing
            loadProjectTableData();
            updateProjectMenuItems();
            loadWorkingTimeTableData();
        } catch (IOException e) {
            showErrorMessage("Error loading the Edit Project popup.");
            e.printStackTrace();
        }
    }
    private void updateProjectMenuItems() {
        try {
            List<Project> projects = projectService.getAllProjects();
            projectMenu.getItems().clear();
            for (Project project : projects) {
                MenuItem menuItem = new MenuItem(project.getProjectName()+ " " + project.getProjectNumber());
                menuItem.setOnAction(event -> handleProjectMenuItemSelected(project));
                projectMenu.getItems().add(menuItem);
            }
        } catch (Exception e) {
            showErrorMessage("Error loading projects.");
            e.printStackTrace();
        }
    }
    private void updateEmployeeMenuItems() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            employeeMenu.getItems().clear();
            for (Employee employee : employees) {
                MenuItem menuItem = new MenuItem(employee.getFirstName() + " " + employee.getLastName());
                menuItem.setOnAction(event -> handleEmployeeMenuItemSelected(employee));
                employeeMenu.getItems().add(menuItem);
            }
        } catch (Exception e) {
            showErrorMessage("Error loading employees.");
            e.printStackTrace();
        }
    }
    private TableColumn<WorkingTimeRecord, WorkingTimeRecord> createDeleteButtonColumnTime() {
        TableColumn<WorkingTimeRecord, WorkingTimeRecord> deleteButtonColumn = new TableColumn<>("Delete");
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    WorkingTimeRecord workingTimeRecord = getTableView().getItems().get(getIndex());
                    handleDeleteWorkingTime(workingTimeRecord);
                });
            }

            @Override
            protected void updateItem(WorkingTimeRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        return deleteButtonColumn;
    }
    private TableColumn<WorkingTimeRecord, WorkingTimeRecord> createEditButtonColumnTime() {
        TableColumn<WorkingTimeRecord, WorkingTimeRecord> editButtonColumn = new TableColumn<>("Edit");
        editButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    showEditWorkingHourPopup(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(WorkingTimeRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        return editButtonColumn;
    }
    private TableColumn<Employee, Employee> createDeleteButtonColumnEmployee() {
        TableColumn<Employee, Employee> deleteButtonColumn = new TableColumn<>("Delete");
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    handleDeleteEmployee(employee);
                });
            }

            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        return deleteButtonColumn;
    }
    private TableColumn<Employee, Employee> createEditButtonColumnEmployee() {
        TableColumn<Employee, Employee> editButtonColumn = new TableColumn<>("Edit");
        editButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    showEditEmployeePopup(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        return editButtonColumn;
    }
    private TableColumn<Project, Project> createDeleteButtonColumnProject() {
        TableColumn<Project, Project> deleteButtonColumn = new TableColumn<>("Delete");
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Project project = getTableView().getItems().get(getIndex());
                    handleDeleteProject(project);
                });
            }

            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        return deleteButtonColumn;
    }
    private TableColumn<Project, Project> createEditButtonColumnProject() {
        TableColumn<Project, Project> editButtonColumn = new TableColumn<>("Edit");
        editButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    showEditProjectPopup(getTableView().getItems().get(getIndex()));
                });
            }

            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        return editButtonColumn;
    }

    private void handleDeleteWorkingTime(WorkingTimeRecord workingTimeRecord) {
        workingTimeRecordService.deleteWorkingTimeRecord(workingTimeRecord.getId());
        loadWorkingTimeTableData();
    }
    private void handleEditEmployee(Employee employee) {
        employeeService.updateEmployee(employee);
        loadEmployeeTableData();
        updateEmployeeMenuItems();
    }
    private void handleDeleteEmployee(Employee employee) {
        employeeService.deleteEmployee(employee.getId());
        loadEmployeeTableData();
        updateEmployeeMenuItems();
    }

    private void handleDeleteProject(Project project) {
        projectService.deleteProject(project.getId());
        loadProjectTableData();
        updateProjectMenuItems();
    }
    private void handleEditProject(Project project) {
        projectService.updateProject(project);
        loadProjectTableData();
        updateProjectMenuItems();
    }
    private void handleProjectMenuItemSelected(Project project) {
        System.out.println("Selected project: " + project.getProjectName() + " " + project.getProjectNumber());
        selectedProjectName.set(project.getProjectName());

    }

    private void handleEmployeeMenuItemSelected(Employee employee) {
        System.out.println("Selected employee: " + employee.getFirstName() + " " + employee.getLastName());
        selectedEmployeeName.set(employee.getFirstName() + " " + employee.getLastName());
    }
}