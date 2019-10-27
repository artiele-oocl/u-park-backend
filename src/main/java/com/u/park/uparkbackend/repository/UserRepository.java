package com.u.park.uparkbackend.repository;

import com.u.park.uparkbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT usr FROM User usr WHERE (usr.email = ?1 OR usr.phoneNumber = ?1) AND usr.password = ?2")
    User findOneByUsernameAndPassword(String username, String password);

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByEmail(String email);
}
