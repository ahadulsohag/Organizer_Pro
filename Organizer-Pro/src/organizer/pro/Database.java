/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package organizer.pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

/**
 *
 * @author ahadu
 */
public class Database {

    public static Connection connectDB() {
        try {
            // Try the new driver first
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/organizerpro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", 
                    "root", 
                    ""
                );
                return connect;
            } catch (ClassNotFoundException e) {
                // Fallback to old driver if new one is not found
                System.out.println("New MySQL driver not found, using legacy driver...");
                Class.forName("com.mysql.jdbc.Driver");
                Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost/organizerpro", 
                    "root", 
                    ""
                );
                return connect;
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Add task to database
    public static void addTaskToDB(Task task, int adminId) {
        String sql = "INSERT INTO tasks (admin_id, title, description, priority, category, due_date, completed, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, adminId);
            prepare.setString(2, task.getTitle());
            prepare.setString(3, task.getDescription());
            prepare.setString(4, task.getPriority());
            prepare.setString(5, task.getCategory());
            prepare.setDate(6, task.getDueDate() != null ? java.sql.Date.valueOf(task.getDueDate()) : null);
            prepare.setBoolean(7, task.isCompleted());
            prepare.setDate(8, java.sql.Date.valueOf(task.getCreatedDate()));

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load all tasks for a specific admin
    public static ObservableList<Task> loadTasksFromDB(int adminId) {
        ObservableList<Task> taskList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tasks WHERE admin_id = ? ORDER BY created_date DESC";

        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, adminId);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                Task task = new Task(
                    result.getInt("task_id"),
                    result.getString("title"),
                    result.getString("description"),
                    result.getString("priority"),
                    result.getString("category"),
                    result.getDate("due_date") != null ? result.getDate("due_date").toLocalDate() : null,
                    result.getBoolean("completed"),
                    result.getDate("created_date").toLocalDate()
                );
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    // Update task in database
    public static void updateTaskInDB(Task task, int adminId) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, category = ?, due_date = ?, completed = ? WHERE task_id = ? AND admin_id = ?";

        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setString(1, task.getTitle());
            prepare.setString(2, task.getDescription());
            prepare.setString(3, task.getPriority());
            prepare.setString(4, task.getCategory());
            prepare.setDate(5, task.getDueDate() != null ? java.sql.Date.valueOf(task.getDueDate()) : null);
            prepare.setBoolean(6, task.isCompleted());
            prepare.setInt(7, task.getId());
            prepare.setInt(8, adminId);

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete task from database
    public static void deleteTaskFromDB(int taskId, int adminId) {
        String sql = "DELETE FROM tasks WHERE task_id = ? AND admin_id = ?";

        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, taskId);
            prepare.setInt(2, adminId);
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update task completion status
    public static void updateTaskStatus(int taskId, boolean completed, int adminId) {
        String sql = "UPDATE tasks SET completed = ? WHERE task_id = ? AND admin_id = ?";

        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setBoolean(1, completed);
            prepare.setInt(2, taskId);
            prepare.setInt(3, adminId);
            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get next task ID (for new tasks)
    public static int getNextTaskId(int adminId) {
        String sql = "SELECT MAX(task_id) as max_id FROM tasks WHERE admin_id = ?";
        
        try (Connection connect = connectDB(); 
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            prepare.setInt(1, adminId);
            ResultSet result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getInt("max_id") + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; // First task for this admin
    }
}