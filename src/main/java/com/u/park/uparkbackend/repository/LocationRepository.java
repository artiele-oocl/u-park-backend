package com.u.park.uparkbackend.repository;

import com.u.park.uparkbackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findOneByName(String name);
}
