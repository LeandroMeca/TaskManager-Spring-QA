package com.task.manager.controller;

import com.task.manager.entity.Task;
import com.task.manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> list(){
        return taskService.findAll();
    }

    @PostMapping
    public Task create(@RequestBody Task task){
        return taskService.save(task);
    }

}
