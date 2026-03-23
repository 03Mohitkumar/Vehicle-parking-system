package com.parking.service;

import com.parking.entity.Booking;
import com.parking.entity.ParkingRecord;
import com.parking.repository.BookingRepository;
import com.parking.repository.ParkingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingRecordService {

    @Autowired
    private ParkingRecordRepository parkingRecordRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    /**
     * Bookings that are CONFIRMED and have no parking record yet.
     * Staff use these to record vehicle entry.
     */
    public List<Booking> findConfirmedBookingsForEntry() {
        return bookingRepository.findConfirmedBookingsWithoutRecord();
    }

    /**
     * Parking records with no exit time yet (vehicle still parked).
     * Staff use these to record vehicle exit.
     */
    public List<ParkingRecord> findActiveParkingRecords() {
        return parkingRecordRepository.findActiveParkingRecords();
    }

    /**
     * Record vehicle entry: create parking record and mark booking as ACTIVE, slot as OCCUPIED.
     */
    public void recordEntry(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking must be CONFIRMED to record entry");
        }
        if (booking.getParkingRecord() != null) {
            throw new RuntimeException("Entry already recorded for this booking");
        }
        bookingService.startBooking(bookingId);
        booking = bookingRepository.findById(bookingId).orElseThrow();
        ParkingRecord record = new ParkingRecord(LocalDateTime.now(), booking);
        record = parkingRecordRepository.save(record);
        booking.setParkingRecord(record);
        bookingRepository.save(booking);
    }

    /**
     * Record vehicle exit: set exit time, duration, and complete the booking (slot AVAILABLE).
     */
    public void recordExit(Long recordId) {
        ParkingRecord record = parkingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Parking record not found"));
        if (record.getExitTime() != null) {
            throw new RuntimeException("Exit already recorded for this record");
        }
        LocalDateTime now = LocalDateTime.now();
        record.setExitTime(now);
        record.calculateDuration();
        parkingRecordRepository.save(record);
        bookingService.completeBooking(record.getBooking().getId());
    }

    public Optional<ParkingRecord> findById(Long id) {
        return parkingRecordRepository.findById(id);
    }
}
