package com.factoryreceipt.factoryreceipt.repository;

import com.factoryreceipt.factoryreceipt.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findByShopName(String shopName);
}
