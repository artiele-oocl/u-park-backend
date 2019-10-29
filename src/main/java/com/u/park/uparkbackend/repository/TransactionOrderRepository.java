package com.u.park.uparkbackend.repository;

import com.u.park.uparkbackend.model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionOrderRepository extends JpaRepository<TransactionOrder, Long> {

    List<TransactionOrder> findByUserId(Long userId);
}
