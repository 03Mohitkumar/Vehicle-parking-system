-- Parking Management System Database Schema
-- This file contains the SQL schema and initial data for the application

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    color VARCHAR(30) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    vehicle_year VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create Parking Slots table
CREATE TABLE IF NOT EXISTS parking_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_number VARCHAR(20) NOT NULL UNIQUE,
    floor INTEGER NOT NULL,
    slot_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    hourly_rate DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    expected_end_time TIMESTAMP NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_paid BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    parking_slot_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    FOREIGN KEY (parking_slot_id) REFERENCES parking_slots(id) ON DELETE CASCADE
);

-- Create Parking Records table
CREATE TABLE IF NOT EXISTS parking_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    vehicle_id BIGINT NOT NULL,
    parking_slot_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    FOREIGN KEY (parking_slot_id) REFERENCES parking_slots(id) ON DELETE CASCADE
);

-- Create Payments table
CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    booking_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

-- Insert sample users
INSERT INTO users (username, email, password, first_name, last_name, role, is_active) VALUES
('admin', 'admin@parking.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'ADMIN', TRUE),
('staff1', 'staff1@parking.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Staff', 'Member', 'STAFF', TRUE),
('user1', 'user1@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Doe', 'USER', TRUE),
('user2', 'user2@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', 'USER', TRUE);

-- Insert sample parking slots
INSERT INTO parking_slots (slot_number, floor, slot_type, status, hourly_rate, is_active) VALUES
('A1', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A2', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A3', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A4', 1, 'COMPACT', 'AVAILABLE', 5.00, TRUE),
('A5', 1, 'COMPACT', 'MAINTENANCE', 5.00, TRUE),
('B1', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B2', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B3', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B4', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('B5', 2, 'STANDARD', 'AVAILABLE', 7.50, TRUE),
('C1', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C2', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C3', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C4', 3, 'LARGE', 'AVAILABLE', 10.00, TRUE),
('C5', 3, 'LARGE', 'BLOCKED', 10.00, TRUE);

-- Insert sample vehicles
INSERT INTO vehicles (license_plate, make, model, color, vehicle_type, vehicle_year, user_id, is_active) VALUES
('ABC123', 'Toyota', 'Camry', 'Blue', 'SEDAN', '2020', 3, TRUE),
('XYZ789', 'Honda', 'Civic', 'Red', 'SEDAN', '2019', 3, TRUE),
('DEF456', 'Ford', 'F-150', 'Black', 'TRUCK', '2021', 4, TRUE),
('GHI789', 'BMW', 'X5', 'White', 'SUV', '2022', 4, TRUE);

-- Insert sample bookings
INSERT INTO bookings (start_time, end_time, expected_end_time, amount, status, is_paid, user_id, vehicle_id, parking_slot_id) VALUES
('2024-01-15 09:00:00', '2024-01-15 17:00:00', '2024-01-15 17:00:00', 40.00, 'COMPLETED', TRUE, 3, 1, 1),
('2024-01-15 10:30:00', '2024-01-15 15:30:00', '2024-01-15 15:30:00', 37.50, 'COMPLETED', TRUE, 4, 2, 2),
('2024-01-16 08:00:00', NULL, '2024-01-16 18:00:00', 75.00, 'ACTIVE', FALSE, 3, 1, 3),
('2024-01-16 11:00:00', NULL, '2024-01-16 16:00:00', 37.50, 'CONFIRMED', FALSE, 4, 2, 4);

-- Insert sample parking records
INSERT INTO parking_records (entry_time, exit_time, vehicle_id, parking_slot_id) VALUES
('2024-01-15 09:00:00', '2024-01-15 17:00:00', 1, 1),
('2024-01-15 10:30:00', '2024-01-15 15:30:00', 2, 2),
('2024-01-16 08:00:00', NULL, 1, 3);

-- Insert sample payments
INSERT INTO payments (amount, payment_method, transaction_id, status, booking_id) VALUES
(40.00, 'CREDIT_CARD', 'TXN001', 'COMPLETED', 1),
(37.50, 'DEBIT_CARD', 'TXN002', 'COMPLETED', 2),
(75.00, 'CASH', 'TXN003', 'PENDING', 3),
(37.50, 'ONLINE', 'TXN004', 'PENDING', 4);
