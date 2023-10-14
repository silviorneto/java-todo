package com.silvioromano.todo.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;

    private String description;

    @Column(length = 50)
    private String title;

    private UUID userID;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private TaskPriority priority;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
