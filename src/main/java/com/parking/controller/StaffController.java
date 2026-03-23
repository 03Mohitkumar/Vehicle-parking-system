package com.parking.controller;

import com.parking.entity.Booking;
import com.parking.entity.ParkingRecord;
import com.parking.entity.User;
import com.parking.service.ParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private ParkingRecordService parkingRecordService;

    @GetMapping("/dashboard")
    public String staffDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User staff = (User) authentication.getPrincipal();
        List<Booking> confirmedBookings = parkingRecordService.findConfirmedBookingsForEntry();
        int activeRecordsCount = parkingRecordService.findActiveParkingRecords().size();
        model.addAttribute("user", staff);
        model.addAttribute("confirmedBookings", confirmedBookings);
        model.addAttribute("awaitingEntryCount", confirmedBookings.size());
        model.addAttribute("activeRecordsCount", activeRecordsCount);
        model.addAttribute("pageTitle", "Staff Dashboard");
        return "staff/dashboard";
    }

    @GetMapping("/entries")
    public String vehicleEntries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User staff = (User) authentication.getPrincipal();
        List<Booking> confirmedBookings = parkingRecordService.findConfirmedBookingsForEntry();
        model.addAttribute("user", staff);
        model.addAttribute("confirmedBookings", confirmedBookings);
        model.addAttribute("pageTitle", "Vehicle Entries");
        return "staff/entries";
    }

    @PostMapping("/entries/record")
    public String recordEntry(@RequestParam Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            parkingRecordService.recordEntry(bookingId);
            redirectAttributes.addFlashAttribute("success", "Vehicle entry recorded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/entries";
    }

    @GetMapping("/exits")
    public String vehicleExits(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User staff = (User) authentication.getPrincipal();
        List<ParkingRecord> activeRecords = parkingRecordService.findActiveParkingRecords();
        model.addAttribute("user", staff);
        model.addAttribute("activeRecords", activeRecords);
        model.addAttribute("pageTitle", "Vehicle Exits");
        return "staff/exits";
    }

    @PostMapping("/exits/record")
    public String recordExit(@RequestParam Long recordId, RedirectAttributes redirectAttributes) {
        try {
            parkingRecordService.recordExit(recordId);
            redirectAttributes.addFlashAttribute("success", "Vehicle exit recorded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/exits";
    }
}
