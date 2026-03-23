package com.parking.repository;

import com.parking.entity.User;
import com.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    
    boolean existsByLicensePlate(String licensePlate);
    
    List<Vehicle> findByUser(User user);
    
    List<Vehicle> findByUserId(Long userId);
    
    List<Vehicle> findByIsActive(Boolean isActive);
    
    @Query("SELECT v FROM Vehicle v WHERE v.user.id = :userId AND v.isActive = true")
    List<Vehicle> findActiveVehiclesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT v FROM Vehicle v WHERE v.licensePlate = :licensePlate AND v.isActive = true")
    Optional<Vehicle> findActiveVehicleByLicensePlate(@Param("licensePlate") String licensePlate);
}
