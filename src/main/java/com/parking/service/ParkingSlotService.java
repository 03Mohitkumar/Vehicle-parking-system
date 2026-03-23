package com.parking.service;

import com.parking.entity.ParkingSlot;
import com.parking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    public ParkingSlot saveSlot(ParkingSlot slot) {
        return parkingSlotRepository.save(slot);
    }

    public Optional<ParkingSlot> findById(Long id) {
        return parkingSlotRepository.findById(id);
    }

    public Optional<ParkingSlot> findBySlotNumber(String slotNumber) {
        return parkingSlotRepository.findBySlotNumber(slotNumber);
    }

    public List<ParkingSlot> findAllSlots() {
        return parkingSlotRepository.findAll();
    }

    public List<ParkingSlot> findActiveSlots() {
        return parkingSlotRepository.findByIsActive(true);
    }

    public List<ParkingSlot> findAvailableSlots() {
        return parkingSlotRepository.findAvailableSlots(ParkingSlot.SlotStatus.AVAILABLE);
    }

    public List<ParkingSlot> findSlotsByFloor(Integer floor) {
        return parkingSlotRepository.findByFloor(floor);
    }

    public List<ParkingSlot> findSlotsByType(ParkingSlot.SlotType slotType) {
        return parkingSlotRepository.findBySlotType(slotType);
    }

    public List<ParkingSlot> findSlotsByStatus(ParkingSlot.SlotStatus status) {
        return parkingSlotRepository.findByStatus(status);
    }

    public List<ParkingSlot> findAvailableSlotsByFloor(Integer floor) {
        return parkingSlotRepository.findAvailableSlotsByFloor(floor, ParkingSlot.SlotStatus.AVAILABLE);
    }

    public List<ParkingSlot> findAvailableSlotsByType(ParkingSlot.SlotType slotType) {
        return parkingSlotRepository.findAvailableSlotsByType(slotType, ParkingSlot.SlotStatus.AVAILABLE);
    }

    public void updateSlotStatus(Long slotId, ParkingSlot.SlotStatus status) {
        Optional<ParkingSlot> slotOpt = parkingSlotRepository.findById(slotId);
        if (slotOpt.isPresent()) {
            ParkingSlot slot = slotOpt.get();
            slot.setStatus(status);
            parkingSlotRepository.save(slot);
        }
    }

    public void blockSlot(Long slotId) {
        updateSlotStatus(slotId, ParkingSlot.SlotStatus.BLOCKED);
    }

    public void unblockSlot(Long slotId) {
        updateSlotStatus(slotId, ParkingSlot.SlotStatus.AVAILABLE);
    }

    public void setSlotMaintenance(Long slotId) {
        updateSlotStatus(slotId, ParkingSlot.SlotStatus.MAINTENANCE);
    }

    public long countSlotsByStatus(ParkingSlot.SlotStatus status) {
        return parkingSlotRepository.countByStatus(status);
    }

    public long countSlotsByFloorAndStatus(Integer floor, ParkingSlot.SlotStatus status) {
        return parkingSlotRepository.countByFloorAndStatus(floor, status);
    }

    public void deleteSlot(Long id) {
        parkingSlotRepository.deleteById(id);
    }

    public void deactivateSlot(Long id) {
        Optional<ParkingSlot> slotOpt = parkingSlotRepository.findById(id);
        if (slotOpt.isPresent()) {
            ParkingSlot slot = slotOpt.get();
            slot.setIsActive(false);
            parkingSlotRepository.save(slot);
        }
    }

    public ParkingSlot createSlot(String slotNumber, Integer floor, ParkingSlot.SlotType slotType, BigDecimal hourlyRate) {
        ParkingSlot slot = new ParkingSlot(slotNumber, floor, slotType, hourlyRate);
        return saveSlot(slot);
    }

    public List<ParkingSlot> getAllActiveSlotsOrdered() {
        return parkingSlotRepository.findAllActiveSlotsOrdered();
    }
}
