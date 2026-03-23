# Parking Management System - Database Files

This directory contains SQL files for setting up the Parking Management System database.

## Files Overview

### H2 Database (Development)
- `../src/main/resources/schema.sql` - H2 table creation only
- `../src/main/resources/data.sql` - H2 schema + sample data

### MySQL Database (Production)
- `mysql_schema.sql` - Complete MySQL schema with indexes
- `mysql_sample_data.sql` - Sample data for MySQL

### PostgreSQL Database (Production)
- `postgresql_schema.sql` - Complete PostgreSQL schema with indexes and triggers

## Quick Start

### For Development (H2)
The application automatically creates the database using the files in `src/main/resources/`. No manual setup required.

### For Production (MySQL)

1. **Create Database:**
```sql
CREATE DATABASE parking_management;
USE parking_management;
```

2. **Run Schema:**
```bash
mysql -u username -p parking_management < mysql_schema.sql
```

3. **Load Sample Data:**
```bash
mysql -u username -p parking_management < mysql_sample_data.sql
```

4. **Update application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/parking_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
```

### For Production (PostgreSQL)

1. **Create Database:**
```sql
CREATE DATABASE parking_management;
\c parking_management;
```

2. **Run Schema:**
```bash
psql -U username -d parking_management -f postgresql_schema.sql
```

3. **Load Sample Data:**
```bash
psql -U username -d parking_management -f mysql_sample_data.sql
```

4. **Update application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
```

## Database Schema

### Tables

1. **users** - User accounts and authentication
2. **vehicles** - Vehicle information linked to users
3. **parking_slots** - Available parking spaces
4. **bookings** - Parking slot reservations
5. **parking_records** - Entry/exit logs
6. **payments** - Payment transactions

### Key Features

- **Foreign Key Constraints** - Maintains data integrity
- **Indexes** - Optimized for common queries
- **Timestamps** - Automatic created_at/updated_at tracking
- **Soft Deletes** - is_active flags for data retention
- **Enums** - Type safety for status fields

## Sample Data

The sample data includes:
- 5 users (1 admin, 1 staff, 3 regular users)
- 32 parking slots across 5 floors
- 7 vehicles
- 8 bookings (various statuses)
- 5 parking records
- 8 payment records

## Default Credentials

- **Admin**: username `admin`, password `password123`
- **Staff**: username `staff1`, password `password123`
- **Users**: username `user1`, `user2`, `user3`, password `password123`

## Security Notes

- All passwords are BCrypt hashed
- Change default passwords in production
- Use environment variables for database credentials
- Enable SSL for production database connections
