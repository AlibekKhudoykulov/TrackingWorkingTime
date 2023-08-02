package com.example.trackinghours.service.impl;

import java.sql.*;

import com.example.trackinghours.connection.DatabaseConnection;
import com.example.trackinghours.entity.Employee;
import com.example.trackinghours.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private Connection connection;

    public EmployeeServiceImpl() {
        connection = DatabaseConnection.getConnection();
    }
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Employee employee = new Employee(id, firstName, lastName);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Employee employee = new Employee(id, firstName, lastName);
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Employee getEmployeeByName(String fullName) {
        String[] names = fullName.split("\\s+");
        String firstName = names[0];
        String lastName = names[1];

        String sql = "SELECT * FROM employee WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return new Employee(id, firstName, lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Employee getEmployeeByFirstNameAndLastName(String firstName, String lastName) {

        String sql = "SELECT * FROM employee WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return new Employee(id, firstName, lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void addEmployee(Employee employee) {
        // Save the employee to the database
        String sql = "INSERT INTO employee (first_name, last_name) VALUES ( ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
                // Update the employee in the database
                String sql = "UPDATE employee SET first_name=?, last_name=? WHERE id=?";
                try (var preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, employee.getFirstName());
                    preparedStatement.setString(2, employee.getLastName());
                    preparedStatement.setInt(3, employee.getId());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }


    @Override
    public void deleteEmployee(int id) {
        // Delete the employee from the database
        String sql = "DELETE FROM employee WHERE id=?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
