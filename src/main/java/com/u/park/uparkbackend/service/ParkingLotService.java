package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.ParkingLotAndDistanceDto;
import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.repository.ParkingLotRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }

    public List<ParkingLotAndDistanceDto> getNearestParkingLotsFromLocation(Double latitude, Double longitude) {
        List<ParkingLot> parkingLotList = parkingLotRepository.findAll();
        List<ParkingLotAndDistanceDto> nearestParkingLotsAndDistance = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLotList) {
            Double distance = distance(latitude, longitude, parkingLot.getLatitude(), parkingLot.getLongitude());
            if (distance < 2.00) {
                ParkingLotAndDistanceDto parkingLotAndDistanceDto = modelMapper.map(parkingLot, ParkingLotAndDistanceDto.class);
                parkingLotAndDistanceDto.setDistance(distance);
                nearestParkingLotsAndDistance.add(parkingLotAndDistanceDto);
            }
        }
        return nearestParkingLotsAndDistance.stream()
                .sorted(Comparator.comparingDouble(ParkingLotAndDistanceDto::getDistance))
                .collect(Collectors.toList());
    }

    private double distance(double fromLat, double fromLong, double toLat, double toLong) {
        if ((fromLat == toLat) && (fromLong == toLong)) {
            return 0;
        } else {
            double theta = fromLong - toLong;
            double dist = Math.sin(Math.toRadians(fromLat)) * Math.sin(Math.toRadians(toLat)) + Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }

    public List<ParkingLot> createParkingLot(List<ParkingLot> parkingLot) {
        return parkingLotRepository.saveAll(parkingLot);
    }

    public ParkingLot getParkingLotbyId(Long parkingLotId) {
        return parkingLotRepository.findById(parkingLotId)
                .orElse(null);
    }
}
