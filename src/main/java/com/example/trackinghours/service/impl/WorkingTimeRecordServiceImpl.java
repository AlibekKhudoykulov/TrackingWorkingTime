package com.example.trackinghours.service.impl;

import com.example.trackinghours.connection.DatabaseConnection;
import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.entity.Project;
import com.example.trackinghours.entity.WorkingTimeRecord;
import com.example.trackinghours.service.WorkingTimeRecordService;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkingTimeRecordServiceImpl implements WorkingTimeRecordService {

    private Connection connection;
    private EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
    private ProjectServiceImpl projectService = new ProjectServiceImpl();

    public WorkingTimeRecordServiceImpl() {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public List<WorkingTimeRecord> getAllWorkingTimeRecords() {
        List<WorkingTimeRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM working_time_record";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int employeeId = resultSet.getInt("employee_id");
                Employee employeeById = employeeService.getEmployeeById(employeeId);
                int projectId = resultSet.getInt("project_id");
                Project projectById = projectService.getProjectById(projectId);
                LocalDate date = resultSet.getDate("date").toLocalDate();
                LocalTime startedHour = resultSet.getTime("start_time").toLocalTime();
                LocalTime endedHour = resultSet.getTime("end_time").toLocalTime();
                double hoursWorked = resultSet.getDouble("hours_worked");
                WorkingTimeRecord record = new WorkingTimeRecord(id, employeeById, projectById, startedHour, endedHour,date, hoursWorked);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public void addWorkingTimeRecord(WorkingTimeRecord record) {
        String sql = "INSERT INTO working_time_record (employee_id, project_id, date, start_time, end_time, hours_worked) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, record.getEmployee().getId());
            statement.setInt(2, record.getProject().getId());
            statement.setDate(3, Date.valueOf(record.getDate()));
            statement.setTime(4, Time.valueOf(record.getStartTime()));
            statement.setTime(5, Time.valueOf(record.getEndTime()));
            statement.setDouble(6, record.getHoursWorked());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addWorkingTimeFromCSV(WorkingTimeRecord record) {
        String sql = "INSERT INTO working_time_record (employee_id, project_id, date, start_time, end_time, hours_worked) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, record.getEmployee().getId());
            statement.setInt(2, record.getProject().getId());
            statement.setDate(3, Date.valueOf(record.getDate()));
            statement.setTime(4, Time.valueOf(record.getStartTime()));
            statement.setTime(5, Time.valueOf(record.getEndTime()));
            statement.setDouble(6, record.getHoursWorked());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWorkingTimeRecord(WorkingTimeRecord record) {
        String sql = "UPDATE working_time_record SET employee_id=?, project_id=?, date=?, start_time=?, end_time=?, hours_worked=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, record.getEmployee().getId());
            statement.setInt(2, record.getProject().getId());
            statement.setDate(3, Date.valueOf(record.getDate()));
            statement.setTime(4, Time.valueOf(record.getStartTime()));
            statement.setTime(5, Time.valueOf(record.getEndTime()));
            statement.setDouble(6, record.getHoursWorked());
            statement.setInt(7, record.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWorkingTimeRecord(int id) {
        String sql = "DELETE FROM working_time_record WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
