package com.parking.repository;

import com.parking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    
    Optional<ParkingSlot> findBySlotNumber(String slotNumber);
    
    List<ParkingSlot> findByFloor(Integer floor);
    
    List<ParkingSlot> findBySlotType(ParkingSlot.SlotType slotType);
    
    List<ParkingSlot> findByStatus(ParkingSlot.SlotStatus status);
    
    List<ParkingSlot> findByIsActive(Boolean isActive);
    
    @Query("SELECT s FROM ParkingSlot s WHERE s.status = :status AND s.isActive = true")
    List<ParkingSlot> findAvailableSlots(@Param("status") ParkingSlot.SlotStatus status);
    
    @Query("SELECT s FROM ParkingSlot s WHERE s.floor = :floor AND s.status = :status AND s.isActive = true")
    List<ParkingSlot> findAvailableSlotsByFloor(@Param("floor") Integer floor, @Param("status") ParkingSlot.SlotStatus status);
    
    @Query("SELECT s FROM ParkingSlot s WHERE s.slotType = :slotType AND s.status = :status AND s.isActive = true")
    List<ParkingSlot> findAvailableSlotsByType(@Param("slotType") ParkingSlot.SlotType slotType, @Param("status") ParkingSlot.SlotStatus status);
    
    @Query("SELECT COUNT(s) FROM ParkingSlot s WHERE s.status = :status")
    long countByStatus(@Param("status") ParkingSlot.SlotStatus status);
    
    @Query("SELECT COUNT(s) FROM ParkingSlot s WHERE s.floor = :floor AND s.status = :status")
    long countByFloorAndStatus(@Param("floor") Integer floor, @Param("status") ParkingSlot.SlotStatus status);
    
    @Query("SELECT s FROM ParkingSlot s WHERE s.isActive = true ORDER BY s.floor, s.slotNumber")
    List<ParkingSlot> findAllActiveSlotsOrdered();
}
