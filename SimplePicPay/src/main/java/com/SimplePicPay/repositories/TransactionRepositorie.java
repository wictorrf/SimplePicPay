package com.SimplePicPay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SimplePicPay.domain.transaction.Transaction;

public interface TransactionRepositorie extends JpaRepository<Transaction, Long> {
    
}
