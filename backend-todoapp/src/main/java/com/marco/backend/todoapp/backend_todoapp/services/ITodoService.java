package com.marco.backend.todoapp.backend_todoapp.services;

import java.util.List;
import java.util.Optional;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoService {
    List<Todo> findAll();
    
    Todo findById(String id);
    
    Todo save(Todo todo);

    Todo update(String id, Todo todo);

    void remove(String id);
}
