# Vehicle Parking Management System

A comprehensive Spring Boot web application for managing parking spaces, vehicle entries/exits, online bookings, and analytical reports with role-based access control.

## Features

### 🚗 Core Features
- **Real-time Slot Monitoring**: Live parking space status updates with visual indicators
- **Online Booking System**: Interactive booking interface with slot filtering
- **Role-based Dashboards**: Separate interfaces for Admin, Staff, and Users
- **Vehicle Management**: Register and manage multiple vehicles per user
- **Payment Integration**: Support for multiple payment methods
- **Analytics & Reporting**: Revenue tracking and usage analytics

### 👥 User Roles
- **Admin**: Complete system control, user management, analytics
- **Staff**: Vehicle entry/exit management, slot assignments
- **User**: Booking management, vehicle registration, profile management

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 11
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript, Chart.js
- **Database**: H2 (in-memory) for development, MySQL for production
- **Security**: Spring Security with form-based authentication
- **Build Tool**: Maven

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ParkingManagement
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open your browser and go to `http://localhost:8080`
   - The application will redirect you to the login page

### Demo Accounts

The application comes with pre-configured demo accounts:

| Role | Username | Password | Description |
|------|----------|----------|-------------|
| Admin | admin | admin123 | Full system access |
| Staff | staff | staff123 | Vehicle entry/exit management |
| User | user1 | user123 | Regular user account |
| User | user2 | user123 | Another user account |

### Sample Data

The application automatically initializes with:
- 78 parking slots across 3 floors
- Different slot types (Standard, Premium, Disabled, EV Charging)
- Sample vehicles for demo users
- Various slot statuses for demonstration

## Application Structure

```
src/main/java/com/parking/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java # Spring Security configuration
│   └── DataInitializer.java # Sample data initialization
├── controller/             # REST and web controllers
│   ├── AuthController.java # Authentication endpoints
│   ├── AdminController.java # Admin dashboard
│   ├── UserController.java # User dashboard
│   └── ApiController.java  # REST API endpoints
├── entity/                 # JPA entities
│   ├── User.java          # User entity with roles
│   ├── Vehicle.java       # Vehicle information
│   ├── ParkingSlot.java   # Parking slot details
│   ├── Booking.java       # Booking records
│   ├── ParkingRecord.java # Entry/exit logs
│   └── Payment.java       # Payment information
├── repository/             # Data access layer
│   ├── UserRepository.java
│   ├── VehicleRepository.java
│   ├── ParkingSlotRepository.java
│   ├── BookingRepository.java
│   ├── ParkingRecordRepository.java
│   └── PaymentRepository.java
└── service/                # Business logic layer
    ├── UserService.java
    ├── VehicleService.java
    ├── ParkingSlotService.java
    └── BookingService.java
```

## Key Endpoints

### Web Pages
- `/` - Home page (redirects to login)
- `/login` - User login
- `/signup` - User registration
- `/dashboard` - Role-based dashboard
- `/admin/*` - Admin management pages
- `/user/*` - User management pages

### REST API
- `GET /api/slots` - Get all parking slots
- `GET /api/slots/available` - Get available slots
- `GET /api/slots/status` - Get slot status counts
- `POST /api/slots/{id}/status` - Update slot status

## Database Schema

The application uses the following main entities:

- **users**: User accounts with role-based access
- **vehicles**: Vehicle information linked to users
- **parking_slots**: Parking space definitions
- **bookings**: Reservation and booking records
- **parking_records**: Entry/exit timestamps
- **payments**: Payment transaction records

## Features by Role

### Admin Dashboard
- View system statistics and analytics
- Manage parking slots (create, update, block)
- User management and role assignment
- Revenue and usage reports
- Real-time slot monitoring

### Staff Dashboard
- Vehicle entry and exit management
- Manual slot assignments
- Print entry/exit receipts
- View current occupancy

### User Dashboard
- Search and book available slots
- Manage vehicle registrations
- View booking history
- Profile management
- Download receipts

## Development

### Adding New Features

1. **Entities**: Add new JPA entities in the `entity` package
2. **Repositories**: Create repository interfaces extending `JpaRepository`
3. **Services**: Implement business logic in the `service` package
4. **Controllers**: Add web or REST controllers
5. **Templates**: Create Thymeleaf templates in `src/main/resources/templates`

### Database Configuration

For production, update `application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/parking_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

