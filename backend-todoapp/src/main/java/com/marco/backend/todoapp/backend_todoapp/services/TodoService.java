package com.marco.backend.todoapp.backend_todoapp.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.repositories.ITodoRepository;

@Service
public class TodoService implements ITodoService{

    @Autowired
    private ITodoRepository repository;

    public String getFormatDate(Duration duration){
    
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60; 
        long seconds = duration.getSeconds() % 60;
        
        String duracionFormateada = String.format(
            "%d days, %d hours, %d minutes, %d seconds",
            days, hours, minutes, seconds
        );

        return duracionFormateada;
    } 

    @Transactional(readOnly = true)
    public Map<String, Object> getMetrics() {
        List<Todo> todos = repository.findAll();

        List<Todo> doneTodo = todos.stream()
        .filter( todo -> todo.getDone().equals(true))
        .collect(Collectors.toList());

        Duration duration = Duration.ZERO;
        Duration avgDoneDurationGeneral = Duration.ZERO;
        Duration avgDoneDuration = Duration.ZERO;
        for (Todo done : doneTodo) {
            duration =duration.plus( Duration.between(done.getCreationDate(), done.getDoneDate()));    
        }
        if (doneTodo.size() > 0)
            avgDoneDurationGeneral= duration.dividedBy(doneTodo.size());

        Map<PriorityEnum, String> averageTimeByPriority = new HashMap<>();

        for (PriorityEnum priority : PriorityEnum.values()) {
            List<Todo> doneTodosByPriority = doneTodo.stream()
                    .filter(todo -> todo.getPriority() == priority)
                    .collect(Collectors.toList());

            Duration totalDuration = Duration.ZERO;
            for (Todo todo : doneTodosByPriority) {
                if (todo.getDoneDate() != null) {
                    totalDuration = totalDuration.plus(Duration.between(todo.getCreationDate(), todo.getDoneDate()));
                }
            }

            Duration averageDuration = Duration.ZERO;
            if (! doneTodosByPriority.isEmpty())
                averageDuration = totalDuration.dividedBy(doneTodosByPriority.size());
            averageTimeByPriority.put(priority, getFormatDate(averageDuration));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("averageEstimatedTimeToComplete", getFormatDate(avgDoneDurationGeneral)) ; 
        response.put("averageTimeToFinishByPriority", averageTimeByPriority);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTodosFiltered(Boolean done, String name, PriorityEnum priority, Integer pageable, 
        List<String> sortBy, Sort.Direction sortDirection ) {

        return repository.getFiltered(done, name, priority, pageable, sortBy, sortDirection);
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findById(String id) {
        return (Todo)repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    @Override
    @Transactional
    public void remove(String id) {
        repository.deleteById(id);
    }

    @Override
    public Todo update(String id, Todo todo) {

        Todo todoFound = this.findById(id);
        if (todoFound != null){
            todoFound.setDueDate(todo.getDueDate()); 
            todoFound.setPriority(todo.getPriority());
            todoFound.setTaskName(todo.getTaskName());
            return this.save(todoFound);
        }
        return null;
    }


    @Override
    public Todo updateDone(String id) {
        Todo todoFound = this.findById(id);
        if (todoFound != null){
            if (! todoFound.getDone()){
                todoFound.setDone(true);
                todoFound.setDoneDate(LocalDateTime.now());
            }

            return this.save(todoFound);
        }
        return null;
    }


    @Override
    public Todo updateUndone(String id) {
        Todo todoFound = this.findById(id);
        if (todoFound != null){
            if (todoFound.getDone()){
                todoFound.setDone(false);
                todoFound.setDoneDate(null);
            }

            return this.save(todoFound);
        }
        return null;
    }
}
