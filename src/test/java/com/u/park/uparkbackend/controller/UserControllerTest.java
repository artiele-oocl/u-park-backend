package com.u.park.uparkbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.u.park.uparkbackend.dto.UserDto;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.service.UserService;
import javassist.tools.web.BadHttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
        UserDto user = createDtoUser("Juan Dela Cruz", "juan@oocl.com", "09999999999");

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
        UserDto user1 = createDtoUser("Juan Dela Cruz", "juan@oocl.com", "09999999999");
        UserDto user2 = createDtoUser("Jose Rizal", "jose@oocl.com", "08888888888");

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

    @Test
    void createUser_should_return_status_code_400_when_given_invalid_input() throws Exception {
        UserDto user = createDtoUser("Juan Dela Cruz", "juanooclcom", "111");

        doThrow(BadHttpRequest.class).when(userService).createUser(any());

        ResultActions result = mvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(user)));

        result.andExpect(status().isBadRequest())
                .andExpect((jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value())));
    }

    private UserDto createDtoUser(String name, String email, String phoneNumber) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setPhoneNumber(phoneNumber);
        return userDto;
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}