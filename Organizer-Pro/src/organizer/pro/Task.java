package organizer.pro;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private String priority;
    private String category;
    private LocalDate dueDate;
    private boolean completed;
    private LocalDate createdDate;

    // Constructor
    public Task(int id, String title, String description, String priority, 
                String category, LocalDate dueDate, boolean completed, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.dueDate = dueDate;
        this.completed = completed;
        this.createdDate = createdDate;
    }

    // Default constructor
    public Task() {
        this.createdDate = LocalDate.now();
        this.completed = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    // Helper methods
    public boolean isOverdue() {
        if (dueDate == null || completed) {
            return false;
        }
        return dueDate.isBefore(LocalDate.now());
    }

    public String getStatus() {
        if (completed) {
            return "Completed";
        } else if (isOverdue()) {
            return "Overdue";
        } else {
            return "Pending";
        }
    }

    @Override
    public String toString() {
        return title + " (" + priority + " Priority)";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}