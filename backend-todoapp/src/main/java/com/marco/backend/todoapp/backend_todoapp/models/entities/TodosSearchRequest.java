package com.marco.backend.todoapp.backend_todoapp.models.entities;

import java.util.List;
import org.springframework.data.domain.Sort;

import com.marco.backend.todoapp.backend_todoapp.validations.ValidPriority;
import com.marco.backend.todoapp.backend_todoapp.validations.ValidSort;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;


public class TodosSearchRequest {
    
    @Min(value = 0, message = "The page must be a non-negative number")
    private Integer page = 0;

    private Boolean done;

    @Size(max = 100, message = "The name must not exceed 100 characters")
    private String name;
    
    @ValidPriority
    private String priority; 
    @Size(max = 5, message = "You can specify up to 5 fields for sorting")
    private List<String> sortBy;

    @ValidSort
    private String sortDirection;

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Boolean getDone() { return done; }
    public void setDone(Boolean done) { this.done = done; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PriorityEnum getPriority() {
        if (priority == null || priority.isEmpty()) {
            return null; 
        }
        return PriorityEnum.valueOf(priority.toUpperCase()); 
    }
    public void setPriority(String priority) { this.priority = priority; }

    public List<String> getSortBy() { return sortBy; }
    public void setSortBy(List<String> sortBy) { this.sortBy = sortBy; }

    public Sort.Direction getSortDirection() {
        if (sortDirection == null || sortDirection.isEmpty()) {
            return null; 
        }
        return Sort.Direction.valueOf(sortDirection); 
    }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}
