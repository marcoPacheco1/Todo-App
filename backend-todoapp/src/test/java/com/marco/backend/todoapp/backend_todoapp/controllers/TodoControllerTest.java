package com.marco.backend.todoapp.backend_todoapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.services.ITodoService;
import org.springframework.http.MediaType;
import java.util.stream.Stream;

import jakarta.persistence.PersistenceContext;


@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    private static MockHttpServletRequest request;

    // @PersistenceContext


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ITodoService service;

    @BeforeEach
    public void setUp() {
        // http://localhost:8080/todos?sortBy=priority&sortDirection=DESC&page=0&size=2
        // MockitoAnnotations.openMocks(this);
    }


    @Test
    public void should_return_filtered_todos_with_sorting_and_pagination() throws Exception {
        // Arrange
        List<Todo> filteredTodos = Arrays.asList(
            new Todo("2", "Learn vue", PriorityEnum.High,
                    LocalDateTime.of(2025, 6, 1, 0, 0), false),
            new Todo("3", "Learn react", PriorityEnum.Low,
            LocalDateTime.of(2025, 3, 1, 0, 0), false)
        );

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        response.put("filteredTodo", filteredTodos);
        response.put("totalPages", 1);
        response.put("currentPage", 0);

        Mockito.when(service.getTodosFiltered(
            eq(false),
            isNull(),
            isNull(),                 
            eq(0),
            eq(Collections.singletonList("priority")),
            eq(Sort.Direction.DESC)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/todos")
                .param("done", "false")
                .param("page", "0")
                .param("sortBy", "priority")
                .param("sortDirection", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalItems").value(2))
            .andExpect(jsonPath("$.filteredTodo[0].taskName").value("Learn vue"))
            .andExpect(jsonPath("$.filteredTodo[1].priority").exists())
            .andExpect(jsonPath("$.totalPages").exists())
            .andExpect(jsonPath("$.currentPage").value(0));
    }
    
    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana", "orange");
    }


    @Test
    public void should_return_todo_by_id_when_todo_exists() throws Exception {
        // Arrange
        String id = "1";
        Todo todo = new Todo(
            id, "Comprar pan", PriorityEnum.High,
            LocalDateTime.of(2025, 5, 18, 0, 0), true
        );

        Mockito.when(service.findById(eq(id))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.taskName").value("Comprar pan"));
    }

    @Test
    public void should_return_not_found_when_todo_does_not_exist() throws Exception {
        // Arrange
        String id = "212";
        Mockito.when(service.findById(eq(id))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    public void should_create_todo_when_todo_is_valid() throws Exception {
        // Arrange
        Todo todo = new Todo(
            "a41408e7-e4ad-4fb6-80bc-c4c0b4cb34f2", "Learn react", PriorityEnum.Low,
            LocalDateTime.of(2025, 10, 1, 0, 0), false
        );

        Mockito.when(service.save(any(Todo.class))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(todo.getId()))
            .andExpect(jsonPath("$.taskName").value(todo.getTaskName()));
    }

    
    @Test
    public void should_return_bad_request_when_invalid_input() throws Exception {
        // Arrange
        String invalidTodoJson = "{\"done\": false, \"priority\": \"Low\", \"dueDate\": \"2025-10-01T00:00:00.000Z\"}";

        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTodoJson))
            .andExpect(status().isBadRequest())
            // .andExpect(jsonPath("$.errors[0].field").value("taskName"))
            .andReturn();
    }


    @Test
    public void should_update_todo_when_valid_input_Put() throws Exception {
        // Arrange
        String id = "2";
        Todo todo = new Todo(
            id, "Hacer la tareaEditado", PriorityEnum.High,
            LocalDateTime.of(2025, 6, 1, 0, 0), false
        );

        Mockito.when(service.update(eq(id), any(Todo.class))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.taskName").value("Hacer la tareaEditado"));
    }

    @Test
    public void should_return_not_found_when_todo_does_not_exist_putTodo() throws Exception {
        // Arrange
        String id = "3123";
        Todo todo = new Todo(
            id, "Hacer la tareaEditado", PriorityEnum.High,
            LocalDateTime.of(2025, 6, 1, 0, 0), false
        );

        Mockito.when(service.update(eq(id), any(Todo.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_bad_request_when_invalid_input_putTodo() throws Exception {
        // Arrange
        String id = "2";
        String invalidTodoJson = "{\"id\":\"2\",\"taskName\":\"Hacer la tareaEditado\",\"priority\":\"High\",\"dueDate\":\"2025-04-08T20:37:35.024+00:00\",\"done\":false}";

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTodoJson))
            .andExpect(status().isBadRequest());
    }


    @Test
    public void should_update_done_when_todo_exists_post_done() throws Exception {
        // Arrange
        String id = "2";
        Todo todo = new Todo(
            id, "Hacer la tarea", PriorityEnum.High,
            LocalDateTime.of(2025, 6, 1, 0, 0), true
        );

        Mockito.when(service.updateDone(eq(id))).thenReturn(todo);
        Mockito.when(service.save(todo)).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + id + "/done")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    public void should_return_not_found_when_todo_does_not_exist_post_done() throws Exception {
        // Arrange
        String id = "31234";
        Mockito.when(service.updateDone(eq(id))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/todos/" + id + "/done")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    public void should_update_undone_when_todo_exists_put_undone() throws Exception {
        // Arrange
        String id = "2";
        Todo todo = new Todo(
            id, "Hacer la tareaEditado", PriorityEnum.High,
            LocalDateTime.of(2025, 6, 1, 0, 0), false
        );

        Mockito.when(service.updateUndone(eq(id))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/todos/" + id + "/undone")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    public void should_return_not_found_when_todo_does_not_exist_put_undone() throws Exception {
        // Arrange
        String id = "12345";
        Mockito.when(service.updateUndone(eq(id))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/todos/" + id + "/undone")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void should_delete_todo_when_todo_exists_delete() throws Exception {
        // Arrange
        String id = "2";
        Todo todo = new Todo(
            id, "Hacer la tarea", PriorityEnum.High,
            LocalDateTime.of(2025, 6, 1, 0, 0), false
        );

        Mockito.when(service.findById(eq(id))).thenReturn(todo);
        Mockito.doNothing().when(service).remove(eq(id));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_not_found_when_todo_does_not_exist_delete() throws Exception {
        // Arrange
        String id = "1234";
        Mockito.when(service.findById(eq(id))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    

    @Test
    public void should_return_metrics() throws Exception {
        // Arrange
        Map<String, String> averageTimeToFinishByPriority = new HashMap<>();
        averageTimeToFinishByPriority.put("Low", "0 days, 0 hours, 5 minutes, 0 seconds");
        averageTimeToFinishByPriority.put("Medium", "1 days, 1 hours, 10 minutes, 0 seconds");
        averageTimeToFinishByPriority.put("High", "3 days, 12 hours, 17 minutes, 24 seconds");

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("averageTimeToFinishByPriority", averageTimeToFinishByPriority);
        metrics.put("averageEstimatedTimeToComplete", "0 days, 12 hours, 0 minutes, 0 seconds");

        Mockito.when(service.getMetrics()).thenReturn(metrics);
        
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/metrics")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.averageTimeToFinishByPriority.Low").value("0 days, 0 hours, 5 minutes, 0 seconds"))
            .andExpect(jsonPath("$.averageTimeToFinishByPriority.High").value("3 days, 12 hours, 17 minutes, 24 seconds"))
            .andExpect(jsonPath("$.averageEstimatedTimeToComplete").value("0 days, 12 hours, 0 minutes, 0 seconds"));

        System.out.println("Mock configurado despues de perform: " + Mockito.mockingDetails(service).isMock());
    }

    @Test
    public void should_getTodos_with_filters() throws Exception {
        // Arrange
        List<Todo> filteredTodos = Arrays.asList(
            new Todo("2", "Hacer la tarea", PriorityEnum.High, 
                LocalDateTime.of(2025, 6, 1,0,0), false),
            new Todo("3", "Llamar al doctor", PriorityEnum.Low, 
                LocalDateTime.of(2025, 3, 1,0,0), false)
        );

        String filteredTodosJson = objectMapper.writeValueAsString(filteredTodos);


        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", 2);
        response.put("filteredTodo", filteredTodosJson);
        response.put("totalPages", 1);
        response.put("currentPage", 0);
      
        Mockito.when(service.getTodosFiltered(
            eq(false),
            ArgumentMatchers.<String>nullable(String.class),
            any(PriorityEnum.class),
            eq(0),
            eq(Arrays.asList("priority")),
            eq(Sort.Direction.DESC)
        )).thenReturn(response);
       
        // Act & Assert
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todos")
            .param("sortBy", "priority")
            .param("sortDirection", "DESC")
            .param("page", "0")
            .param("size", "2")
            .param("done", "false")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        System.out.println("Respuesta JSONNUEVO1: " + result.getResponse().getContentAsString());
    }

}
