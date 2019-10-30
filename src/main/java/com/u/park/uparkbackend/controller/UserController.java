package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.dto.UserDto;
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
    public ResponseEntity<UserDto> createUser(@RequestBody User user) throws BadHttpRequest {
        UserDto userDto = userService.createUser(user);
        if (isNull(userDto)) {
            return null;
        }
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @RequestMapping(path = "/login")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserByUsernameAndPassword(@RequestBody User user) {
        UserDto isExisting = userService.findUserByUsernameAndPassword(user);
        if (isNull(isExisting)) {
            return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isExisting, HttpStatus.OK);
    }

    @RequestMapping(path = "/wallet")
    @PatchMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> topUpToWallet(@RequestBody User user) {
        return new ResponseEntity<>(userService.updateUserWallet(user), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                              @RequestBody UserDto userDto) {
        UserDto isUpdated = userService.updateUser(id, userDto);
        if (isNull(isUpdated)) {
            return new ResponseEntity<>(new UserDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }
}
