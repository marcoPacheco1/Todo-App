package com.marco.backend.todoapp.backend_todoapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.services.TodoService;
import org.springframework.data.domain.Sort;


@SpringBootTest
public class TodoRepositoryTest {

    private List<Todo> expectedTodos;
    private ITodoRepository repository;
    private static final int pageElements = 3;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new TodoRepository();
        expectedTodos = Arrays.asList(
            new Todo("1", "Buy bread", PriorityEnum.High, LocalDateTime.of(2025, 5, 18, 0, 0), true),
            new Todo("2", "Pay bills", PriorityEnum.High, LocalDateTime.of(2025, 6, 1, 0, 0), false),
            new Todo("3", "Study react", PriorityEnum.Low, LocalDateTime.of(2025, 3, 1, 0, 0), false)
        );
        TodoRepository.todosSimulados.clear(); 

        TodoRepository.todosSimulados = new ArrayList<>(Arrays.asList(
        new Todo("1", "Buy bread", PriorityEnum.High, 
            LocalDateTime.of(2025, 5, 18,0,0), true),
        new Todo("2", "Pay bills", PriorityEnum.High, 
            LocalDateTime.of(2025, 6, 1,0,0), false),
        new Todo("3", "Study react", PriorityEnum.Low, 
            LocalDateTime.of(2025, 3, 1,0,0), false)
        ));
    }


    @Test
    public void should_return_all_todos() {
        // Act
        List<Todo> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        Todo firstTodo = result.get(0);
        assertEquals("1", firstTodo.getId());
        assertEquals("Buy bread", firstTodo.getTaskName());
        assertEquals(PriorityEnum.High, firstTodo.getPriority());
        assertEquals(LocalDateTime.of(2025, 5, 18, 0, 0), firstTodo.getDueDate());
        assertTrue(firstTodo.getDone());
    }


    @Test
    public void test_findById_returns_todo_when_id_exists() {
        // Arrange
        String existingId = "2";

        // Act
        Optional<Todo> result = repository.findById(existingId);

        // Assert
        assertTrue(result.isPresent());
        Todo todo = result.get();
        assertEquals("2", todo.getId());
        assertEquals("Pay bills", todo.getTaskName());
        assertEquals(PriorityEnum.High, todo.getPriority());
        assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0), todo.getDueDate());
        assertFalse(todo.getDone());
    }

    @Test
    public void test_findById_returns_empty_optional_when_id_does_not_exist() {
        // Arrange
        String nonExistingId = "124";
        // Act
        Optional<Todo> result = repository.findById(nonExistingId);
        // Assert
        assertFalse(result.isPresent());
    }


    @Test
    public void test_save_creates_new_todo_when_id_is_null() {
        // Arrange
        Todo newTodo = new Todo(null, "New Task", PriorityEnum.Low, LocalDateTime.now(), false);

        // Act
        Todo savedTodo = repository.save(newTodo);
        List<Todo> todos = repository.findAll();

        // Assert
        assertNotNull(savedTodo.getId());
        assertEquals(newTodo, savedTodo);
        assertTrue(todos.contains(savedTodo));
        assertEquals(newTodo, todos.get(0)); // Verifica que se agrega al principio
    }

    @Test
    public void test_save_updates_existing_todo_when_id_is_not_null() {
        // Arrange
        Todo existingTodo = repository.findAll().get(0);
        Todo updatedTodo = new Todo(existingTodo.getId(), "Updated Task", PriorityEnum.High, LocalDateTime.now().plusDays(1), true);

        // Act
        Todo savedTodo = repository.save(updatedTodo);
        List<Todo> todos = repository.findAll();

        // Assert
        assertEquals(updatedTodo, savedTodo);
        assertTrue(todos.contains(updatedTodo));
        assertEquals(updatedTodo, todos.get(0)); // Verifica que se actualiza al principio
        assertFalse(todos.contains(existingTodo)); // Verifica que el antiguo Todo no está
    }

    @Test
    public void test_deleteById_removes_todo_when_id_exists() {
        // Arrange
        String existingId = "2";

        // Act
        repository.deleteById(existingId);
        List<Todo> todos = repository.findAll();

        // Assert
        assertEquals(2, todos.size()); 
        assertFalse(todos.stream().anyMatch(todo -> todo.getId().equals(existingId)));
    }

    @Test
    public void test_deleteById_does_nothing_when_id_does_not_exist() {
        // Arrange
        String nonExistingId = "4";
        List<Todo> initialTodos = repository.findAll();

        // Act
        repository.deleteById(nonExistingId);
        List<Todo> finalTodos = repository.findAll();

        // Assert
        assertEquals(initialTodos.size(), finalTodos.size()); 
        assertEquals(initialTodos, finalTodos);
    }

    @Test
    public void test_getFiltered_filters_and_sorts_and_paginates() {
        // Arrange
        Boolean done = false;
        String name = "study";
        PriorityEnum priority = PriorityEnum.Low;
        Integer page = 0;
        List<String> sortBy = Arrays.asList("dueDate");
        Sort.Direction sortDirection = Sort.Direction.ASC;

        // Act
        Map<String, Object> result = repository.getFiltered(done, name, priority, page, sortBy, sortDirection);
        List<Todo> filteredTodos = (List<Todo>) result.get("fileredTodo");

        // Assert
        assertEquals(1, filteredTodos.size());
        assertEquals("Study react", filteredTodos.get(0).getTaskName());
        assertEquals(0, result.get("currentPage"));
        assertEquals(1, result.get("totalPages"));
        assertEquals(1, result.get("totalItems"));
    }

    @Test
    public void test_getFiltered_filters_and_sorts_descending() {
        // Arrange
        List<String> sortBy = Arrays.asList("dueDate");
        Sort.Direction sortDirection = Sort.Direction.DESC;

        // Act
        Map<String, Object> result = repository.getFiltered(null, null, null, 0, sortBy, sortDirection);
        List<Todo> filteredTodos = (List<Todo>) result.get("fileredTodo");
        
        // Assert
        assertEquals(3, filteredTodos.size());
        assertEquals("Pay bills", filteredTodos.get(0).getTaskName());
        assertEquals("Buy bread", filteredTodos.get(1).getTaskName());
        assertEquals("Study react", filteredTodos.get(2).getTaskName());
    }

    @Test
    public void test_getFiltered_filters_by_name_and_priority() {
        // Arrange
        String name = "bread";
        PriorityEnum priority = PriorityEnum.High;

        // Act
        Map<String, Object> result = repository.getFiltered(null, name, priority, 0, null, null);
        List<Todo> filteredTodos = (List<Todo>) result.get("fileredTodo");

        // Assert
        assertEquals(1, filteredTodos.size());
        assertEquals("Buy bread", filteredTodos.get(0).getTaskName());
    }

    @Test
    public void test_getFiltered_last_valid_page_out_of_range() {
        // Arrange
        int totalItems = expectedTodos.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageElements);
        int outOfRangePage = totalPages + 1;
        int lastValidPage = totalPages - 1;

        // Act
        Map<String, Object> result = repository.getFiltered(null, null, null, outOfRangePage, null, null);
        List<Todo> filteredTodos = (List<Todo>) result.get("fileredTodo");
        int returnedPage = (int) result.get("currentPage");

        // Assert
        assertEquals(lastValidPage, returnedPage);
        assertFalse(filteredTodos.isEmpty());
    }

    @Test
    public void test_getFiltered_null_params() {

        // Act
        Map<String, Object> result = repository.getFiltered(null, null, null, null, null, null);
        
        // Assert
        assertEquals(3, result.get("totalItems"));
    }

    @Test
    public void test_getFiltered_multiple_sort_fields() {
        // Arrange
        List<String> sortBy = Arrays.asList("priority", "dueDate");
        Sort.Direction sortDirection = Sort.Direction.ASC;

        // Act
        Map<String, Object> result = repository
            .getFiltered(null, null, null, 0, sortBy, sortDirection);
        List<Todo> filteredTodos = (List<Todo>) result.get("fileredTodo");

        // Assert
        assertEquals(3, filteredTodos.size());
        assertEquals("Study react", filteredTodos.get(0).getTaskName());
        assertEquals("Buy bread", filteredTodos.get(1).getTaskName());
        assertEquals("Pay bills", filteredTodos.get(2).getTaskName());
    }
}
