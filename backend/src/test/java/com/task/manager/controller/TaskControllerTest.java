package com.task.manager.controller;


import com.task.manager.entity.Task;
import com.task.manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Test
    void shouldReturnTask() throws Exception {
        Task task = new Task(1L,"Estudar","JUnit",false);

        when(service.findAll()).thenReturn(List.of(task));
        mockMvc.perform(get("/tasks")).andExpect(status().isOk()).
                andExpect(jsonPath("$[0].title").value("Estudar"));
    }


}
