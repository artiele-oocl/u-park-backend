package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.UserDto;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
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

    public UserDto createUser(User user) throws BadHttpRequest {
        if (!isEmpty(userRepository.findUserByPhoneNumber(user.getPhoneNumber()))
                || !isEmpty(userRepository.findUserByEmail(user.getEmail())))
            throw new BadHttpRequest();
        try {
            User userEntity = userRepository.save(user);
            UserDto userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setName(userEntity.getName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setPhoneNumber((userEntity.getPhoneNumber()));

            return userDto;
        } catch (ConstraintViolationException e) {
            throw new BadHttpRequest();
        }
    }

    public List<UserDto> getUsers() {
        List<User> userEntityList = userRepository.findAll();

        List<UserDto> userDtoList = new ArrayList<>();

        for (User userEntity : userEntityList) {
            UserDto userDto = new UserDto();

            userDto.setId(userEntity.getId());
            userDto.setName(userEntity.getName());
            userDto.setEmail(userEntity.getEmail());
            userDto.setPhoneNumber(userEntity.getPhoneNumber());

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

        User userEntity = userRepository.findOneByUsernameAndPassword(username, password);

        UserDto userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setName(userEntity.getName());
        userDto.setPhoneNumber(userEntity.getPhoneNumber());

        return userDto;
    }

    private String getUserName(String email, String phoneNumber) {
        return !isEmpty(email) ? email : phoneNumber;
    }
}
