package com.marco.backend.todoapp.backend_todoapp.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Todo {
    public enum Priority {
        High, Medium, Low
    }

    public Todo(String id, String taskName, Priority priority, Date dueDate, Boolean done) {
        this.id = id;
        this.taskName = taskName;
        this.priority = priority;
        this.dueDate = dueDate;
        this.done = done;
        this.creationDate = LocalDateTime.now(java.time.ZoneId.of("America/Mexico_City"));
    }

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String taskName; 

    @Enumerated(EnumType.STRING)
    private Priority priority;
    private Date dueDate;
    private Boolean done;
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Boolean getDone() {
        return done;
    }
    public void setDone(Boolean done) {
        this.done = done;
        // if (done) {
            // this.dueDate = new Date();
        // } else
        //     this.dueDate = null;
        // }
    }

}
