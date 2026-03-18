package com.task.manager.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.manager.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTask() throws Exception{

        Task task = new Task();
        task.setTitle("Estudar teste de integração");
        task.setDescription("Aprender SpringBootTest");
        task.setCompleted(false);

        mockMvc.perform(post("/tasks").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task))).andExpect(status().isOk());
    }
}
