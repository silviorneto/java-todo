package com.silvioromano.todo.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TaskDto(
        @NotNull
        @NotBlank(message = "Field description is mandatory")
        String description,

        @NotNull
        @NotBlank(message = "Field title is mandatory")
        @Size(max = 50, message = "Field title cannot be bigger than {max}")
        String title,

        @NotNull(message = "Field startAt cannot be null")
        @DateTimeFormat
        LocalDateTime startAt,

        @NotNull(message = "Field endAt cannot be null")
        @DateTimeFormat
        LocalDateTime endAt,

        @NotNull
        TaskPriority priority
) {
}
