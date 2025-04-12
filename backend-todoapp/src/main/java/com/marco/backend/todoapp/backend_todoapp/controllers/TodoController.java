package com.marco.backend.todoapp.backend_todoapp.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.repositories.ITodoRepository;
import com.marco.backend.todoapp.backend_todoapp.services.ITodoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    // http://localhost:8080/todos?done=false&page=1&name=pan&priority=Low
    @GetMapping("/todos")
    public Map<String, Object> getTodos(
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Boolean done,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) PriorityEnum priority
    ) {
        return this.service.getTodosFiltered(done, name, priority, page);
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
    public ResponseEntity<?> create(@Valid @RequestBody Todo todo) {
        System.out.println("controlador entro");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(todo));
    }

    @PutMapping("todos/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable String id, @RequestBody Todo todo) {
        Todo todoUpdated = service.update(id, todo);
        if (todoUpdated != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(todoUpdated);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("todos/{id}/done")
    public ResponseEntity<?> updateDone(@Valid @PathVariable String id) {
        Todo todoUpdated = service.updateDone(id);
        if (todoUpdated != null)
            return ResponseEntity.status(HttpStatus.OK).body(service.save(todoUpdated));
        return ResponseEntity.notFound().build();
        
    }


    @PutMapping("todos/{id}/undone")
    public ResponseEntity<?> updateUndone(@Valid @PathVariable String id) {
        Todo todoUpdated = service.updateUndone(id);
        if (todoUpdated != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(todoUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("todos/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        Todo todo = service.findById(id);
        if (todo != null)
        {
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
