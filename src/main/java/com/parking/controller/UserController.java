package com.parking.controller;

import com.parking.entity.Booking;
import com.parking.entity.ParkingSlot;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.service.BookingService;
import com.parking.service.ParkingSlotService;
import com.parking.service.ReceiptService;
import com.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ReceiptService receiptService;

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        // Sync booking status with slot: if slot is AVAILABLE but booking still CONFIRMED/ACTIVE, mark COMPLETED
        bookingService.syncBookingStatusWithSlotForUser(user.getId());
        
        // Get user's vehicles
        List<Vehicle> vehicles = vehicleService.findActiveUserVehicles(user.getId());
        
        // Get user's recent bookings
        List<Booking> recentBookings = bookingService.findUserBookings(user.getId());
        if (recentBookings.size() > 5) {
            recentBookings = recentBookings.subList(0, 5);
        }
        
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("recentBookings", recentBookings);
        model.addAttribute("pageTitle", "My Dashboard");
        
        return "user/dashboard";
    }

    @GetMapping("/slots")
    public String viewSlots(Model model) {
        List<ParkingSlot> availableSlots = parkingSlotService.findAvailableSlots();
        model.addAttribute("slots", availableSlots);
        model.addAttribute("pageTitle", "Find Slots");
        return "user/slots";
    }

    @GetMapping("/book")
    public String bookSlot(@RequestParam Long slotId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        ParkingSlot slot = parkingSlotService.findById(slotId).orElse(null);
        List<Vehicle> vehicles = vehicleService.findActiveUserVehicles(user.getId());
        
        if (slot == null) {
            return "redirect:/user/slots?error=Slot not found";
        }
        
        model.addAttribute("slot", slot);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Book Slot");
        
        return "user/book";
    }

    @PostMapping("/book")
    public String createBooking(@RequestParam Long slotId,
                              @RequestParam Long vehicleId,
                              @RequestParam String startTime,
                              @RequestParam String expectedEndTime) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        try {
            ParkingSlot slot = parkingSlotService.findById(slotId).orElseThrow(() -> new RuntimeException("Slot not found"));
            Vehicle vehicle = vehicleService.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found"));
            
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime expectedEnd = LocalDateTime.parse(expectedEndTime);
            
            bookingService.createBooking(user, vehicle, slot, start, expectedEnd);
            
            return "redirect:/user/bookings?success=Booking created successfully";
        } catch (Exception e) {
            return "redirect:/user/slots?error=" + e.getMessage();
        }
    }

    @GetMapping("/bookings")
    public String viewBookings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        // Sync: if slot is AVAILABLE but booking still CONFIRMED/ACTIVE, mark COMPLETED
        bookingService.syncBookingStatusWithSlotForUser(user.getId());
        
        List<Booking> bookings = bookingService.findUserBookings(user.getId());
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Bookings");
        
        return "user/bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            return "redirect:/user/bookings?success=Booking cancelled successfully";
        } catch (Exception e) {
            return "redirect:/user/bookings?error=" + e.getMessage();
        }
    }

    @GetMapping("/bookings/{id}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return receiptService.generateReceiptPdf(id, user.getId())
                .map(pdfBytes -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "parking-receipt-" + id + ".pdf");
                    headers.setContentLength(pdfBytes.length);
                    return ResponseEntity.ok().headers(headers).body(pdfBytes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehicles")
    public String manageVehicles(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        List<Vehicle> vehicles = vehicleService.findActiveUserVehicles(user.getId());
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Vehicles");
        
        return "user/vehicles";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(@RequestParam String licensePlate,
                           @RequestParam String make,
                           @RequestParam String model,
                           @RequestParam String color,
                           @RequestParam String year,
                           @RequestParam Vehicle.VehicleType vehicleType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        try {
            vehicleService.createVehicle(licensePlate, make, model, color, year, vehicleType, user);
            return "redirect:/user/vehicles?success=Vehicle added successfully";
        } catch (Exception e) {
            return "redirect:/user/vehicles?error=" + e.getMessage();
        }
    }

    @PostMapping("/vehicles/{id}/update")
    public String updateVehicle(@PathVariable Long id,
                              @RequestParam String licensePlate,
                              @RequestParam String make,
                              @RequestParam String model,
                              @RequestParam String color,
                              @RequestParam String year,
                              @RequestParam Vehicle.VehicleType vehicleType) {
        try {
            vehicleService.updateVehicle(id, licensePlate, make, model, color, year, vehicleType);
            return "redirect:/user/vehicles?success=Vehicle updated successfully";
        } catch (Exception e) {
            return "redirect:/user/vehicles?error=" + e.getMessage();
        }
    }

    @PostMapping("/vehicles/{id}/delete")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/user/vehicles?success=Vehicle deleted successfully";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        List<Booking> allBookings = bookingService.findUserBookings(user.getId());
        long totalBookings = allBookings.size();
        long activeBookings = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED || b.getStatus() == Booking.BookingStatus.ACTIVE)
                .count();
        List<Vehicle> userVehicles = vehicleService.findActiveUserVehicles(user.getId());
        int totalVehicles = userVehicles.size();
        
        model.addAttribute("user", user);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("activeBookings", activeBookings);
        model.addAttribute("totalVehicles", totalVehicles);
        model.addAttribute("pageTitle", "My Profile");
        return "user/profile";
    }
}
