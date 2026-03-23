package com.parking.controller;

import com.parking.entity.ParkingSlot;
import com.parking.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @GetMapping("/slots")
    public ResponseEntity<List<ParkingSlot>> getAllSlots() {
        List<ParkingSlot> slots = parkingSlotService.findActiveSlots();
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/available")
    public ResponseEntity<List<ParkingSlot>> getAvailableSlots() {
        List<ParkingSlot> slots = parkingSlotService.findAvailableSlots();
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/floor/{floor}")
    public ResponseEntity<List<ParkingSlot>> getSlotsByFloor(@PathVariable Integer floor) {
        List<ParkingSlot> slots = parkingSlotService.findAvailableSlotsByFloor(floor);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/type/{type}")
    public ResponseEntity<List<ParkingSlot>> getSlotsByType(@PathVariable ParkingSlot.SlotType type) {
        List<ParkingSlot> slots = parkingSlotService.findAvailableSlotsByType(type);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/slots/status")
    public ResponseEntity<Map<String, Long>> getSlotStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("available", parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.AVAILABLE));
        statusCounts.put("occupied", parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.OCCUPIED));
        statusCounts.put("reserved", parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.RESERVED));
        statusCounts.put("maintenance", parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.MAINTENANCE));
        statusCounts.put("blocked", parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.BLOCKED));
        
        return ResponseEntity.ok(statusCounts);
    }

    @PostMapping("/slots/{id}/status")
    public ResponseEntity<Map<String, String>> updateSlotStatus(@PathVariable Long id, @RequestParam ParkingSlot.SlotStatus status) {
        try {
            parkingSlotService.updateSlotStatus(id, status);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Slot status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
