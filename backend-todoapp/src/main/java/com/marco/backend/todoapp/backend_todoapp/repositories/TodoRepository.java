package com.marco.backend.todoapp.backend_todoapp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Repository;

import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

@Repository
public class TodoRepository implements ITodoRepository {
    
    private static final List<Todo> todosSimulados = new ArrayList<>(Arrays.asList(
        new Todo("1", "Comprar pan", Todo.Priority.Low, new Date(), false),
        new Todo("2", "Hacer la tarea", Todo.Priority.High, new Date(), false),
        new Todo("3", "Llamar al doctor", Todo.Priority.High, new Date(), false)
        ));
        
    @Override
    public Iterable<Todo> findAll() {
        return todosSimulados;
    }

    @Override
    public Optional<Todo> findById(String id) {
        
        return todosSimulados.stream()
            .filter(todo -> todo.getId().equals(id))
            .findFirst();
    }

    @Override
    public <S extends Todo> S save(S entity) {
        if (entity.getId() == null) {
            String nextId = UUID.randomUUID().toString();
            entity.setId(nextId);
            todosSimulados.add(entity);
        } else {
            todosSimulados.removeIf(todo -> todo.getId().equals(entity.getId()));
            todosSimulados.add(entity);
        }
        return entity;
    }

    @Override
    public void deleteById(String id) {

        System.out.println("gola");
        todosSimulados.removeIf(todo -> todo.getId().equals(id));
    }

    @Override
    public void delete(Todo entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }


    @Override
    public <S extends Todo> Iterable<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }
    

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

  

    @Override
    public Iterable<Todo> findAllById(Iterable<String> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    

    
    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
    }

    @Override
    public void deleteAll(Iterable<? extends Todo> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

}
