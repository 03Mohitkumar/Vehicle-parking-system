package com.parking.repository;

import com.parking.entity.Booking;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUser(User user);
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByVehicle(Vehicle vehicle);
    
    List<Booking> findByParkingSlot(ParkingSlot parkingSlot);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findUserBookingsOrderedByDate(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.startTime >= :startDate AND b.startTime <= :endDate")
    List<Booking> findBookingsByStatusAndDateRange(@Param("status") Booking.BookingStatus status, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.parkingSlot.id = :slotId AND b.status IN ('CONFIRMED', 'ACTIVE')")
    List<Booking> findActiveBookingsForSlot(@Param("slotId") Long slotId);

    /** CONFIRMED bookings with no parking record yet (for staff to record entry). Eagerly fetch user, vehicle, slot for staff entries view. */
    @Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.vehicle LEFT JOIN FETCH b.parkingSlot WHERE b.status = 'CONFIRMED' AND NOT EXISTS (SELECT 1 FROM ParkingRecord pr WHERE pr.booking = b) ORDER BY b.startTime")
    List<Booking> findConfirmedBookingsWithoutRecord();

    @Query("SELECT b FROM Booking b JOIN b.parkingSlot s WHERE b.user.id = :userId AND b.status IN ('CONFIRMED', 'ACTIVE') AND s.status = 'AVAILABLE'")
    List<Booking> findActiveBookingsWithAvailableSlotForUser(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.startTime >= :startDate AND b.startTime <= :endDate ORDER BY b.startTime")
    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    long countByStatus(@Param("status") Booking.BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status AND b.startTime >= :startDate AND b.startTime <= :endDate")
    long countByStatusAndDateRange(@Param("status") Booking.BookingStatus status, 
                                  @Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(b.amount) FROM Booking b WHERE b.status = 'COMPLETED' AND b.isPaid = true AND b.startTime >= :startDate AND b.startTime <= :endDate")
    Optional<Double> calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
}
