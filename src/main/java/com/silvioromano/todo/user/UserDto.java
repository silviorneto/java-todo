package com.silvioromano.todo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto (
        @NotNull(message = "Field username cannot be null")
        @NotBlank(message = "Field username is mandatory")
        @Size(max = 50, message = "Field username cannot be bigger than {max}")
        String username,
        @NotNull(message = "Field name cannot be null")
        @NotBlank(message = "Field name is mandatory")
        @Size(max = 80, message = "Field name cannot be bigger than {max}")
        String name,
        @NotNull(message = "Field password cannot be null")
        @NotBlank(message = "Field password is mandatory")
        String password
    ) {
}
