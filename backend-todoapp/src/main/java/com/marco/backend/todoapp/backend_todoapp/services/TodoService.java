package com.marco.backend.todoapp.backend_todoapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTodosFiltered(Boolean done, String name, PriorityEnum priority, Integer pageable) {
        return repository.getFiltered(done, name, priority, pageable);
        // if (done != null && name != null && priority != null) {
        //     return (List<Todo>)repository.findByDoneAndNameContainingAndPriority(done, name, priority, pageable);
        // } else if (done != null && name != null) {
        //     return (List<Todo>)repository.findByDoneAndNameContaining(done, name, pageable);
        // } else if (done != null && priority != null) {
        //     return (List<Todo>)repository.findByDoneAndPriority(done, priority, pageable);
        // } else if (name != null && priority != null) {
        //     return (List<Todo>)repository.findByNameContainingAndPriority(name, priority, pageable);
        // } else if (done != null) {
        //     return (List<Todo>)repository.findByDone(done, pageable);
        // } else if (name != null) {
        //     return (List<Todo>)repository.findByNameContaining(name, pageable);
        // } else if (priority != null) {
        //     return (List<Todo>)repository.findByPriority(priority, pageable);
        // } else {
        //     return (List<Todo>)repository.findAll(pageable);
        // }


        // return (List<Todo>)repository.findAll();
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
            // todoFound.setDone(todo.getDone());
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


    // private static final List<Todo> todosSimulados = new ArrayList<>(Arrays.asList(
    //     new Todo(1L, "Comprar pan", "Low", new Date(), false),
    //     new Todo(2L, "Hacer la tarea", "High", new Date(), false),
    //     new Todo(3L, "Llamar al doctor", "Medium", new Date(), false)
    // ));
}
