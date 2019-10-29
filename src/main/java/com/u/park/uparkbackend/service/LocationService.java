package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.Location;
import com.u.park.uparkbackend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location getLocationByName(String name) {
        return locationRepository.findOneByName(name);
    }

    public List<Location> createLocations(List<Location> locationList) {
        return locationRepository.saveAll(locationList);
    }

    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
}
