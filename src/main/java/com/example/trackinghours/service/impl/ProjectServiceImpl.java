package com.example.trackinghours.service.impl;

import com.example.trackinghours.connection.DatabaseConnection;
import com.example.trackinghours.entity.Project;
import com.example.trackinghours.service.ProjectService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    private Connection connection;

    public ProjectServiceImpl() {
        connection = DatabaseConnection.getConnection();
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM project";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Project project = new Project(id, name, description);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public Project getProjectById(int id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                return new Project(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Project getProjectByName(String name) {
        String sql = "SELECT * FROM project WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                return new Project(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Project getProjectByNameAndNumber(String name , String number) {
        String sql = "SELECT * FROM project WHERE name =  ? AND description = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                return new Project(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO project (name, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getProjectNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProject(Project project) {
        String sql = "UPDATE project SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getProjectNumber());
            preparedStatement.setInt(3, project.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
