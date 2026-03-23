package com.parking.repository;

import com.parking.entity.ParkingRecord;
import com.parking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    
    Optional<ParkingRecord> findByBooking(Booking booking);
    
    List<ParkingRecord> findByBookingUserId(Long userId);
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    List<ParkingRecord> findRecordsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.booking.user.id = :userId ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findUserRecordsOrderedByDate(@Param("userId") Long userId);
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.exitTime IS NULL")
    List<ParkingRecord> findActiveParkingRecords();
    
    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    long countRecordsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(pr.durationMinutes) FROM ParkingRecord pr WHERE pr.exitTime IS NOT NULL AND pr.entryTime >= :startDate AND pr.entryTime <= :endDate")
    Optional<Double> calculateAverageParkingDuration(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
}
