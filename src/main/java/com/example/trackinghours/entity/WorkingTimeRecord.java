package com.example.trackinghours.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class WorkingTimeRecord {
    private int id;
    private Employee employee;
    private Project project;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private double hoursWorked;

    public WorkingTimeRecord() {
    }

    public WorkingTimeRecord(
            int id,
            Employee employee,
            Project project,
            LocalTime startTime,
            LocalTime endTime,
            LocalDate date,
            double hoursWorked) {
        this.id = id;
        this.employee = employee;
        this.project = project;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.hoursWorked = hoursWorked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getHoursWorked() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toHours();
    }
    public double getHoursWorkedCSV() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
