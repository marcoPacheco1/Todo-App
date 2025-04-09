package com.marco.backend.todoapp.backend_todoapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

public interface ITodoRepository extends CrudRepository<Todo, String>{

}
