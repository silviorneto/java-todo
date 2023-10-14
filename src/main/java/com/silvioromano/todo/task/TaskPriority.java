package com.silvioromano.todo.task;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TaskPriorityDeserializer.class)
public enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH;

    @Override
    public String toString() {
        String nome = this.name();
        return nome.charAt(0) + nome.substring(1).toLowerCase();
    }
}
