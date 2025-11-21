package com.factoryreceipt.factoryreceipt.repository;

import com.factoryreceipt.factoryreceipt.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByUserId(String userId);
}
