package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.UserDto;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void createUser_should_return_user() throws BadHttpRequest {
        User user = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999");

        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.createUser(user), is(notNullValue()));
    }

    @Test
    void getUsers_should_return_users() {
        User user1 = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999");
        User user2 = createUser("Jose Rizal", "jose@oocl.com", "08888888888");

        UserDto userDto1 =  modelMapper.map(user1, UserDto.class);
        UserDto userDto2 = modelMapper.map(user2, UserDto.class);

        when(userRepository.findAll()).thenReturn(asList(user1, user2));
        List<UserDto> returnedUserDtoList = userService.getUsers();

        assertThat(user1.getName(), is(returnedUserDtoList.get(0).getName()));
        assertThat(user2.getName(), is(returnedUserDtoList.get(1).getName()));
    }

    //todo: find a way to trigger actual ConstraintViolationException from JPA for testing
//    @Test
//    void createUser_should_throw_bad_request_exception_when_email_is_not_valid_format() {
//        User user = createUser("Juan Dela Cruz", "juanooclcom", "09999999999", "password");
//
//        doThrow(ConstraintViolationException.class).when(userRepository).save(user);
//
//        assertThrows(BadHttpRequest.class, () -> userService.createUser(user));
//    }
//
//    @Test
//    void createUser_should_throw_bad_request_exception_when_phone_number_is_not_valid_format() {
//        User user = createUser("Juan Dela Cruz", "juan@oocl.com", "111", "password");
//
//        doThrow(ConstraintViolationException.class).when(userRepository).save(user);
//
//        assertThrows(BadHttpRequest.class, () -> userService.createUser(user));
//    }

    private User createUser(String name, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
