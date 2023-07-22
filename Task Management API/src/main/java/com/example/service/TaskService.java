package com.example.service;


import com.example.dto.CreateTaskDto;
import com.example.exceptions.TaskNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.model.Task;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        } else {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }
    }
    public List<Task> getAllTasksForUser(Integer userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(CreateTaskDto taskDto) {
        if (userRepository.findById(taskDto.getUserId()).isPresent()) {
            Task task = new Task();
            task.setName(taskDto.getName());
            task.setDescription(taskDto.getDescription());
            task.setCompleted(taskDto.getCompleted());
            task.setDateCreated(LocalDateTime.now());
            User u = userRepository.findById(taskDto.getUserId()).get();
            task.setUser(u);
            Task newTask = taskRepository.save(task);
            u.getTasks().add(newTask);
            userRepository.save(u);
            return newTask;
        }
       throw new UserNotFoundException("User not found");
    }

    public Task updateTask(Long id, Task task) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.isCompleted());
            taskRepository.save(existingTask);
        } else {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }
        return task;
    }



    public void deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleSetTasksToDone() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<Task> tasks = taskRepository.findByCreatedDateBeforeAndStatusNotDone(oneDayAgo);
        tasks.forEach(task -> {
            task.setCompleted(true);
            taskRepository.save(task);
        });
    }






}

