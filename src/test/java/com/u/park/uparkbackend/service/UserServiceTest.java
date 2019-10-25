package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    @Test
    void createUser_should_return_user() throws BadHttpRequest {
        User user = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999", "password");

        when(userRepository.save(user)).thenReturn(user);

        assertThat(userService.createUser(user), is(notNullValue()));
    }

    @Test
    void getUsers_should_return_users() {
        User user1 = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999", "password");
        User user2 = createUser("Jose Rizal", "jose@oocl.com", "08888888888", "password2");

        when(userRepository.findAll()).thenReturn(asList(user1, user2));

        assertThat(userService.getUsers(), containsInAnyOrder(user1, user2));
    }

    //todo: find a way to trigger actual ConstraintViolationException from JPA for testing
    @Test
    void createUser_should_throw_bad_request_exception_when_email_is_not_valid_format() {
        User user = createUser("Juan Dela Cruz", "juanooclcom", "09999999999", "password");

        doThrow(ConstraintViolationException.class).when(userRepository).save(user);

        assertThrows(BadHttpRequest.class, () -> userService.createUser(user));
    }

    @Test
    void createUser_should_throw_bad_request_exception_when_phone_number_is_not_valid_format() {
        User user = createUser("Juan Dela Cruz", "juan@oocl.com", "111", "password");

        doThrow(ConstraintViolationException.class).when(userRepository).save(user);

        assertThrows(BadHttpRequest.class, () -> userService.createUser(user));
    }

    private User createUser(String name, String email, String phoneNumber, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(getMd5(password));
        return user;
    }

    private String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
