package com.task.manager.service;

import com.task.manager.entity.Task;
import com.task.manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository repository){
        this.taskRepository = repository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task save(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updateTask){
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(("Task not found")));

        task.setTitle(updateTask.getTitle());
        task.setDescription(updateTask.getDescription());
        task.setCompleted(updateTask.isCompleted());

        return taskRepository.save(task);
    }

    public void deletetask(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task deleted"));
        taskRepository.delete(task);
    }

}
