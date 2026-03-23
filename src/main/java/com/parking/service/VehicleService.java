package com.parking.service;

import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> findUserVehicles(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public List<Vehicle> findActiveVehicles() {
        return vehicleRepository.findByIsActive(true);
    }

    public List<Vehicle> findActiveUserVehicles(Long userId) {
        return vehicleRepository.findActiveVehiclesByUserId(userId);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleRepository.existsByLicensePlate(licensePlate);
    }

    public Vehicle createVehicle(String licensePlate, String make, String model, String color, String year, Vehicle.VehicleType vehicleType, User user) {
        if (existsByLicensePlate(licensePlate)) {
            throw new RuntimeException("Vehicle with this license plate already exists");
        }

        Vehicle vehicle = new Vehicle(licensePlate, make, model, color, year, vehicleType, user);
        return saveVehicle(vehicle);
    }

    public Vehicle updateVehicle(Long id, String licensePlate, String make, String model, String color, String year, Vehicle.VehicleType vehicleType) {
        Optional<Vehicle> vehicleOpt = findById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            
            // Check if license plate is being changed and if it already exists
            if (!vehicle.getLicensePlate().equals(licensePlate) && existsByLicensePlate(licensePlate)) {
                throw new RuntimeException("Vehicle with this license plate already exists");
            }
            
            vehicle.setLicensePlate(licensePlate);
            vehicle.setMake(make);
            vehicle.setModel(model);
            vehicle.setColor(color);
            vehicle.setYear(year);
            vehicle.setVehicleType(vehicleType);
            
            return saveVehicle(vehicle);
        }
        throw new RuntimeException("Vehicle not found");
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public void deactivateVehicle(Long id) {
        Optional<Vehicle> vehicleOpt = findById(id);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setIsActive(false);
            vehicleRepository.save(vehicle);
        }
    }
}
