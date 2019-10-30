package com.u.park.uparkbackend.controller;

import com.u.park.uparkbackend.dto.TransactionOrderDto;
import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.model.TransactionOrder;
import com.u.park.uparkbackend.service.ParkingLotService;
import com.u.park.uparkbackend.service.TransactionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/transactionOrder")
public class TransactionOrderController {

    @Autowired
    private TransactionOrderService transactionOrderService;

    @Autowired
    private ParkingLotService parkingLotService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionOrder> addTransactionOrder(@RequestBody TransactionOrder transactionOrder) {
        return new ResponseEntity<>(transactionOrderService.saveTransactionOrder(transactionOrder), CREATED);
    }

    @GetMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> getActiveTransactionOrder(@PathVariable("userId") Long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        TransactionOrder transactionOrder = transactionOrderService.getActiveTransactionOrder(userId);
        ParkingLot parkingLot = parkingLotService.getParkingLotbyId(transactionOrder.getParkingLotId());
        map.put("transactionOrder", transactionOrder);
        map.put("parkingLot", parkingLot);
        return map;
    }

    @GetMapping(path = "/all/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionOrderDto>> getTransactionOrdersOfUser(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionOrderService.getTransactionOrdersOfUser(userId), OK);
    }
}