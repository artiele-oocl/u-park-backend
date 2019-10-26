package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws BadHttpRequest {
        if (!isEmpty(userRepository.findUserByPhoneNumber(user.getPhoneNumber()))) throw new BadHttpRequest();
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new BadHttpRequest();
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findUserByUsernameAndPassword(User user) {
        String username = getUserName(user.getEmail(), user.getPhoneNumber());
        String password = user.getPassword();
        if (isEmpty(username) || isEmpty(password)) {
            return null;
        }
        return userRepository.findOneByUsernameAndPassword(username, password);
    }

    private String getUserName(String email, String phoneNumber) {
        return !isEmpty(email) ? email : phoneNumber;
    }
}
