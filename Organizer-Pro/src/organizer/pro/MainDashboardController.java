package organizer.pro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainDashboardController implements Initializable {

    // Header components
    @FXML
    private AnchorPane header_pane;
    @FXML
    private Label welcome_user;
    @FXML
    private Button logout_btn;

    // Sidebar components
    @FXML
    private AnchorPane sidebar_pane;
    @FXML
    private Button all_tasks_btn, pending_tasks_btn, completed_tasks_btn, overdue_tasks_btn;
    @FXML
    private Button high_priority_btn, medium_priority_btn, low_priority_btn;
    @FXML
    private Label total_tasks_label, completed_tasks_label, pending_tasks_label;

    // Task input components
    @FXML
    private AnchorPane task_input_pane;
    @FXML
    private TextField task_title;
    @FXML
    private TextArea task_description;
    @FXML
    private ComboBox<String> task_priority, task_category;
    @FXML
    private DatePicker task_due_date;
    @FXML
    private Button add_task_btn, clear_form_btn, update_task_btn;

    // Task list components
    @FXML
    private ListView<Task> task_list_view;
    @FXML
    private ComboBox<String> filter_combo;
    @FXML
    private TextField search_field;
    @FXML
    private MenuButton sort_menu;
    @FXML
    private MenuItem sort_title, sort_priority, sort_date, sort_created;
    @FXML
    private Button edit_task_btn, delete_task_btn, mark_complete_btn, mark_pending_btn, duplicate_task_btn;

    // Data
    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private ObservableList<Task> filteredTasks = FXCollections.observableArrayList();
    private Task selectedTask = null;
    private boolean isEditMode = false;
    private int currentAdminId;
    private AlertMessage alert = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentAdminId = FXMLDocumentController.Data.admin_id;
        setupComboBoxes();
        setupTaskListView();
        
        // Load tasks from database
        loadTasksFromDatabase();
        showAllTasks();
        updateStatistics();
    }

    // Load tasks from database
    private void loadTasksFromDatabase() {
        allTasks.clear();
        ObservableList<Task> dbTasks = Database.loadTasksFromDB(currentAdminId);
        allTasks.addAll(dbTasks);
    }

    // Update the addTask method
    @FXML
    private void addTask(ActionEvent event) {
        if (isEditMode) {
            updateTask(event);
            return;
        }

        if (task_title.getText().trim().isEmpty()) {
            alert.errorMessage("Task title cannot be empty!");
            return;
        }

        // Get next available task ID
        int nextId = Database.getNextTaskId(currentAdminId);
        
        Task newTask = new Task(
                nextId,
                task_title.getText().trim(),
                task_description.getText().trim(),
                task_priority.getValue(),
                task_category.getValue(),
                task_due_date.getValue(),
                false,
                LocalDate.now()
        );

        // Add to database first
        Database.addTaskToDB(newTask, currentAdminId);

        // Then add to local list
        allTasks.add(newTask);
        refreshTaskList();
        clearForm(event);
        updateStatistics();
        alert.SuccessMessage("Task added successfully!");
    }

    private void setupComboBoxes() {
        // Priority options
        task_priority.setItems(FXCollections.observableArrayList("High", "Medium", "Low"));
        task_priority.setValue("Medium");

        // Category options
        task_category.setItems(FXCollections.observableArrayList(
                "Work", "Personal", "Shopping", "Health", "Education", "Travel", "Other"
        ));
        task_category.setValue("Personal");

        // Filter options
        filter_combo.setItems(FXCollections.observableArrayList(
                "All Tasks", "Pending", "Completed", "Overdue", "High Priority", "Medium Priority", "Low Priority"
        ));
        filter_combo.setValue("All Tasks");
    }

    private void setupTaskListView() {
        task_list_view.setItems(filteredTasks);

        // Custom cell factory for better task display
        task_list_view.setCellFactory(listView -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a custom layout for each task item
                    VBox taskContainer = new VBox(5);
                    HBox titleRow = new HBox(10);

                    CheckBox completedCheckBox = new CheckBox();
                    completedCheckBox.setSelected(task.isCompleted());
                    completedCheckBox.setOnAction(e -> toggleTaskCompletion(task));

                    Label titleLabel = new Label(task.getTitle());
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    Label priorityLabel = new Label(task.getPriority());
                    priorityLabel.setStyle(getPriorityStyle(task.getPriority()));

                    Label dueDateLabel = new Label(task.getDueDate() != null
                            ? task.getDueDate().toString() : "No due date");

                    titleRow.getChildren().addAll(completedCheckBox, titleLabel, priorityLabel, dueDateLabel);

                    if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                        Label descLabel = new Label(task.getDescription());
                        descLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
                        taskContainer.getChildren().addAll(titleRow, descLabel);
                    } else {
                        taskContainer.getChildren().add(titleRow);
                    }

                    // Style completed tasks differently
                    if (task.isCompleted()) {
                        taskContainer.setStyle("-fx-opacity: 0.6;");
                        titleLabel.setStyle(titleLabel.getStyle() + "-fx-strikethrough: true;");
                    }

                    setGraphic(taskContainer);
                }
            }
        });
    }

    private String getPriorityStyle(String priority) {
        switch (priority) {
            case "High":
                return "-fx-background-color: #ff4444; -fx-text-fill: white; -fx-padding: 2 8 2 8; -fx-background-radius: 10;";
            case "Medium":
                return "-fx-background-color: #ffaa00; -fx-text-fill: white; -fx-padding: 2 8 2 8; -fx-background-radius: 10;";
            case "Low":
                return "-fx-background-color: #00aa00; -fx-text-fill: white; -fx-padding: 2 8 2 8; -fx-background-radius: 10;";
            default:
                return "-fx-background-color: gray; -fx-text-fill: white; -fx-padding: 2 8 2 8; -fx-background-radius: 10;";
        }
    }

    @FXML
    private void updateTask(ActionEvent event) {
        if (selectedTask == null) {
            return;
        }

        selectedTask.setTitle(task_title.getText().trim());
        selectedTask.setDescription(task_description.getText().trim());
        selectedTask.setPriority(task_priority.getValue());
        selectedTask.setCategory(task_category.getValue());
        selectedTask.setDueDate(task_due_date.getValue());

        // Update in database
        Database.updateTaskInDB(selectedTask, currentAdminId);

        refreshTaskList();
        clearForm(event);
        exitEditMode();
        alert.SuccessMessage("Task updated successfully!");
    }

    @FXML
    private void clearForm(ActionEvent event) {
        task_title.clear();
        task_description.clear();
        task_priority.setValue("Medium");
        task_category.setValue("Personal");
        task_due_date.setValue(null);
        selectedTask = null;
        exitEditMode();
    }

    @FXML
    private void selectTask(MouseEvent event) {
        Task selected = task_list_view.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedTask = selected;
        }
    }

    @FXML
    private void editSelectedTask(ActionEvent event) {
        if (selectedTask == null) {
            alert.errorMessage("Please select a task to edit!");
            return;
        }

        // Populate form with selected task data
        task_title.setText(selectedTask.getTitle());
        task_description.setText(selectedTask.getDescription());
        task_priority.setValue(selectedTask.getPriority());
        task_category.setValue(selectedTask.getCategory());
        task_due_date.setValue(selectedTask.getDueDate());

        enterEditMode();
    }

    @FXML
    private void deleteSelectedTask(ActionEvent event) {
        if (selectedTask == null) {
            alert.errorMessage("Please select a task to delete!");
            return;
        }

        if (alert.confirmationMessage("Are you sure you want to delete this task?")) {
            // Delete from database
            Database.deleteTaskFromDB(selectedTask.getId(), currentAdminId);
            
            // Remove from local list
            allTasks.remove(selectedTask);
            selectedTask = null;
            refreshTaskList();
            updateStatistics();
            alert.SuccessMessage("Task deleted successfully!");
        }
    }

    @FXML
    private void markTaskComplete(ActionEvent event) {
        if (selectedTask == null) {
            alert.errorMessage("Please select a task to mark as complete!");
            return;
        }
        selectedTask.setCompleted(true);
        
        // Update in database
        Database.updateTaskStatus(selectedTask.getId(), true, currentAdminId);
        
        refreshTaskList();
        updateStatistics();
    }

    @FXML
    private void markTaskPending(ActionEvent event) {
        if (selectedTask == null) {
            alert.errorMessage("Please select a task to mark as pending!");
            return;
        }
        selectedTask.setCompleted(false);
        
        // Update in database
        Database.updateTaskStatus(selectedTask.getId(), false, currentAdminId);
        
        refreshTaskList();
        updateStatistics();
    }

    @FXML
    private void duplicateTask(ActionEvent event) {
        if (selectedTask == null) {
            alert.errorMessage("Please select a task to duplicate!");
            return;
        }

        // Get next available task ID
        int nextId = Database.getNextTaskId(currentAdminId);
        
        Task duplicatedTask = new Task(
                nextId,
                selectedTask.getTitle() + " (Copy)",
                selectedTask.getDescription(),
                selectedTask.getPriority(),
                selectedTask.getCategory(),
                selectedTask.getDueDate(),
                false,
                LocalDate.now()
        );

        // Add to database
        Database.addTaskToDB(duplicatedTask, currentAdminId);
        
        allTasks.add(duplicatedTask);
        refreshTaskList();
        updateStatistics();
        alert.SuccessMessage("Task duplicated successfully!");
    }

    // Sidebar filter methods
    @FXML
    private void showAllTasks() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks);
    }

    @FXML
    private void showPendingTasks() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList()));
    }

    @FXML
    private void showCompletedTasks() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList()));
    }

    @FXML
    private void showOverdueTasks() {
        filteredTasks.clear();
        LocalDate today = LocalDate.now();
        filteredTasks.addAll(allTasks.stream()
                .filter(task -> !task.isCompleted() && task.getDueDate() != null && task.getDueDate().isBefore(today))
                .collect(Collectors.toList()));
    }

    @FXML
    private void showHighPriority() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks.stream()
                .filter(task -> "High".equals(task.getPriority()))
                .collect(Collectors.toList()));
    }

    @FXML
    private void showMediumPriority() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks.stream()
                .filter(task -> "Medium".equals(task.getPriority()))
                .collect(Collectors.toList()));
    }

    @FXML
    private void showLowPriority() {
        filteredTasks.clear();
        filteredTasks.addAll(allTasks.stream()
                .filter(task -> "Low".equals(task.getPriority()))
                .collect(Collectors.toList()));
    }

    @FXML
    private void filterTasks(ActionEvent event) {
        String selectedFilter = filter_combo.getValue();
        switch (selectedFilter) {
            case "All Tasks":
                showAllTasks();
                break;
            case "Pending":
                showPendingTasks();
                break;
            case "Completed":
                showCompletedTasks();
                break;
            case "Overdue":
                showOverdueTasks();
                break;
            case "High Priority":
                showHighPriority();
                break;
            case "Medium Priority":
                showMediumPriority();
                break;
            case "Low Priority":
                showLowPriority();
                break;
        }
    }

    @FXML
    private void searchTasks(KeyEvent event) {
        String searchText = search_field.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            showAllTasks();
        } else {
            filteredTasks.clear();
            filteredTasks.addAll(allTasks.stream()
                    .filter(task -> task.getTitle().toLowerCase().contains(searchText)
                    || (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchText)))
                    .collect(Collectors.toList()));
        }
    }

    // Sort methods
    @FXML
    private void sortByTitle() {
        filteredTasks.sort(Comparator.comparing(Task::getTitle));
    }

    @FXML
    private void sortByPriority() {
        filteredTasks.sort((t1, t2) -> {
            int priority1 = getPriorityValue(t1.getPriority());
            int priority2 = getPriorityValue(t2.getPriority());
            return Integer.compare(priority2, priority1); // High priority first
        });
    }

    @FXML
    private void sortByDate() {
        filteredTasks.sort((t1, t2) -> {
            if (t1.getDueDate() == null && t2.getDueDate() == null) {
                return 0;
            }
            if (t1.getDueDate() == null) {
                return 1;
            }
            if (t2.getDueDate() == null) {
                return -1;
            }
            return t1.getDueDate().compareTo(t2.getDueDate());
        });
    }

    @FXML
    private void sortByCreated() {
        filteredTasks.sort(Comparator.comparing(Task::getCreatedDate).reversed());
    }

    private int getPriorityValue(String priority) {
        switch (priority) {
            case "High":
                return 3;
            case "Medium":
                return 2;
            case "Low":
                return 1;
            default:
                return 0;
        }
    }

    private void toggleTaskCompletion(Task task) {
        task.setCompleted(!task.isCompleted());
        
        // Update in database
        Database.updateTaskStatus(task.getId(), task.isCompleted(), currentAdminId);
        
        refreshTaskList();
        updateStatistics();
    }

    private void refreshTaskList() {
        task_list_view.refresh();
    }

    private void updateStatistics() {
        int total = allTasks.size();
        int completed = (int) allTasks.stream().filter(Task::isCompleted).count();
        int pending = total - completed;

        total_tasks_label.setText("Total Tasks: " + total);
        completed_tasks_label.setText("Completed: " + completed);
        pending_tasks_label.setText("Pending: " + pending);
    }

    private void enterEditMode() {
        isEditMode = true;
        add_task_btn.setVisible(false);
        update_task_btn.setVisible(true);
    }

    private void exitEditMode() {
        isEditMode = false;
        add_task_btn.setVisible(true);
        update_task_btn.setVisible(false);
    }

    @FXML
    private void logoutUser(ActionEvent event) {
        if (alert.confirmationMessage("Are you sure you want to logout?")) {
            try {
                // Load the login form
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage loginStage = new Stage();
                loginStage.setTitle("Organizer Pro - Login");
                loginStage.setScene(new Scene(root));
                loginStage.setMinWidth(340);
                loginStage.setMinHeight(520);
                loginStage.show();

                // Close the current dashboard window
                logout_btn.getScene().getWindow().hide();

            } catch (Exception e) {
                e.printStackTrace();
                alert.errorMessage("Unable to logout. Please try again.");
            }
        }
    }

    // Method to set welcome message
    public void setWelcomeUser(String username) {
        welcome_user.setText("Welcome, " + username + "!");
    }
}