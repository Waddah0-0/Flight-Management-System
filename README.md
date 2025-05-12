# Flight Management System

A comprehensive Java-based Flight Management System that provides a robust solution for managing airline operations. This system implements a role-based architecture supporting three distinct user types: Administrators, Agents, and Customers, each with specific functionalities tailored to their needs.

## 🚀 Features

### User Management
- **Multi-role System**
  - Administrator: Full system control and user management
  - Agent: Flight and booking management
  - Customer: Flight search and booking capabilities
- **Secure Authentication**
  - Password validation with requirements:
    - Minimum 8 characters
    - At least one uppercase letter
    - At least one lowercase letter
    - At least one number
    - At least one special character
  - Email validation
  - Session management

### Flight Management
- **Flight Operations**
  - Create and manage flight schedules
  - Set and modify flight details:
    - Flight number
    - Airline
    - Origin/Destination
    - Departure/Arrival times
    - Seat availability
  - Dynamic pricing for different seat classes
- **Seat Management**
  - Three seat classes:
    - Economy
    - Business
    - First Class
  - Real-time seat availability tracking
  - Automatic seat release on cancellation

### Booking System
- **Booking Features**
  - Multi-passenger booking support
  - Seat class selection
  - Special requests handling
  - Booking status tracking:
    - Reserved
    - Confirmed
    - Cancelled
- **Booking Management**
  - View booking history
  - Cancel bookings (with time restrictions)
  - Modify booking details
  - Calculate total prices

### Data Management
- **File-based Storage**
  - Persistent data storage in text files
  - Automatic file initialization
  - Data integrity checks
- **Data Files**
  - users.txt: User profiles and credentials
  - flights.txt: Flight schedules and details
  - bookings.txt: Booking records
  - passengers.txt: Passenger information

## 🛠️ Technical Details

### System Architecture
- **Object-Oriented Design**
  - Inheritance hierarchy for user types
  - Encapsulated data management
  - Interface-based operations
- **Data Flow**
  - File-based persistence
  - In-memory caching
  - Real-time updates

### Code Structure
```
src/
├── Main.java                 # Application entry point
├── FileManager.java          # File operations handler
├── User.java                 # Base user class
├── Customer.java            # Customer implementation
├── Agent.java               # Agent implementation
├── Administrator.java       # Administrator implementation
├── Flight.java              # Flight management
├── Booking.java             # Booking operations
├── Passenger.java           # Passenger information
├── UserRole.java            # User role enumeration
├── SeatClass.java           # Seat class enumeration
└── BookingStatus.java       # Booking status enumeration
```

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- Basic understanding of Java programming
- Command-line interface familiarity

## 🚀 Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Waddah0-0/Flight-Management-System.git
   cd Flight-Management-System
   ```

2. **Compile the Project**
   ```bash
   javac src/*.java
   ```

3. **Run the Application**
   ```bash
   java -cp src Main
   ```

## 👥 Default Users

### Administrator
- Username: `admin`
- Password: `Admin123!`
- Capabilities: Full system control

### Agent
- Username: `agent1`
- Password: `Agent123!`
- Capabilities: Flight and booking management

### Customer
- Username: `customer1`
- Password: `Customer123!`
- Capabilities: Flight search and booking

## 🔒 Security Features

- Password encryption
- Input validation
- Session management
- Role-based access control
- Secure file handling

## 📝 File Formats

### users.txt
```
username|password|name|email|contactInfo|address|role
```

### flights.txt
```
flightNumber|airline|origin|destination|departureTime|arrivalTime|economyPrice|businessPrice|firstClassPrice|economySeats|businessSeats|firstClassSeats
```

### bookings.txt
```
bookingId|customerUsername|flightNumber|status|seatClass|totalPrice|bookingTime
```

### passengers.txt
```
bookingId|firstName|lastName|passportNumber|nationality|dateOfBirth
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

Waddah0-0
- GitHub: [@Waddah0-0](https://github.com/Waddah0-0) 