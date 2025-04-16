package com.marco.backend.todoapp.backend_todoapp.repositories;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;

@Repository
public class TodoRepository implements ITodoRepository{
    private static final int pageElements = 10;

    public static List<Todo> todosSimulados = new ArrayList<>(Arrays.asList(
        new Todo("1", "Buy bread", PriorityEnum.High, 
            LocalDateTime.of(2025, 5, 18,0,0), true),
        new Todo("2", "Pay bills", PriorityEnum.High, 
            LocalDateTime.of(2025, 6, 1,0,0), false),
        new Todo("3", "Study react", PriorityEnum.Low, 
            LocalDateTime.of(2025, 3, 1,0,0), false)
    ));
        
    @Override
    public List<Todo> findAll() {
        return todosSimulados;
    }

    @Override
    public Optional<Todo> findById(String id) {
        Optional<Todo> optionalTodo = todosSimulados.stream()
            .filter(todo -> todo.getId().equals(id))
            .findFirst();
        return optionalTodo; 
    }

    @Override
    public <S extends Todo> S save(S entity) {
        if (entity.getId() == null) {
            String nextId = UUID.randomUUID().toString();
            entity.setId(nextId);
            todosSimulados.add(0,entity);
        } else {
            todosSimulados.removeIf(todo -> todo.getId().equals(entity.getId()));
            todosSimulados.add(0,entity);
        }
        return entity;
    }

    @Override
    public void deleteById(String id) {
        todosSimulados.removeIf(todo -> todo.getId().equals(id));
    }

    @Override
    public Map<String, Object> getFiltered(Boolean done, String name, PriorityEnum priority, Integer page, List<String> sortBy, Sort.Direction sortDirection) {
        Comparator<Todo> comparator = null;
        
        if (sortBy != null && !sortBy.isEmpty()) {
            for (int i = 0; i < sortBy.size(); i++) {
                String sort = sortBy.get(i);
                Comparator<Todo> currentComparator = null;
    
                if (sort.equalsIgnoreCase("priority")) {
                    currentComparator = Comparator.comparing(Todo::getPriority);
                } else if (sort.equalsIgnoreCase("dueDate")) {
                    currentComparator = Comparator.comparing(Todo::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
                }
    
                if (currentComparator != null) {
                    if (comparator == null) {
                        comparator = currentComparator;
                    } else {
                        comparator = comparator.thenComparing(currentComparator);
                    }
                }
            }
    
            if (comparator != null && sortDirection == Sort.Direction.DESC) {
                comparator = comparator.reversed();
            }
        }
        
        List<Todo> fileredTodo = todosSimulados.stream()
            .filter( todo -> done == null || todo.getDone().equals(done))
            .filter( todo -> name == null || todo.getTaskName().toLowerCase().contains(name.toLowerCase()))
            .filter( todo -> priority == null || todo.getPriority() ==  priority)
            .collect(Collectors.toList());
        
        if (comparator != null) {
            fileredTodo.sort(comparator);
        }

        int totalPages = (int) Math.ceil((double) fileredTodo.size() / pageElements) ;
        if (page == null)
            page = 0;
        else if (page > totalPages)
            page = totalPages - 1;

        int start = (page ) * pageElements;
        int end = Math.min((start + pageElements), fileredTodo.size());

        List<Todo> fileredTodoList;

        if (start > fileredTodo.size()) {
            fileredTodoList = Collections.emptyList(); // Return an empty list if page number is out of range
        }
        fileredTodoList = fileredTodo.subList(start, end);
        
        Map<String, Object> response = new HashMap<>();
        response.put("fileredTodo", fileredTodoList) ;
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalItems", fileredTodo.size());

        return response;
    }




    // @Override
    // public Page<Todo> findByDone(Boolean done, Pageable pageable) {
    //     List<Todo> filtered = todosSimulados.stream()
    //             .filter(todo -> todo.getDone() == done)
    //             .collect(Collectors.toList());
    //     return getPage(filtered, pageable);
    // }

    // @Override
    // public Page<Todo> findByNameContaining(String name, Pageable pageable) {
    //     List<Todo> filtered = todosSimulados.stream()
    //             .filter(todo -> todo.getName().contains(name))
    //             .collect(Collectors.toList());
    //     return getPage(filtered, pageable);
    // }

    // @Override
    // public Page<Todo> findByPriority(Integer priority, Pageable pageable) {
    //     List<Todo> filtered = todosSimulados.stream()
    //             .filter(todo -> todo.getPriority().ordinal() == priority)
    //             .collect(Collectors.toList());
    //     return getPage(filtered, pageable);
    // }

    // @Override
    // public Page<Todo> findByDoneAndNameContaining(Boolean done, String name, Pageable pageable) {
    //     List<Todo> filtered = todosSimulados.stream()
    //             .filter(todo -> todo.getDone() == done && todo.getName().contains(name))
    //             .collect(Collectors.toList());
    //     return getPage(filtered, pageable);
    // }

    // @Override
    // public Page<Todo> findByDoneAndPriority(Boolean done, Integer priority, Pageable pageable) {
    //     List<Todo> filtered = todosSimulados.stream()
    //             .filter(todo -> todo.getName().contains(name) && todo.getPriority().ordinal() == priority)
    //             .collect(Collectors.toList());
    //     return getPage(filtered, pageable);
    // }

    // @Override
    // public Page<Todo> findByNameContainingAndPriority(String name, Integer priority, Pageable pageable) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findByNameContainingAndPriority'");
    // }






    @Override
    public Todo getReferenceById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    // @Override
    // public Optional<Todo> findById(String id) {
        
    //     return todosSimulados.stream()
    //         .filter(todo -> todo.getId().equals(id))
    //         .findFirst();
    // }


    @Override
    public void flush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flush'");
    }

    @Override
    public <S extends Todo> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
    }

    @Override
    public <S extends Todo> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
    }

    @Override
    public void deleteAllInBatch(Iterable<Todo> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
    }

    @Override
    public Todo getOne(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOne'");
    }

    @Override
    public Todo getById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    
    @Override
    public <S extends Todo> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Todo> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public List<Todo> findAllById(Iterable<String> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public boolean existsById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void delete(Todo entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
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

    @Override
    public <S extends Todo> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOne'");
    }

    @Override
    public <S extends Todo> long count(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public <S extends Todo> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public <S extends Todo, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBy'");
    }

    @Override
    public <S extends Todo> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public List<Todo> findAll(Sort sort) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Page<Todo> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public <S extends Todo> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
