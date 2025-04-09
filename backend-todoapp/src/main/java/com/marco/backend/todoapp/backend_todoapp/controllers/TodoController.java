package com.marco.backend.todoapp.backend_todoapp.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.repositories.ITodoRepository;
import com.marco.backend.todoapp.backend_todoapp.services.ITodoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    private ITodoService service;

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return this.service.findAll();
        // Todo t = new Todo();
        // t.setId((long)1);
        // t.setTaskName("task");
        // t.setPriority("High");
        // t.setDueDate(new Date());
        // t.setDone(false);
        // return Arrays.asList(t);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable String id) {
        Todo todo = service.findById(id);
        if (todo != null)
        {
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/todos")
    public ResponseEntity<?> create(@RequestBody Todo todo) {
        System.out.println("controlador entro");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(todo));
    }

    @PutMapping("todos/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Todo todo) {
        Todo todoUpdated = service.update(id, todo);
        if (todoUpdated != null)
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(todoUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("todos/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Todo todo = service.findById(id);
        if (todo != null)
        {
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}
