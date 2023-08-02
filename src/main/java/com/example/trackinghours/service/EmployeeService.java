package com.example.trackinghours.service;

import com.example.trackinghours.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(int id);
    Employee getEmployeeByName(String name);
    Employee getEmployeeByFirstNameAndLastName(String firstName, String lastName);
    void addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(int id);
}
