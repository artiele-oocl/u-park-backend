package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.UserDto;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.UserRepository;
import javassist.tools.web.BadHttpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto createUser(User user) throws BadHttpRequest {
        User existingUser = userRepository.findUserByPhoneNumberOrEmail(user.getPhoneNumber(), user.getEmail());
        if (!isEmpty(existingUser)) throw new BadHttpRequest();

        try {
            user.setPassword(hashPassword(user.getPassword()));
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
        String password = hashPassword(user.getPassword());
        if (isEmpty(username) || isEmpty(password)) {
            return null;
        }

        user = userRepository.findOneByUsernameAndPassword(username, password);
        return modelMapper.map(user, UserDto.class);
    }

    private String getUserName(String email, String phoneNumber) {
        return !isEmpty(email) ? email : phoneNumber;
    }

    private String hashPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDto updateUserWallet(User user) {
        User walletUser;
        Optional<User> walletOwner = userRepository.findById(user.getId());
        if (walletOwner.isPresent()) {
            walletUser = walletOwner.get();
            walletUser.setWallet(walletUser.getWallet() + user.getWallet());
            walletUser = userRepository.save(walletUser);
            return modelMapper.map(walletUser, UserDto.class);
        }
        return null;
    }

    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if (isNull(user)) {
            return null;
        }
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }
}
