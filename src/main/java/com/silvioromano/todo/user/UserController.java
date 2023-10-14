package com.silvioromano.todo.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.silvioromano.todo.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        Optional<UserModel> user = userRepository.findByUsername(userDto.username());
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        var userToCreate = new UserModel();
        Utils.copyNonNullProperties(userDto, userToCreate);

        var passwordHashed = BCrypt
                .withDefaults()
                .hashToString(12, userToCreate.getPassword().toCharArray());
        userToCreate.setPassword(passwordHashed);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userToCreate));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        var users = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
