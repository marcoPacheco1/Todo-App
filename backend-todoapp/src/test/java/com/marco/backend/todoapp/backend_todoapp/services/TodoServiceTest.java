package com.marco.backend.todoapp.backend_todoapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.repositories.ITodoRepository;

@SpringBootTest
public class TodoServiceTest {

    private Todo expectedTodo;
    
    @Mock
    private ITodoRepository repository;
    
    @InjectMocks
    private TodoService service;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        expectedTodo = new Todo(
            "123", "Comprar pan", PriorityEnum.High, 
            LocalDateTime.of(2025, 5, 18,0,0), true
        );
    }

    @Test
    public void should_Return_Todo_findById() {
        // Arrange
        String id = "123";
        expectedTodo.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(expectedTodo));

        // Act
        Todo actualTodo = service.findById(id);

        // Assert
        assertEquals(expectedTodo, actualTodo);
    }

    @Test
    public void should_returnNull_id_notFound_findById() {
        // Arrange
        String id = "234";
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        Todo actualTodo = service.findById(id);

        // Assert
        assertNull(actualTodo);
    }

    @Test
    public void should_save_todo() {
        // Arrange
        Todo todo = new Todo("1", "Task 1", PriorityEnum.High, LocalDateTime.now(), false);
        when(repository.save(todo)).thenReturn(todo); // Configura el comportamiento del mock

        // Act
        Todo savedTodo = service.save(todo);

        // Assert
        assertEquals(todo, savedTodo);
        verify(repository, times(1)).save(todo);
    }

    @Test
    public void should_remove_todo_by_id() {
        // Arrange
        String todoId = "123";
        // Act
        service.remove(todoId);
        // Assert
        verify(repository, times(1)).deleteById(todoId); 
    }

    @Test
    public void should_update_todo_when_todo_exists() {
        // Arrange
        String todoId = "123";
        Todo existingTodo = new Todo(todoId, "Old Task", PriorityEnum.Low, LocalDateTime.now(), false);
        Todo updatedTodo = new Todo(todoId, "New Task", PriorityEnum.High, LocalDateTime.now().plusDays(1), true);

        Mockito.when(repository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        Mockito.when(repository.save(any(Todo.class))).thenReturn(updatedTodo);

        // Act
        Todo result = service.update(todoId, updatedTodo);

        // Assert
        assertEquals(updatedTodo, result);
        verify(repository, times(1)).findById(todoId);
        verify(repository, times(1)).save(any(Todo.class));
    }

    @Test
    public void should_return_null_when_todo_does_not_exist() {
        // Arrange
        String todoId = "123";
        Todo updatedTodo = new Todo("123", "New Task", PriorityEnum.High, LocalDateTime.now().plusDays(1), true);

        Mockito.when(repository.findById(todoId)).thenReturn(Optional.empty());

        // Act
        Todo result = service.update(todoId, updatedTodo);

        // Assert
        assertNull(result);
        verify(repository, times(1)).findById(todoId);
        verify(repository, never()).save(any(Todo.class));
    }


    @Test
    public void should_update_todo_done_when_todo_exists_and_is_not_done() {
        // Arrange
        String todoId = "123";
        Todo existingTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), false);
        Todo updatedTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), true);

        when(repository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(repository.save(any(Todo.class))).thenReturn(updatedTodo);

        // Act
        Todo result = service.updateDone(todoId);

        // Assert
        assertEquals(updatedTodo, result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.times(1)).save(any(Todo.class));
    }

    @Test
    public void should_return_same_todo_when_todo_exists_and_is_already_done() {
        // Arrange
        String todoId = "123";
        Todo existingTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), true);

        when(repository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(repository.save(any(Todo.class))).thenReturn(existingTodo);

        // Act
        Todo result = service.updateDone(todoId);

        // Assert
        assertEquals(existingTodo, result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.times(1)).save(any(Todo.class));
    }

    @Test
    public void should_return_null_when_todo_does_not_exist_updateDone() {
        // Arrange
        String todoId = "123";

        when(repository.findById(todoId)).thenReturn(Optional.empty());

        // Act
        Todo result = service.updateDone(todoId);

        // Assert
        assertNull(result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.never()).save(any(Todo.class));
    }

    @Test
    public void should_update_todo_undone_when_todo_exists_and_is_done() {
        // Arrange
        String todoId = "123";
        Todo existingTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), true);
        Todo updatedTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), false);

        when(repository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(repository.save(any(Todo.class))).thenReturn(updatedTodo);

        // Act
        Todo result = service.updateUndone(todoId);

        // Assert
        assertEquals(updatedTodo, result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.times(1)).save(any(Todo.class));
    }

    @Test
    public void should_return_same_todo_when_todo_exists_and_is_not_done() {
        // Arrange
        String todoId = "123";
        Todo existingTodo = new Todo(todoId, "Task", PriorityEnum.Low, LocalDateTime.now(), false);

        when(repository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(repository.save(any(Todo.class))).thenReturn(existingTodo);

        // Act
        Todo result = service.updateUndone(todoId);

        // Assert
        assertEquals(existingTodo, result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.times(1)).save(any(Todo.class));
    }

    @Test
    public void should_return_null_when_todo_does_not_exist_updateUndone() {
        // Arrange
        String todoId = "123";

        when(repository.findById(todoId)).thenReturn(Optional.empty());

        // Act
        Todo result = service.updateUndone(todoId);

        // Assert
        assertNull(result);
        verify(repository, Mockito.times(1)).findById(todoId);
        verify(repository, Mockito.never()).save(any(Todo.class));
    }

    @Test
    public void should_return_metrics_with_completed_and_incomplete_todos() {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2025, 5, 1,0,0);
        List<Todo> todos = Arrays.asList(
            new Todo("1", "Task 1", PriorityEnum.High, now.minusDays(3), true),
            new Todo("2", "Task 2", PriorityEnum.Low, now.plusDays(3), true),
            new Todo("3", "Task 3", PriorityEnum.Medium, now.plusDays(2), true),
            new Todo("4", "Task 4", PriorityEnum.High, now, false)
        );
        LocalDateTime doneDate = LocalDateTime.of(2025, 5, 2,0,0);
        todos.get(0).setCreationDate(now);
        todos.get(1).setCreationDate(now);
        todos.get(2).setCreationDate(now);

        todos.get(0).setDoneDate(doneDate);
        todos.get(1).setDoneDate(doneDate);
        todos.get(2).setDoneDate(doneDate);

        when(repository.findAll()).thenReturn(todos);
        // Act
        Map<String, Object> metrics = service.getMetrics();
        
        // Assert
        assertEquals("1 days, 0 hours, 0 minutes, 0 seconds", metrics.get("averageEstimatedTimeToComplete"));
        Map<PriorityEnum, String> averageTimeByPriority = (Map<PriorityEnum, String>) metrics.get("averageTimeToFinishByPriority");
        assertEquals("1 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Low));
        assertEquals("1 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Medium));
        assertEquals("1 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.High));
    }

    @Test
    public void should_return_metrics_with_no_completed_todos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        List<Todo> todos = Arrays.asList(
            new Todo("2", "Task 2", PriorityEnum.Low, now.minusDays(2), false),
            new Todo("3", "Task 3", PriorityEnum.Medium, now.minusDays(1), false),
            new Todo("4", "Task 4", PriorityEnum.High, now, false)
        );
        todos.get(0).setDueDate(now.plusDays(1));
        todos.get(1).setDueDate(now.plusDays(2));
        todos.get(2).setDueDate(now.plusDays(3));

        when(repository.findAll()).thenReturn(todos);

        // Act
        Map<String, Object> metrics = service.getMetrics();

        // Assert
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", metrics.get("averageEstimatedTimeToComplete"));
        Map<PriorityEnum, String> averageTimeByPriority = (Map<PriorityEnum, String>) metrics.get("averageTimeToFinishByPriority");
        assertEquals("1 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Low));
        assertEquals("2 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Medium));
        assertEquals("3 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.High));
    }

    @Test
    public void should_return_metrics_with_no_incomplete_todos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        List<Todo> todos = Arrays.asList(
            new Todo("1", "Task 1", PriorityEnum.High, now.minusDays(3), true)
        );
        todos.get(0).setDoneDate(now);

        when(repository.findAll()).thenReturn(todos);

        // Act
        Map<String, Object> metrics = service.getMetrics();

        // Assert
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", metrics.get("averageEstimatedTimeToComplete"));
        Map<PriorityEnum, String> averageTimeByPriority = (Map<PriorityEnum, String>) metrics.get("averageTimeToFinishByPriority");
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.High));
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Low));
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Medium));
    }

    @Test
    public void should_return_metrics_with_no_todos() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        Map<String, Object> metrics = service.getMetrics();

        // Assert
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", metrics.get("averageEstimatedTimeToComplete"));
        Map<PriorityEnum, String> averageTimeByPriority = (Map<PriorityEnum, String>) metrics.get("averageTimeToFinishByPriority");
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.High));
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Low));
        assertEquals("0 days, 0 hours, 0 minutes, 0 seconds", averageTimeByPriority.get(PriorityEnum.Medium));
    }


    @Test
    public void should_call_repository_with_all_parameters() {
        // Arrange
        Boolean done = true;
        String name = "Task 1";
        PriorityEnum priority = PriorityEnum.High;
        Integer pageable = 0;
        List<String> sortBy = Arrays.asList("taskName", "dueDate");
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("result", Arrays.asList("todo1", "todo2"));

        when(repository.getFiltered(done, name, priority, pageable, sortBy, sortDirection)).thenReturn(expectedResult);

        // Act
        Map<String, Object> result = service.getTodosFiltered(done, name, priority, pageable, sortBy, sortDirection);

        // Assert
        assertEquals(expectedResult, result);
        verify(repository, times(1)).getFiltered(done, name, priority, pageable, sortBy, sortDirection);
    }

    @Test
    public void should_call_repository_with_some_parameters_null() {
        // Arrange
        Boolean done = null;
        String name = null;
        PriorityEnum priority = null;
        Integer pageable = 1;
        List<String> sortBy = Arrays.asList("priority");
        Sort.Direction sortDirection = Sort.Direction.DESC;

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("result", Arrays.asList("todo3", "todo4"));

        when(repository.getFiltered(done, name, priority, pageable, sortBy, sortDirection)).thenReturn(expectedResult);

        // Act
        Map<String, Object> result = service.getTodosFiltered(done, name, priority, pageable, sortBy, sortDirection);

        // Assert
        assertEquals(expectedResult, result);
        verify(repository, times(1)).getFiltered(done, name, priority, pageable, sortBy, sortDirection);
    }

    @Test
    public void should_call_repository_with_empty_sort_list() {
        // Arrange
        Boolean done = false;
        String name = "Task";
        PriorityEnum priority = PriorityEnum.Medium;
        Integer pageable = 0;
        List<String> sortBy = Arrays.asList();
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("result", Arrays.asList("todo5"));

        when(repository.getFiltered(done, name, priority, pageable, sortBy, sortDirection)).thenReturn(expectedResult);

        // Act
        Map<String, Object> result = service.getTodosFiltered(done, name, priority, pageable, sortBy, sortDirection);

        // Assert
        assertEquals(expectedResult, result);
        verify(repository, times(1)).getFiltered(done, name, priority, pageable, sortBy, sortDirection);
    }

    @Test
    public void should_call_repository_with_null_sort_list() {
        // Arrange
        Boolean done = false;
        String name = "Task";
        PriorityEnum priority = PriorityEnum.Medium;
        Integer pageable = 0;
        List<String> sortBy = null;
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("result", Arrays.asList("todo5"));

        when(repository.getFiltered(done, name, priority, pageable, sortBy, sortDirection)).thenReturn(expectedResult);

        // Act
        Map<String, Object> result = service.getTodosFiltered(done, name, priority, pageable, sortBy, sortDirection);

        // Assert
        assertEquals(expectedResult, result);
        verify(repository, times(1)).getFiltered(done, name, priority, pageable, sortBy, sortDirection);
    }
}
