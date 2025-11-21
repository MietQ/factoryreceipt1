package com.factoryreceipt.factoryreceipt.repository;

import com.factoryreceipt.factoryreceipt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Możemy dodać metody wyszukiwania po userId:
    User findByUserId(String userId);
}
