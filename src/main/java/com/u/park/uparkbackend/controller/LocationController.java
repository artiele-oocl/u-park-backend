package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.model.Location;
import com.u.park.uparkbackend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> getLocations() {
        return new ResponseEntity<>(locationService.getLocations(), HttpStatus.OK);
    }

    @GetMapping(path = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> getLocationByName(@RequestParam String name) {
        return new ResponseEntity<>(locationService.getLocationByName(name), HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> createLocations(@RequestBody List<Location> locationList) {
        return new ResponseEntity<>(locationService.createLocations(locationList), HttpStatus.CREATED);
    }
}
