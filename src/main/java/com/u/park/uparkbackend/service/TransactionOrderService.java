package com.u.park.uparkbackend.service;

import com.u.park.uparkbackend.dto.TransactionOrderDto;
import com.u.park.uparkbackend.model.ParkingLot;
import com.u.park.uparkbackend.model.TransactionOrder;
import com.u.park.uparkbackend.model.User;
import com.u.park.uparkbackend.repository.ParkingLotRepository;
import com.u.park.uparkbackend.repository.TransactionOrderRepository;
import com.u.park.uparkbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionOrderService {

    @Autowired
    private TransactionOrderRepository transactionOrderRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public TransactionOrder saveTransactionOrder(TransactionOrder transactionOrder) {
        transactionOrder.setCheckIn(getCurrentTime());
        return transactionOrderRepository.save(transactionOrder);
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public List<TransactionOrderDto> getTransactionOrdersOfUser(Long userId) {
        List<TransactionOrder> transactionOrderList = transactionOrderRepository.findByUserId(userId);
        List<TransactionOrderDto> transactionOrderDtoList = new ArrayList<>();
        for (TransactionOrder transactionOrder : transactionOrderList) {
            TransactionOrderDto transactionOrderDto = modelMapper.map(transactionOrder, TransactionOrderDto.class);
            transactionOrderDto.setParkingLotName(getParkingLotNameById(transactionOrder.getParkingLotId()));
            transactionOrderDtoList.add(transactionOrderDto);
        }
        return transactionOrderDtoList;
    }

    private String getParkingLotNameById(Long parkingLotId) {
        return parkingLotRepository.findById(parkingLotId)
                .map(ParkingLot::getName)
                .orElse(null);
    }

    public TransactionOrder getActiveTransactionOrder(Long userId) {
        return transactionOrderRepository.findOneActiveTransactionByUserId(userId);
    }

    public void updateRatings(TransactionOrder transactionOrder) {
        TransactionOrder transOrder = transactionOrderRepository
                .findOneById(transactionOrder.getId());

        transOrder.setStarRating(transactionOrder.getStarRating());
        transactionOrderRepository.save(transOrder);
    }


    public Map<String, Object> updateAndGetTransaction(Long transactionId) {
        Map<String, Object> map = new HashMap<String, Object>();

        TransactionOrder transOrder = transactionOrderRepository
                .findOneById(transactionId);
        ParkingLot parkingLot = parkingLotRepository
                .findById(transOrder.getParkingLotId())
                .orElse(null);

        transOrder.setCheckOut(getCurrentTime());
        calculateTotal(transOrder, parkingLot);
        transactionOrderRepository.save(transOrder);
        map.put("transactionOrder", transOrder);
        map.put("parkingLot", parkingLot);
        return map;

    }

    private void calculateTotal(TransactionOrder transOrder, ParkingLot parkingLot) {
        long minutes = ChronoUnit.MINUTES.between(transOrder.getCheckIn(), getCurrentTime());
        long hours = ChronoUnit.HOURS.between(transOrder.getCheckIn(), getCurrentTime());

        hours = hours < 1 ? 1 : hours;
        Float totalFee  = hours * parkingLot.getRate();
        totalFee = minutes > 1 ? totalFee + parkingLot.getRate(): totalFee;
        updateWallet(transOrder.getUserId(), totalFee);
        transOrder.setTotalFee(totalFee);

    }

    private void updateWallet(Long userId, Float totalFee) {
        User user = userRepository.findOneById(userId);
        float currentBalance = user.getWallet();
        float remainingBalance = currentBalance - totalFee;
        user.setWallet(remainingBalance);
        userRepository.save(user);
    }
}
