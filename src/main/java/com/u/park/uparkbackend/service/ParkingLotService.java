package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.ParkingLotAndDistance;
import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }

    public List<ParkingLotAndDistance> getNearestParkingLotsFromLocation(Double latitude, Double longitude) {
        List<ParkingLot> parkingLotList = parkingLotRepository.findAll();
        List<ParkingLotAndDistance> nearestParkingLotsAndDistance = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLotList) {
            Double distance = distance(latitude, longitude, parkingLot.getLatitude(), parkingLot.getLongitude());
            if (distance < 2.00) {
                nearestParkingLotsAndDistance.add(convertToParkingLotsAndDistance(parkingLot, distance));
            }
        }
        return nearestParkingLotsAndDistance;
    }

    private ParkingLotAndDistance convertToParkingLotsAndDistance(ParkingLot parkingLot, Double distance) {
        ParkingLotAndDistance parkingLotAndDistance = new ParkingLotAndDistance();
        parkingLotAndDistance.setId(parkingLot.getId());
        parkingLotAndDistance.setName(parkingLot.getName());
        parkingLotAndDistance.setCapacity(parkingLot.getCapacity());
        parkingLotAndDistance.setAddress(parkingLot.getAddress());
        parkingLotAndDistance.setLatitude(parkingLot.getLatitude());
        parkingLotAndDistance.setLongitude(parkingLot.getLongitude());
        parkingLotAndDistance.setRate(parkingLot.getRate());
        parkingLotAndDistance.setDistance(distance);
        return parkingLotAndDistance;
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
}
