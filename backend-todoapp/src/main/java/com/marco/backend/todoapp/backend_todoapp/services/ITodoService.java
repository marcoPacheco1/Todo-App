package com.marco.backend.todoapp.backend_todoapp.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoService {
    // List<Todo> findAll();
    Map<String, Object> getTodosFiltered(Boolean done,String name, PriorityEnum priority, Integer page, List<String> sortBy, Sort.Direction sortDirection );
    
    Map<String, Object> getMetrics(); 
    Todo findById(String id);
    
    Todo save(Todo todo);

    Todo update(String id, Todo todo);
    
    Todo updateDone(String id); 

    Todo updateUndone(String id);

    void remove(String id);
}
