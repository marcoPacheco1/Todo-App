package com.marco.backend.todoapp.backend_todoapp.repositories;


import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoRepository extends JpaRepository<Todo, String>{

    Map<String, Object> getFiltered(Boolean done, String name, PriorityEnum priority, Integer page);

    // Page<Todo> findByDone(Boolean done, Pageable pageable);
    // Page<Todo> findByNameContaining(String name, Pageable pageable);
    // Page<Todo> findByPriority(Integer priority, Pageable pageable);
    // Page<Todo> findByDoneAndNameContaining(Boolean done, String name, Pageable pageable);
    // Page<Todo> findByDoneAndPriority(Boolean done, Integer priority, Pageable pageable);
    // Page<Todo> findByNameContainingAndPriority(String name, Integer priority, Pageable pageable);
    // Page<Todo> findByDoneAndNameContainingAndPriority(Boolean done, String name, Integer priority, Pageable pageable);

}
