# Flight Management System

A Java-based Flight Management System that allows users to manage flights, bookings, and user accounts. The system supports different user roles (Administrator, Agent, and Customer) with specific functionalities for each role.

## Features

- User Management
  - User registration and login
  - Role-based access control (Admin, Agent, Customer)
  - Profile management

- Flight Management
  - Create and manage flights
  - Search flights by origin, destination, and date
  - View available seats and pricing

- Booking Management
  - Create and manage bookings
  - Add multiple passengers
  - Select seat classes (Economy, Business, First Class)
  - Cancel bookings

- File-based Data Storage
  - Persistent storage for users, flights, bookings, and passengers
  - Automatic file initialization with example data

## Setup Instructions

1. Ensure you have Java installed on your system
2. Clone this repository
3. Compile the Java files:
   ```
   javac src/*.java
   ```
4. Run the application:
   ```
   java -cp src Main
   ```

## Default Users

The system comes with example users:

1. Administrator
   - Username: admin
   - Password: Admin123!

2. Agent
   - Username: agent1
   - Password: Agent123!

3. Customer
   - Username: customer1
   - Password: Customer123!

## Project Structure

- `src/` - Contains all Java source files
  - `Main.java` - Main application class
  - `FileManager.java` - Handles file operations
  - `User.java` - Base user class
  - Other supporting classes

## Data Files

- `users.txt` - Stores user information
- `flights.txt` - Stores flight details
- `bookings.txt` - Stores booking information
- `passengers.txt` - Stores passenger details 