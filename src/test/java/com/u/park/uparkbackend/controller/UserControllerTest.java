package com.u.park.uparkbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createUser_should_create_user_when_all_values_are_present_and_return_status_code_201() throws Exception {
        User user = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999", "password");

        when(userService.createUser(any())).thenReturn(user);

        ResultActions result = mvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(user)));

        result.andExpect(status().isCreated())
                .andExpect((jsonPath("$.name").value("Juan Dela Cruz")))
                .andExpect((jsonPath("$.email").value("juan@oocl.com")))
                .andExpect((jsonPath("$.phoneNumber").value("09999999999")));
    }

    @Test
    void getUsers_should_return_list_of_all_users_and_return_status_code_200() throws Exception {
        User user1 = createUser("Juan Dela Cruz", "juan@oocl.com", "09999999999", "password");
        User user2 = createUser("Jose Rizal", "jose@oocl.com", "08888888888", "password2");

        when(userService.getUsers()).thenReturn(asList(user1, user2));

        ResultActions result = mvc.perform(get("/api/users"));

        result.andExpect(status().isOk())
                .andExpect((jsonPath("$[0].name").value("Juan Dela Cruz")))
                .andExpect((jsonPath("$[0].email").value("juan@oocl.com")))
                .andExpect((jsonPath("$[0].phoneNumber").value("09999999999")))
                .andExpect((jsonPath("$[1].name").value("Jose Rizal")))
                .andExpect((jsonPath("$[1].email").value("jose@oocl.com")))
                .andExpect((jsonPath("$[1].phoneNumber").value("08888888888")));
    }

    private User createUser(String name, String email, String phoneNumber, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(getMd5(password));
        return user;
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
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