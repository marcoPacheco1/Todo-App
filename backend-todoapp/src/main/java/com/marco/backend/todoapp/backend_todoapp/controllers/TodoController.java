package com.marco.backend.todoapp.backend_todoapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.models.entities.TodosSearchRequest;
import com.marco.backend.todoapp.backend_todoapp.services.ITodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class TodoController {
    @Autowired
    private ITodoService service;

    @GetMapping("/todos")
    public ResponseEntity<?> getTodos(
        @Valid @ModelAttribute TodosSearchRequest request, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(this.service.getTodosFiltered(request.getDone(), request.getName(), request.getPriority(), request.getPage(), request.getSortBy(), request.getSortDirection()));
    }


    @GetMapping("/todos/metrics")
    public Map<String, Object> getMetrics() {
        return this.service.getMetrics();
    }

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
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