package com.cloudbees.project.controllers;

import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.models.User;
import com.cloudbees.project.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> listUsers() {
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) throws CustomException{
        try {
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> editUser(@RequestBody User user, @PathVariable String id) {
        try {
            return new ResponseEntity<>(userService.editUser(user, id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }
}
