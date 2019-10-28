package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.Location;
import com.u.park.uparkbackend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getLocationByName(String name) {
        List<Location> locationList = locationRepository.findByName(name);
        if(!isNull(locationList)){
            return locationList;
        }
        return null;
    }

    public List<Location> createLocations(List<Location> locationList) {
        return locationRepository.saveAll(locationList);
    }
}
