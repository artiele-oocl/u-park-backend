package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.repository.ParkingLotRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ParkingLotServiceTest {

    @MockBean
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingLotService parkingLotService;

    @Test
    void getParkingLots_should_return_list_of_all_parking_lots() {
        ParkingLot parkingLot1 = createParkingLot(12345L, "Scape parking lot", 20, "24 Diosdado Macapagal Blvd, Pasay, Metro Manila", 14.538540, 120.988510);
        ParkingLot parkingLot2 = createParkingLot(6789L, "Double dragon parking lot", 30, "2850 Epifanio de los Santos Ave, Pasay, 1308 Metro Manila", 15.538540, 121.988510);

        when(parkingLotRepository.findAll()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));

        MatcherAssert.assertThat(parkingLotService.getParkingLots(), containsInAnyOrder(parkingLot1, parkingLot2));
    }

    private ParkingLot createParkingLot(Long id, String name, Integer capacity, String address, Double latitude, Double longitude) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(id);
        parkingLot.setName(name);
        parkingLot.setCapacity(capacity);
        parkingLot.setAddress(address);
        parkingLot.setLatitude(latitude);
        parkingLot.setLongitude(longitude);
        return parkingLot;
    }
}
