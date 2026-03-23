-- Parking Management System - MySQL Sample Data
-- This file contains sample data for testing and development

-- Insert sample users (passwords are BCrypt encoded for 'password123')
INSERT INTO users (username, email, password, first_name, last_name, role, is_active) VALUES
('admin', 'admin@parking.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'ADMIN', TRUE),
('staff1', 'staff1@parking.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Staff', 'Member', 'STAFF', TRUE),
('user1', 'user1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Doe', 'USER', TRUE),
('user2', 'user2@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', 'USER', TRUE),
('user3', 'user3@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Mike', 'Johnson', 'USER', TRUE);

-- Insert sample parking slots
INSERT INTO parking_slots (slot_number, floor, slot_type, status, hourly_rate, is_active) VALUES
-- Floor 1 - Compact slots
('A1', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A2', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A3', 1, 'COMPACT', 'OCCUPIED', 5.00, TRUE),
('A4', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A5', 1, 'COMPACT', 'MAINTENANCE', 5.00, TRUE),
('A6', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A7', 1, 'COMPACT', 'RESERVED', 5.00, TRUE),
('A8', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),

-- Floor 2 - Standard slots
('B1', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B2', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B3', 2, 'STANDARD', 'OCCUPIED', 7.50, TRUE),
('B4', 2, 'STANDARD', 'RESERVED', 7.50, TRUE),
('B5', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B6', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B7', 2, 'STANDARD', 'OCCUPIED', 7.50, TRUE),
('B8', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),

-- Floor 3 - Large slots
('C1', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C2', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C3', 3, 'LARGE', 'OCCUPIED', 10.00, TRUE),
('C4', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C5', 3, 'LARGE', 'BLOCKED', 10.00, TRUE),
('C6', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C7', 3, 'LARGE', 'RESERVED', 10.00, TRUE),
('C8', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),

-- Floor 4 - Handicap slots
('D1', 4, 'HANDICAP', 'AVAILABLE', 3.00, TRUE),
('D2', 4, 'HANDICAP', 'AVAILABLE', 3.00, TRUE),
('D3', 4, 'HANDICAP', 'OCCUPIED', 3.00, TRUE),
('D4', 4, 'HANDICAP', 'AVAILABLE', 3.00, TRUE),

-- Floor 5 - Electric vehicle slots
('E1', 5, 'ELECTRIC', 'AVAILABLE', 8.00, TRUE),
('E2', 5, 'ELECTRIC', 'AVAILABLE', 8.00, TRUE),
('E3', 5, 'ELECTRIC', 'OCCUPIED', 8.00, TRUE),
('E4', 5, 'ELECTRIC', 'AVAILABLE', 8.00, TRUE);

-- Insert sample vehicles
INSERT INTO vehicles (license_plate, make, model, color, vehicle_type, vehicle_year, user_id, is_active) VALUES
('ABC123', 'Toyota', 'Camry', 'Blue', 'SEDAN', '2020', 3, TRUE),
('XYZ789', 'Honda', 'Civic', 'Red', 'SEDAN', '2019', 3, TRUE),
('DEF456', 'Ford', 'F-150', 'Black', 'TRUCK', '2021', 4, TRUE),
('GHI789', 'BMW', 'X5', 'White', 'SUV', '2022', 4, TRUE),
('JKL012', 'Tesla', 'Model 3', 'Silver', 'SEDAN', '2023', 5, TRUE),
('MNO345', 'Audi', 'Q7', 'Gray', 'SUV', '2021', 5, TRUE),
('PQR678', 'Mercedes', 'C-Class', 'Black', 'SEDAN', '2022', 3, TRUE);

-- Insert sample bookings
INSERT INTO bookings (start_time, end_time, expected_end_time, amount, status, is_paid, user_id, vehicle_id, parking_slot_id) VALUES
-- Completed bookings
('2024-01-15 09:00:00', '2024-01-15 17:00:00', '2024-01-15 17:00:00', 40.00, 'COMPLETED', TRUE, 3, 1, 1),
('2024-01-15 10:30:00', '2024-01-15 15:30:00', '2024-01-15 15:30:00', 37.50, 'COMPLETED', TRUE, 4, 2, 2),
('2024-01-16 08:00:00', '2024-01-16 18:00:00', '2024-01-16 18:00:00', 75.00, 'COMPLETED', TRUE, 5, 3, 3),

-- Active bookings
('2024-01-17 08:00:00', NULL, '2024-01-17 18:00:00', 75.00, 'ACTIVE', FALSE, 3, 1, 4),
('2024-01-17 09:30:00', NULL, '2024-01-17 17:30:00', 60.00, 'ACTIVE', FALSE, 4, 2, 5),

-- Confirmed bookings
('2024-01-18 10:00:00', NULL, '2024-01-18 16:00:00', 45.00, 'CONFIRMED', FALSE, 5, 3, 6),
('2024-01-18 11:00:00', NULL, '2024-01-18 15:00:00', 30.00, 'CONFIRMED', FALSE, 3, 4, 7),

-- Pending bookings
('2024-01-19 09:00:00', NULL, '2024-01-19 17:00:00', 40.00, 'PENDING', FALSE, 4, 5, 8);

-- Insert sample parking records
INSERT INTO parking_records (entry_time, exit_time, vehicle_id, parking_slot_id) VALUES
-- Completed parking records
('2024-01-15 09:00:00', '2024-01-15 17:00:00', 1, 1),
('2024-01-15 10:30:00', '2024-01-15 15:30:00', 2, 2),
('2024-01-16 08:00:00', '2024-01-16 18:00:00', 3, 3),

-- Active parking records (no exit time)
('2024-01-17 08:00:00', NULL, 1, 4),
('2024-01-17 09:30:00', NULL, 2, 5);

-- Insert sample payments
INSERT INTO payments (amount, payment_method, transaction_id, status, booking_id) VALUES
-- Completed payments
(40.00, 'CREDIT_CARD', 'TXN001', 'COMPLETED', 1),
(37.50, 'DEBIT_CARD', 'TXN002', 'COMPLETED', 2),
(75.00, 'CASH', 'TXN003', 'COMPLETED', 3),

-- Pending payments
(75.00, 'ONLINE', 'TXN004', 'PENDING', 4),
(60.00, 'MOBILE', 'TXN005', 'PENDING', 5),
(45.00, 'CREDIT_CARD', 'TXN006', 'PENDING', 6),
(30.00, 'DEBIT_CARD', 'TXN007', 'PENDING', 7),
(40.00, 'ONLINE', 'TXN008', 'PENDING', 8);
