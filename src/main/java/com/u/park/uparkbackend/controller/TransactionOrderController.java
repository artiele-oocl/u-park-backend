package com.u.park.uparkbackend.controller;


import com.u.park.uparkbackend.model.TransactionOrder;
import com.u.park.uparkbackend.repository.TransactionOrderRepository;
import com.u.park.uparkbackend.service.TransactionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/transactionOrder")
public class TransactionOrderController {

    @Autowired
    private TransactionOrderService transactionOrderService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionOrder> addTransactionOrder (@RequestBody  TransactionOrder transactionOrder){
        return new ResponseEntity<>(transactionOrderService.saveTransactionOrder(transactionOrder), CREATED);
    }
}