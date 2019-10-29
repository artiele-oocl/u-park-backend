package com.u.park.uparkbackend.repository;

import com.u.park.uparkbackend.model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionOrderRepository extends JpaRepository<TransactionOrder, Long> {

    @Query("SELECT MAX(trns) FROM TransactionOrder trns WHERE trns.checkOut = null AND trns.userId = ?1")
    TransactionOrder findOneActiveTransactionByUserId(Long userId);

    TransactionOrder findOneById(Long transactionId);
}
