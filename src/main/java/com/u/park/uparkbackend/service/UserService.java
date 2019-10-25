package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws BadHttpRequest {
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new BadHttpRequest();
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
