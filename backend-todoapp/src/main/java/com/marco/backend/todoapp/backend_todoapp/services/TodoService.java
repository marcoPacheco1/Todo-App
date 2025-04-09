package com.marco.backend.todoapp.backend_todoapp.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.repositories.ITodoRepository;

@Service
public class TodoService implements ITodoService{

    @Autowired
    private ITodoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Todo> findAll() {
        return (List<Todo>)repository.findAll();
        // return todosSimulados;
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findById(String id) {
        return (Todo)repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Todo save(Todo todo) {
        // System.out.println("save-service");
        // System.out.println(todo.getPriority());
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
            todoFound.setDone(todo.getDone());
            todoFound.setDueDate(todo.getDueDate()); 
            todoFound.setPriority(todo.getPriority());
            todoFound.setTaskName(todo.getTaskName());
            return this.save(todoFound);
        }
        return null;
    }


    // private static final List<Todo> todosSimulados = new ArrayList<>(Arrays.asList(
    //     new Todo(1L, "Comprar pan", "Low", new Date(), false),
    //     new Todo(2L, "Hacer la tarea", "High", new Date(), false),
    //     new Todo(3L, "Llamar al doctor", "Medium", new Date(), false)
    // ));
}
