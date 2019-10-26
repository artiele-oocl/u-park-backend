package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }
}
