package com.parking.controller;

import com.parking.entity.ParkingSlot;
import com.parking.entity.User;
import com.parking.entity.Booking;
import com.parking.service.ParkingSlotService;
import com.parking.service.UserService;
import com.parking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();
        
        // Get statistics
        long totalSlots = parkingSlotService.findActiveSlots().size();
        long availableSlots = parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.AVAILABLE);
        long occupiedSlots = parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.OCCUPIED);
        long reservedSlots = parkingSlotService.countSlotsByStatus(ParkingSlot.SlotStatus.RESERVED);
        
        long totalUsers = userService.countUsersByRole(User.Role.USER);
        long totalStaff = userService.countUsersByRole(User.Role.STAFF);
        
        long totalBookings = bookingService.countBookingsByStatus(Booking.BookingStatus.COMPLETED);
        
        model.addAttribute("admin", admin);
        model.addAttribute("totalSlots", totalSlots);
        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("occupiedSlots", occupiedSlots);
        model.addAttribute("reservedSlots", reservedSlots);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalStaff", totalStaff);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("pageTitle", "Admin Dashboard");
        
        return "admin/dashboard";
    }

    @GetMapping("/slots")
    public String manageSlots(Model model, HttpServletRequest request) {
        List<ParkingSlot> slots = parkingSlotService.getAllActiveSlotsOrdered();
        model.addAttribute("slots", slots);
        model.addAttribute("pageTitle", "Manage Slots");
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
        return "admin/slots";
    }

    @PostMapping("/slots/create")
    public String createSlot(@RequestParam String slotNumber,
                           @RequestParam Integer floor,
                           @RequestParam ParkingSlot.SlotType slotType,
                           @RequestParam BigDecimal hourlyRate) {
        parkingSlotService.createSlot(slotNumber, floor, slotType, hourlyRate);
        return "redirect:/admin/slots";
    }

    @PostMapping("/slots/{id}/status")
    public String updateSlotStatus(@PathVariable Long id, @RequestParam ParkingSlot.SlotStatus status) {
        parkingSlotService.updateSlotStatus(id, status);
        // When slot is set to AVAILABLE, mark any active/confirmed bookings for this slot as COMPLETED
        if (status == ParkingSlot.SlotStatus.AVAILABLE) {
            bookingService.completeBookingsForSlot(id);
        }
        return "redirect:/admin/slots";
    }

    @PostMapping("/slots/{id}/block")
    public String blockSlot(@PathVariable Long id) {
        parkingSlotService.blockSlot(id);
        return "redirect:/admin/slots";
    }

    @PostMapping("/slots/{id}/unblock")
    public String unblockSlot(@PathVariable Long id) {
        parkingSlotService.unblockSlot(id);
        return "redirect:/admin/slots";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAllUsers();
        
        // Calculate statistics
        long totalUsers = users.size();
        long regularUsers = users.stream().filter(u -> u.getRole() == User.Role.USER).count();
        long staffUsers = users.stream().filter(u -> u.getRole() == User.Role.STAFF).count();
        long activeUsers = users.stream().filter(u -> Boolean.TRUE.equals(u.getIsActive())).count();
        
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("regularUsers", regularUsers);
        model.addAttribute("staffUsers", staffUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("pageTitle", "Manage Users");
        return "admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        // Get analytics data for the current month
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        
        long monthlyBookings = bookingService.countBookingsByStatusAndDateRange(
            Booking.BookingStatus.COMPLETED, startOfMonth, endOfMonth);
        
        // Get revenue data
        Double monthlyRevenue = bookingService.calculateRevenueByDateRange(startOfMonth, endOfMonth).orElse(0.0);
        
        model.addAttribute("monthlyBookings", monthlyBookings);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("pageTitle", "Analytics");
        return "admin/analytics";
    }
}
