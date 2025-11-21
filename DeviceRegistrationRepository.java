package com.factoryreceipt.factoryreceipt.repository;

import com.factoryreceipt.factoryreceipt.model.DeviceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRegistrationRepository extends JpaRepository<DeviceRegistration, Long> {

    List<DeviceRegistration> findByUserId(String userId);

    Optional<DeviceRegistration> findByUserIdAndDeviceId(String userId, String deviceId);
}
