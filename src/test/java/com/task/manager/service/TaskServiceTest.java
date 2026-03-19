package com.task.manager.service;


import com.task.manager.entity.Task;
import com.task.manager.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;


    @Test
    @DisplayName("Return task")
    void shouldReturnAllTask(){
        Task task = new Task(1L,"Estudar","Spring Boot",false);

        when(repository.findAll()).thenReturn(List.of(task));
        List<Task> tasks = service.findAll();
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Save task")
    void shouldCreateTask(){
        Task task = new Task(null, "Estudar", "JUnit", false);
        Task taskSaved = new Task(1L, "Estudar", "JUnit", false);

        when(repository.save(task)).thenReturn(taskSaved);
        Task result = service.save(task);
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("List not null")
    void shouldNotReturnEmptyList(){

        when(repository.findAll()).thenReturn(List.of());
        List<Task> tasks = service.findAll();
        assertTrue(tasks.isEmpty());
    }


}
