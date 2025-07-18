package com.marco.backend.todoapp.backend_todoapp.repositories;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoRepository extends JpaRepository<Todo, String>{

    Map<String, Object> getFiltered(Boolean done, String name, PriorityEnum priority, Integer page, List<String> sortBy, Sort.Direction sortDirection );
}
