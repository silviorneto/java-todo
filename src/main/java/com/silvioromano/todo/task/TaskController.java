package com.silvioromano.todo.task;

import com.silvioromano.todo.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid TaskDto taskDto, HttpServletRequest request) {
        var taskToCreate = new TaskModel();
        Utils.copyNonNullProperties(taskDto, taskToCreate);

        taskToCreate.setUserID((UUID) request.getAttribute("userID"));

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskToCreate.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date must be equals or after current date");
        }

        if (currentDate.isAfter(taskToCreate.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be equals or after current date");
        }

        if (taskToCreate.getStartAt().isAfter(taskToCreate.getEndAt())
                || taskToCreate.getStartAt().isEqual(taskToCreate.getEndAt())
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be after start date");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(taskToCreate));
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> findAll(HttpServletRequest request) {
        List<TaskModel> tasks = taskRepository.findByUserID((UUID) request.getAttribute("userID"));
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") UUID taskID, HttpServletRequest request) {
        UUID reqUserID = (UUID) request.getAttribute("userID");

        var task = taskRepository.findByUuidAndUserID(taskID, reqUserID);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(task.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody TaskDto taskDto, @PathVariable("id") UUID taskID, HttpServletRequest request) {
        UUID reqUserID = (UUID) request.getAttribute("userID");

        var taskFounded = taskRepository.findByUuidAndUserID(taskID, reqUserID);
        if (taskFounded.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        TaskModel taskToUpdate = (TaskModel) Utils.createDeepCopyObject(taskFounded.get());
        if (taskToUpdate == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during process");
        }

        Utils.copyNonNullProperties(taskDto, taskToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID taskID, HttpServletRequest request) {
        UUID reqUserID = (UUID) request.getAttribute("userID");

        var taskFounded = taskRepository.findByUuidAndUserID(taskID, reqUserID);
        if (taskFounded.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        TaskModel taskToDelete = (TaskModel) Utils.createDeepCopyObject(taskFounded.get());
        if (taskToDelete == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during process");
        }

        taskRepository.delete(taskToDelete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
