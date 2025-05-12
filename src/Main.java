import java.util.Scanner;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static List<User> users;
    private static List<Flight> flights;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        try {
            FileManager.initializeFiles();
            users = FileManager.loadUsers();
            if (users.isEmpty()) {
                System.err.println("Error: No users loaded. Please check users.txt file.");
                return;
            }
            flights = FileManager.loadFlights();
            
            boolean running = true;
            while (running) {
                if (currentUser == null) {
                    showLoginMenu();
                } else {
                    showUserMenu();
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing system: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Flight Booking Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n=== Welcome, " + currentUser.getName() + " ===");
        
        switch (currentUser.getRole()) {
            case CUSTOMER:
                showCustomerMenu();
                break;
            case AGENT:
                showAgentMenu();
                break;
            case ADMINISTRATOR:
                showAdminMenu();
                break;
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                try {
                    if (user.login(username, password)) {
                        currentUser = user;
                        System.out.println("Login successful!");
                        return;
                    } else {
                        System.out.println("Invalid password. Please try again.");
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Login error: " + e.getMessage());
                    return;
                }
            }
        }
        System.out.println("Username not found. Please try again.");
    }

    private static void register() {
        System.out.println("\n=== Registration ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        try {
            Customer newCustomer = new Customer(username, password, name, email, contactInfo, address);
            FileManager.saveUser(newCustomer);
            users.add(newCustomer);
            System.out.println("Registration successful! Please login.");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void showCustomerMenu() {
        System.out.println("\n=== Customer Menu ===");
        System.out.println("1. Search Flights");
        System.out.println("2. View Bookings");
        System.out.println("3. Update Profile");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                searchFlights();
                break;
            case 2:
                viewBookings();
                break;
            case 3:
                updateProfile();
                break;
            case 4:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void searchFlights() {
        System.out.println("\n=== Search Flights ===");
        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        try {
            LocalDateTime date = LocalDateTime.parse(dateStr + " 00:00", dateFormatter);
            List<Flight> availableFlights = ((Customer) currentUser).searchFlights(origin, destination, dateStr);
            
            if (availableFlights.isEmpty()) {
                System.out.println("No flights found for the given criteria.");
            } else {
                System.out.println("\nAvailable Flights:");
                for (Flight flight : availableFlights) {
                    System.out.println("Flight: " + flight.getFlightNumber());
                    System.out.println("Airline: " + flight.getAirline());
                    System.out.println("Departure: " + flight.getDepartureTime().format(dateFormatter));
                    System.out.println("Arrival: " + flight.getArrivalTime().format(dateFormatter));
                    System.out.println("Price: $" + flight.getPrice(SeatClass.ECONOMY));
                    System.out.println("------------------------");
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private static void viewBookings() {
        Customer customer = (Customer) currentUser;
        List<Booking> bookings = FileManager.loadBookings(users, flights).stream()
            .filter(b -> b.getCustomer().getUsername().equals(customer.getUsername()))
            .collect(Collectors.toList());
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n=== Your Bookings ===");
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Flight: " + booking.getFlight().getFlightNumber());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("Seat Class: " + booking.getSeatClass());
            System.out.println("Total Price: $" + booking.calculateTotalPrice());
            
            // Load and display passengers
            List<Passenger> passengers = FileManager.loadPassengers(booking.getBookingId());
            System.out.println("Passengers:");
            for (Passenger passenger : passengers) {
                System.out.println("  - " + passenger.getFirstName() + " " + passenger.getLastName());
            }
            System.out.println("------------------------");
        }

        System.out.println("\n1. Cancel Booking");
        System.out.println("2. Return to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice == 1) {
            System.out.print("Enter booking ID to cancel: ");
            String bookingId = scanner.nextLine();
            try {
                customer.cancelBooking(bookingId);
                // Update booking status in file
                FileManager.saveBooking(customer.getBookingHistory().stream()
                    .filter(b -> b.getBookingId().equals(bookingId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found")));
                System.out.println("Booking cancelled successfully.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void updateProfile() {
        Customer customer = (Customer) currentUser;
        System.out.println("\n=== Update Profile ===");
        System.out.println("1. Update Name");
        System.out.println("2. Update Email");
        System.out.println("3. Update Contact Info");
        System.out.println("4. Update Address");
        System.out.println("5. Update Password");
        System.out.println("6. Return to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        try {
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    customer.setName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    customer.setEmail(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter new contact info: ");
                    customer.setContactInfo(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Enter new address: ");
                    customer.setAddress(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Enter new password: ");
                    customer.setPassword(scanner.nextLine());
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            customer.updateProfile();
            System.out.println("Profile updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showAgentMenu() {
        System.out.println("\n=== Agent Menu ===");
        System.out.println("1. Manage Flights");
        System.out.println("2. Create Booking");
        System.out.println("3. View Bookings");
        System.out.println("4. Generate Reports");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                manageFlights();
                break;
            case 2:
                createBooking();
                break;
            case 3:
                viewAgentBookings();
                break;
            case 4:
                generateReports();
                break;
            case 5:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void manageFlights() {
        System.out.println("\n=== Manage Flights ===");
        System.out.println("1. Create New Flight");
        System.out.println("2. View All Flights");
        System.out.println("3. Return to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                createFlight();
                break;
            case 2:
                viewAllFlights();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void createFlight() {
        System.out.println("\n=== Create New Flight ===");
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        System.out.print("Enter airline: ");
        String airline = scanner.nextLine();
        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter departure time (yyyy-MM-dd HH:mm): ");
        String departureStr = scanner.nextLine();
        System.out.print("Enter arrival time (yyyy-MM-dd HH:mm): ");
        String arrivalStr = scanner.nextLine();

        try {
            LocalDateTime departureTime = LocalDateTime.parse(departureStr, dateFormatter);
            LocalDateTime arrivalTime = LocalDateTime.parse(arrivalStr, dateFormatter);

            Flight flight = ((Agent) currentUser).createFlight(flightNumber, airline, origin, 
                                                             destination, departureTime, arrivalTime);
            
            // Set initial seat prices
            System.out.print("Enter economy class price: $");
            flight.setPrice(SeatClass.ECONOMY, scanner.nextDouble());
            System.out.print("Enter business class price: $");
            flight.setPrice(SeatClass.BUSINESS, scanner.nextDouble());
            System.out.print("Enter first class price: $");
            flight.setPrice(SeatClass.FIRST_CLASS, scanner.nextDouble());

            // Set initial seat availability
            System.out.print("Enter number of economy seats: ");
            flight.setAvailableSeats(SeatClass.ECONOMY, scanner.nextInt());
            System.out.print("Enter number of business seats: ");
            flight.setAvailableSeats(SeatClass.BUSINESS, scanner.nextInt());
            System.out.print("Enter number of first class seats: ");
            flight.setAvailableSeats(SeatClass.FIRST_CLASS, scanner.nextInt());

            flights.add(flight);
            FileManager.saveFlight(flight);
            System.out.println("Flight created successfully!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllFlights() {
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }

        System.out.println("\n=== Available Flights ===");
        for (Flight flight : flights) {
            System.out.println("Flight: " + flight.getFlightNumber());
            System.out.println("Airline: " + flight.getAirline());
            System.out.println("Route: " + flight.getOrigin() + " -> " + flight.getDestination());
            System.out.println("Departure: " + flight.getDepartureTime().format(dateFormatter));
            System.out.println("Arrival: " + flight.getArrivalTime().format(dateFormatter));
            System.out.println("Available Seats:");
            System.out.println("  Economy: " + flight.getAvailableSeats(SeatClass.ECONOMY));
            System.out.println("  Business: " + flight.getAvailableSeats(SeatClass.BUSINESS));
            System.out.println("  First Class: " + flight.getAvailableSeats(SeatClass.FIRST_CLASS));
            System.out.println("------------------------");
        }
    }

    private static void createBooking() {
        System.out.println("\n=== Create Booking ===");
        viewAllFlights();
        
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        
        Flight selectedFlight = flights.stream()
            .filter(f -> f.getFlightNumber().equals(flightNumber))
            .findFirst()
            .orElse(null);

        if (selectedFlight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.print("Enter customer username: ");
        String customerUsername = scanner.nextLine();
        
        Customer customer = (Customer) users.stream()
            .filter(u -> u.getRole() == UserRole.CUSTOMER && u.getUsername().equals(customerUsername))
            .findFirst()
            .orElse(null);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("Enter number of passengers: ");
        int passengerCount = scanner.nextInt();
        scanner.nextLine();

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < passengerCount; i++) {
            System.out.println("\nPassenger " + (i + 1) + ":");
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter passport number: ");
            String passport = scanner.nextLine();
            System.out.print("Enter nationality: ");
            String nationality = scanner.nextLine();
            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            String dateOfBirth = scanner.nextLine();
            passengers.add(new Passenger(firstName, lastName, passport, nationality, dateOfBirth));
        }

        System.out.println("\nSelect seat class:");
        System.out.println("1. Economy");
        System.out.println("2. Business");
        System.out.println("3. First Class");
        System.out.print("Enter your choice: ");
        
        int seatChoice = scanner.nextInt();
        scanner.nextLine(); 

        SeatClass seatClass;
        switch (seatChoice) {
            case 1: seatClass = SeatClass.ECONOMY; break;
            case 2: seatClass = SeatClass.BUSINESS; break;
            case 3: seatClass = SeatClass.FIRST_CLASS; break;
            default:
                System.out.println("Invalid choice. Defaulting to Economy.");
                seatClass = SeatClass.ECONOMY;
        }

        try {
            Booking booking = ((Agent) currentUser).createBookingForCustomer(
                customer, selectedFlight, passengers, seatClass);
            
            // Save booking and passengers
            FileManager.saveBooking(booking);
            for (Passenger passenger : passengers) {
                FileManager.savePassenger(passenger, booking.getBookingId());
            }
            
            System.out.println("Booking created successfully!");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Total Price: $" + booking.calculateTotalPrice());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAgentBookings() {
        Agent agent = (Agent) currentUser;
        List<Booking> bookings = agent.getManagedBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n=== Managed Bookings ===");
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer: " + booking.getCustomer().getName());
            System.out.println("Flight: " + booking.getFlight().getFlightNumber());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("Seat Class: " + booking.getSeatClass());
            System.out.println("Total Price: $" + booking.getTotalPrice());
            System.out.println("------------------------");
        }

        System.out.println("\n1. Modify Booking");
        System.out.println("2. Return to Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice == 1) {
            System.out.print("Enter booking ID to modify: ");
            String bookingId = scanner.nextLine();
            
            Booking bookingToModify = bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

            if (bookingToModify == null) {
                System.out.println("Booking not found.");
                return;
            }

            System.out.println("\nSelect new seat class:");
            System.out.println("1. Economy");
            System.out.println("2. Business");
            System.out.println("3. First Class");
            System.out.print("Enter your choice: ");
            
            int seatChoice = scanner.nextInt();
            scanner.nextLine(); 

            SeatClass newSeatClass;
            switch (seatChoice) {
                case 1: newSeatClass = SeatClass.ECONOMY; break;
                case 2: newSeatClass = SeatClass.BUSINESS; break;
                case 3: newSeatClass = SeatClass.FIRST_CLASS; break;
                default:
                    System.out.println("Invalid choice. Operation cancelled.");
                    return;
            }

            try {
                agent.modifyBooking(bookingToModify, newSeatClass);
                System.out.println("Booking modified successfully!");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void generateReports() {
        System.out.println("\n=== Generate Reports ===");
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();

        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr + " 00:00", dateFormatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr + " 23:59", dateFormatter);

            List<Booking> report = ((Agent) currentUser).generateBookingReport(startDate, endDate);
            
            if (report.isEmpty()) {
                System.out.println("No bookings found in the specified date range.");
                return;
            }

            System.out.println("\n=== Booking Report ===");
            System.out.println("Period: " + startDateStr + " to " + endDateStr);
            System.out.println("Total Bookings: " + report.size());
            
            double totalRevenue = 0;
            for (Booking booking : report) {
                System.out.println("\nBooking ID: " + booking.getBookingId());
                System.out.println("Customer: " + booking.getCustomer().getName());
                System.out.println("Flight: " + booking.getFlight().getFlightNumber());
                System.out.println("Status: " + booking.getStatus());
                System.out.println("Seat Class: " + booking.getSeatClass());
                System.out.println("Price: $" + booking.getTotalPrice());
                totalRevenue += booking.getTotalPrice();
            }
            
            System.out.println("\nTotal Revenue: $" + totalRevenue);
            System.out.println("Agent Commission: $" + (totalRevenue * 0.05));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n=== Administrator Menu ===");
        System.out.println("1. Manage Users");
        System.out.println("2. System Settings");
        System.out.println("3. View System Logs");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                manageUsers();
                break;
            case 2:
                manageSystemSettings();
                break;
            case 3:
                viewSystemLogs();
                break;
            case 4:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void manageUsers() {
        System.out.println("\n=== Manage Users ===");
        System.out.println("1. Create New User");
        System.out.println("2. View All Users");
        System.out.println("3. Manage User Access");
        System.out.println("4. Return to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                createUser();
                break;
            case 2:
                viewAllUsers();
                break;
            case 3:
                manageUserAccess();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void createUser() {
        System.out.println("\n=== Create New User ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();

        System.out.println("\nSelect user role:");
        System.out.println("1. Customer");
        System.out.println("2. Agent");
        System.out.println("3. Administrator");
        System.out.print("Enter your choice: ");

        int roleChoice = scanner.nextInt();
        scanner.nextLine(); 

        UserRole role;
        String address = "";
        switch (roleChoice) {
            case 1:
                role = UserRole.CUSTOMER;
                System.out.print("Enter address: ");
                address = scanner.nextLine();
                break;
            case 2:
                role = UserRole.AGENT;
                System.out.print("Enter department: ");
                address = scanner.nextLine();
                break;
            case 3:
                role = UserRole.ADMINISTRATOR;
                break;
            default:
                System.out.println("Invalid role choice.");
                return;
        }

        try {
            User newUser = ((Administrator) currentUser).createUser(
                username, password, name, email, contactInfo, role);
            users.add(newUser);
            FileManager.saveUser(newUser);
            System.out.println("User created successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role: " + user.getRole());
            System.out.println("------------------------");
        }
    }

    private static void manageUserAccess() {
        System.out.println("\n=== Manage User Access ===");
        viewAllUsers();
        
        System.out.print("Enter username to manage: ");
        String username = scanner.nextLine();
        
        User userToManage = users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);

        if (userToManage == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("\n1. Grant Access");
        System.out.println("2. Revoke Access");
        System.out.println("3. Return to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        if (choice == 1 || choice == 2) {
            try {
                ((Administrator) currentUser).manageUserAccess(userToManage, choice == 1);
                System.out.println("User access " + (choice == 1 ? "granted" : "revoked") + " successfully.");
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void manageSystemSettings() {
        System.out.println("\n=== System Settings ===");
        System.out.println("1. Modify Setting");
        System.out.println("2. View Current Settings");
        System.out.println("3. Return to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                System.out.print("Enter setting name: ");
                String setting = scanner.nextLine();
                System.out.print("Enter new value: ");
                String value = scanner.nextLine();

                try {
                    ((Administrator) currentUser).modifySystemSettings(setting, value);
                    System.out.println("Setting modified successfully!");
                } catch (IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 2:
                
                System.out.println("\nCurrent System Settings:");
                System.out.println("1. Maintenance Mode: Off");
                System.out.println("2. Debug Mode: Off");
                System.out.println("3. Booking Window: 30 days");
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewSystemLogs() {
        Administrator admin = (Administrator) currentUser;
        List<String> logs = admin.getSystemLogs();
        
        if (logs.isEmpty()) {
            System.out.println("No system logs found.");
            return;
        }

        System.out.println("\n=== System Logs ===");
        for (String log : logs) {
            System.out.println(log);
            System.out.println("------------------------");
        }
    }
} 