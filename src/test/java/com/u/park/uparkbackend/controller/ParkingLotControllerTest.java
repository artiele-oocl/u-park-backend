package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.service.ParkingLotService;
import com.u.park.uparkbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingLotController.class)
public class ParkingLotControllerTest {

    @MockBean
    private ParkingLotService parkingLotService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getParkingLots_should_return_list_of_all_parking_lots_and_return_status_code_200() throws Exception {
        ParkingLot parkingLot1 = createParkingLot(12345L, "Scape parking lot", 20, "24 Diosdado Macapagal Blvd, Pasay, Metro Manila", 14.538540, 120.988510);
        ParkingLot parkingLot2 = createParkingLot(6789L, "Double dragon parking lot", 20, "2850 Epifanio de los Santos Ave, Pasay, 1308 Metro Manila", 15.538540, 121.988510);

        when(parkingLotService.getParkingLots()).thenReturn(Arrays.asList(parkingLot1, parkingLot2));

        ResultActions result = mvc.perform(get("/api/parkinglots"));

        result.andExpect(status().isOk())
                .andExpect((jsonPath("$[0].name").value("Scape parking lot")))
                .andExpect((jsonPath("$[1].name").value("Double dragon parking lot")));
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
