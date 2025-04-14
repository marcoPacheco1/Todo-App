package com.marco.backend.todoapp.backend_todoapp.models.entities;


import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Todo {

    public Todo(String id, String taskName, PriorityEnum priority, LocalDateTime dueDate, Boolean done) {
        this.id = id;
        this.taskName = taskName;
        this.priority = priority;
        this.dueDate = dueDate;
        this.done = done;
        this.creationDate = LocalDateTime.now(java.time.ZoneId.of("America/Mexico_City"));
        this.doneDate = LocalDateTime.now(java.time.ZoneId.of("America/Mexico_City")).plusHours(12);
    }

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "Todo name is mandatory")
    private String taskName; 


    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority should be one of there: [High, Medium, Low] ")
    private PriorityEnum priority;

    private LocalDateTime dueDate;
    
    private Boolean done;

    @Column(nullable = true)
    private LocalDateTime doneDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
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
    public PriorityEnum getPriority() {
        return priority;
    }
    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    public Boolean getDone() {
        return done;
    }
    public void setDone(Boolean done) {
        this.done = done;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }
    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

}
