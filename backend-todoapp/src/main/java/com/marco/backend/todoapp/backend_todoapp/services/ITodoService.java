package com.marco.backend.todoapp.backend_todoapp.services;

import java.util.List;
import java.util.Optional;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoService {
    // List<Todo> findAll();
    List<Todo> getTodosFiltered(Boolean done,String name, PriorityEnum priority, Integer page);
    
    Todo findById(String id);
    
    Todo save(Todo todo);

    Todo update(String id, Todo todo);
    
    Todo updateDone(String id); 

    Todo updateUndone(String id);

    void remove(String id);
}
