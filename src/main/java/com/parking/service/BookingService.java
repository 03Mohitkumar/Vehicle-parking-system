package com.parking.service;

import com.parking.entity.Booking;
import com.parking.entity.ParkingSlot;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSlotService parkingSlotService;

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> findUserBookings(Long userId) {
        return bookingRepository.findUserBookingsOrderedByDate(userId);
    }

    public List<Booking> findBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> findBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findBookingsByDateRange(startDate, endDate);
    }

    public List<Booking> findBookingsByStatusAndDateRange(Booking.BookingStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findBookingsByStatusAndDateRange(status, startDate, endDate);
    }

    public List<Booking> findActiveBookingsForSlot(Long slotId) {
        return bookingRepository.findActiveBookingsForSlot(slotId);
    }

    public Booking createBooking(User user, Vehicle vehicle, ParkingSlot slot, LocalDateTime startTime, LocalDateTime expectedEndTime) {
        // Check if slot is available
        if (slot.getStatus() != ParkingSlot.SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot is not available for booking");
        }

        // Calculate amount based on hourly rate and duration
        long hours = java.time.Duration.between(startTime, expectedEndTime).toHours();
        if (hours < 1) hours = 1; // Minimum 1 hour
        BigDecimal amount = slot.getHourlyRate().multiply(BigDecimal.valueOf(hours));

        Booking booking = new Booking(startTime, expectedEndTime, amount, user, vehicle, slot);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        // Update slot status to reserved
        slot.setStatus(ParkingSlot.SlotStatus.RESERVED);
        parkingSlotService.saveSlot(slot);

        return saveBooking(booking);
    }

    public Booking confirmBooking(Long bookingId) {
        Optional<Booking> bookingOpt = findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            return saveBooking(booking);
        }
        throw new RuntimeException("Booking not found");
    }

    public Booking startBooking(Long bookingId) {
        Optional<Booking> bookingOpt = findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.ACTIVE);
            
            // Update slot status to occupied
            ParkingSlot slot = booking.getParkingSlot();
            slot.setStatus(ParkingSlot.SlotStatus.OCCUPIED);
            parkingSlotService.saveSlot(slot);
            
            return saveBooking(booking);
        }
        throw new RuntimeException("Booking not found");
    }

    public Booking completeBooking(Long bookingId) {
        Optional<Booking> bookingOpt = findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.COMPLETED);
            booking.setEndTime(LocalDateTime.now());
            
            // Update slot status to available
            ParkingSlot slot = booking.getParkingSlot();
            slot.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
            parkingSlotService.saveSlot(slot);
            
            return saveBooking(booking);
        }
        throw new RuntimeException("Booking not found");
    }

    public Booking cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            
            // Update slot status to available
            ParkingSlot slot = booking.getParkingSlot();
            slot.setStatus(ParkingSlot.SlotStatus.AVAILABLE);
            parkingSlotService.saveSlot(slot);
            
            return saveBooking(booking);
        }
        throw new RuntimeException("Booking not found");
    }

    /**
     * Mark all CONFIRMED/ACTIVE bookings for the given slot as COMPLETED.
     * Call this when a slot is set to AVAILABLE (e.g. by admin or when vehicle exits)
     * so that "Recent Bookings" shows correct status.
     */
    public void completeBookingsForSlot(Long slotId) {
        List<Booking> activeBookings = findActiveBookingsForSlot(slotId);
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : activeBookings) {
            booking.setStatus(Booking.BookingStatus.COMPLETED);
            if (booking.getEndTime() == null) {
                booking.setEndTime(now);
            }
            saveBooking(booking);
        }
    }

    /**
     * Sync booking status with slot: if a user's booking is CONFIRMED/ACTIVE but the slot
     * is now AVAILABLE, mark the booking as COMPLETED. Call when loading user dashboard
     * so "Recent Bookings" shows correct status for existing data.
     */
    public void syncBookingStatusWithSlotForUser(Long userId) {
        List<Booking> toComplete = bookingRepository.findActiveBookingsWithAvailableSlotForUser(userId);
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : toComplete) {
            booking.setStatus(Booking.BookingStatus.COMPLETED);
            if (booking.getEndTime() == null) {
                booking.setEndTime(now);
            }
            saveBooking(booking);
        }
    }

    public long countBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }

    public long countBookingsByStatusAndDateRange(Booking.BookingStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.countByStatusAndDateRange(status, startDate, endDate);
    }

    public Optional<Double> calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.calculateRevenueByDateRange(startDate, endDate);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
