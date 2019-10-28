package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.dto.ParkingLotAndDistanceDto;
import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/parkinglots")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingLot>> getParkingLots() {
        return new ResponseEntity<>(parkingLotService.getParkingLots(), HttpStatus.OK);
    }

    @GetMapping(path = "/nearest", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingLotAndDistanceDto>> getNearestParkingLots(@RequestParam Double latitude, @RequestParam Double longitude) {
        return new ResponseEntity<>(parkingLotService.getNearestParkingLotsFromLocation(latitude, longitude), HttpStatus.OK);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingLot>> createParkingLot(@RequestBody List<ParkingLot> parkingLotList) {
        return new ResponseEntity<>(parkingLotService.createParkingLot(parkingLotList), HttpStatus.CREATED);
    }
}
