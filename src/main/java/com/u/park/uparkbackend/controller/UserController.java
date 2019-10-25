package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.service.UserService;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) throws BadHttpRequest {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @RequestMapping(path = "/login")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsernameAndPassword(@RequestBody User user) {
        User isExisting = userService.findUserByUsernameAndPassword(user);
        if (isNull(isExisting)) {
            return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isExisting, HttpStatus.OK);
    }
}
