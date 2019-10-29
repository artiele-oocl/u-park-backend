package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.model.TransactionOrder;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.TransactionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionOrderService {

    @Autowired
    private TransactionOrderRepository transactionOrderRepository;

    public TransactionOrder saveTransactionOrder (TransactionOrder transactionOrder){
        transactionOrder.setCheckIn(getCurrentTime());
        return transactionOrderRepository.save(transactionOrder);
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public List<TransactionOrder> getTransactionOrdersOfUser(Long userId) {
        return transactionOrderRepository.findByUserId(userId);
    }

}
