package com.u.park.uparkbackend.repository;

import com.u.park.uparkbackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT loc FROM Location loc WHERE LOWER(loc.name) LIKE LOWER(CONCAT(?1, '%')) AND ROWNUM=1")
    List<Location> findByName(@Param("name") String name);
}
