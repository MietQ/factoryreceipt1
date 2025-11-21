package com.factoryreceipt.factoryreceipt.service;

import com.factoryreceipt.factoryreceipt.model.DeviceRegistration;
import com.factoryreceipt.factoryreceipt.repository.DeviceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceRegistrationService {

    @Autowired
    private DeviceRegistrationRepository deviceRegistrationRepository;

    private static final int MAX_DEVICES = 2; // maksymalna liczba urządzeń

    public boolean registerDevice(String userId, String deviceId) {
        // 1) Czy to urządzenie już istnieje w bazie?
        Optional<DeviceRegistration> existing =
                deviceRegistrationRepository.findByUserIdAndDeviceId(userId, deviceId);
        if (existing.isPresent()) {
            // Urządzenie już zapisane - nic nie robimy, zwracamy true
            return true;
        }
        // 2) Pobierz wszystkie urządzenia użytkownika
        List<DeviceRegistration> devices = deviceRegistrationRepository.findByUserId(userId);
        if (devices.size() >= MAX_DEVICES) {
            // Za dużo urządzeń
            return false;
        }
        // 3) Zapisujemy nowe urządzenie
        DeviceRegistration reg = new DeviceRegistration(userId, deviceId);
        deviceRegistrationRepository.save(reg);
        return true;
    }

    public void removeDevice(String userId, String deviceId) {
        deviceRegistrationRepository.findByUserIdAndDeviceId(userId, deviceId)
                .ifPresent(deviceRegistrationRepository::delete);
    }

    public List<DeviceRegistration> listDevices(String userId) {
        return deviceRegistrationRepository.findByUserId(userId);
    }
}
