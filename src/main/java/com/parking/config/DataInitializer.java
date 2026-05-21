package com.parking.config;

import com.parking.entity.ParkingSlot;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.service.ParkingSlotService;
import com.parking.service.UserService;
import com.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Override
    public void run(String... args) throws Exception {
        //  initializeUsers();
       // initializeParkingSlots();
       // initializeVehicles();
    }

    private void initializeUsers() {
        // Create admin user
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", "admin@parking.com", "admin123", "Admin", "User", User.Role.ADMIN);
            userService.saveUser(admin);
            System.out.println("Admin user created: admin/admin123");
        }

        // Create staff user
        if (!userService.existsByUsername("staff")) {
            User staff = new User("staff", "staff@parking.com", "staff123", "Staff", "Member", User.Role.STAFF);
            userService.saveUser(staff);
            System.out.println("Staff user created: staff/staff123");
        }

        // Create regular users
        if (!userService.existsByUsername("user1")) {
            User user1 = new User("user1", "user1@example.com", "user123", "John", "Doe", User.Role.USER);
            userService.saveUser(user1);
            System.out.println("User created: user1/user123");
        }

        if (!userService.existsByUsername("user2")) {
            User user2 = new User("user2", "user2@example.com", "user123", "Jane", "Smith", User.Role.USER);
            userService.saveUser(user2);
            System.out.println("User created: user2/user123");
        }
    }

    private void initializeParkingSlots() {
        // Floor 1 - Standard slots
        for (int i = 1; i <= 20; i++) {
            String slotNumber = "A" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 1, ParkingSlot.SlotType.STANDARD, new BigDecimal("10.00"));
            }
        }

        // Floor 1 - Premium slots
        for (int i = 1; i <= 10; i++) {
            String slotNumber = "P" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 1, ParkingSlot.SlotType.PREMIUM, new BigDecimal("15.00"));
            }
        }

        // Floor 2 - Standard slots
        for (int i = 1; i <= 20; i++) {
            String slotNumber = "B" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 2, ParkingSlot.SlotType.STANDARD, new BigDecimal("10.00"));
            }
        }

        // Floor 2 - EV Charging slots
        for (int i = 1; i <= 5; i++) {
            String slotNumber = "EV" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 2, ParkingSlot.SlotType.EV_CHARGING, new BigDecimal("12.00"));
            }
        }

        // Floor 3 - Disabled slots
        for (int i = 1; i <= 8; i++) {
            String slotNumber = "D" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 3, ParkingSlot.SlotType.DISABLED, new BigDecimal("5.00"));
            }
        }

        // Floor 3 - Standard slots
        for (int i = 1; i <= 15; i++) {
            String slotNumber = "C" + String.format("%02d", i);
            if (!parkingSlotService.findBySlotNumber(slotNumber).isPresent()) {
                parkingSlotService.createSlot(slotNumber, 3, ParkingSlot.SlotType.STANDARD, new BigDecimal("8.00"));
            }
        }

        // Slots stay AVAILABLE on startup; OCCUPIED/RESERVED are set only by bookings

        System.out.println("Parking slots initialized");
    }

    private void initializeVehicles() {
        // Get users
        User user1 = userService.findByUsername("user1").orElse(null);
        User user2 = userService.findByUsername("user2").orElse(null);

        if (user1 != null) {
            // Add vehicles for user1
            if (!vehicleService.existsByLicensePlate("ABC-123")) {
                vehicleService.createVehicle("ABC-123", "Toyota", "Camry", "Blue", "2020", Vehicle.VehicleType.CAR, user1);
            }
            if (!vehicleService.existsByLicensePlate("XYZ-789")) {
                vehicleService.createVehicle("XYZ-789", "Honda", "Civic", "Red", "2019", Vehicle.VehicleType.CAR, user1);
            }
        }

        if (user2 != null) {
            // Add vehicles for user2
            if (!vehicleService.existsByLicensePlate("DEF-456")) {
                vehicleService.createVehicle("DEF-456", "Ford", "F-150", "Black", "2021", Vehicle.VehicleType.TRUCK, user2);
            }
            if (!vehicleService.existsByLicensePlate("GHI-012")) {
                vehicleService.createVehicle("GHI-012", "Tesla", "Model 3", "White", "2022", Vehicle.VehicleType.CAR, user2);
            }
        }

        System.out.println("Sample vehicles initialized");
    }
}
