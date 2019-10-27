package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.UserDto;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto createUser(User user) throws BadHttpRequest {
        User existingUser = userRepository.findUserByPhoneNumberOrEmail(user.getPhoneNumber(), user.getEmail());
        if (!isEmpty(existingUser))
            throw new BadHttpRequest();

        try {
            userRepository.save(user);
            return modelMapper.map(user, UserDto.class);
        } catch (ConstraintViolationException e) {
            throw new BadHttpRequest();
        }
    }

    public List<UserDto> getUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public UserDto findUserByUsernameAndPassword(User user) {
        String username = getUserName(user.getEmail(), user.getPhoneNumber());
        String password = user.getPassword();
        if (isEmpty(username) || isEmpty(password)) {
            return null;
        }

        user = userRepository.findOneByUsernameAndPassword(username, password);
        return modelMapper.map(user, UserDto.class);
    }

    private String getUserName(String email, String phoneNumber) {
        return !isEmpty(email) ? email : phoneNumber;
    }
}
