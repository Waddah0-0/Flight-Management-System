import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String FLIGHTS_FILE = "flights.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String PASSENGERS_FILE = "passengers.txt";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String address = "";
            if (user instanceof Customer) {
                address = ((Customer) user).getAddress();
            } else if (user instanceof Agent) {
                address = ((Agent) user).getDepartment();
            }
            
            out.println(String.format("%s|%s|%s|%s|%s|%s|%s",
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getContactInfo(),
                address,
                user.getRole().name()));
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    try {
                        UserRole role = UserRole.valueOf(parts[6].trim());
                        User user;
                        switch (role) {
                            case CUSTOMER:
                                user = new Customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case AGENT:
                                user = new Agent(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case ADMINISTRATOR:
                                user = new Administrator(parts[0], parts[1], parts[2], parts[3], parts[4], 5);
                                break;
                            default:
                                continue;
                        }
                        users.add(user);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid role in users.txt: " + parts[6]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public static void saveFlight(Flight flight) {
        try (FileWriter fw = new FileWriter(FLIGHTS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(String.format("%s|%s|%s|%s|%s|%s|%.2f|%.2f|%.2f|%d|%d|%d",
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime().format(dateFormatter),
                flight.getArrivalTime().format(dateFormatter),
                flight.getPrice(SeatClass.ECONOMY),
                flight.getPrice(SeatClass.BUSINESS),
                flight.getPrice(SeatClass.FIRST_CLASS),
                flight.getAvailableSeats(SeatClass.ECONOMY),
                flight.getAvailableSeats(SeatClass.BUSINESS),
                flight.getAvailableSeats(SeatClass.FIRST_CLASS)));
        } catch (IOException e) {
            System.err.println("Error saving flight: " + e.getMessage());
        }
    }

    public static List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FLIGHTS_FILE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 12) {
                    Flight flight = new Flight(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        LocalDateTime.parse(parts[4].trim(), dateFormatter),
                        LocalDateTime.parse(parts[5].trim(), dateFormatter)
                    );
                    
                    flight.setPrice(SeatClass.ECONOMY, Double.parseDouble(parts[6].trim()));
                    flight.setPrice(SeatClass.BUSINESS, Double.parseDouble(parts[7].trim()));
                    flight.setPrice(SeatClass.FIRST_CLASS, Double.parseDouble(parts[8].trim()));
                    
                    flight.setAvailableSeats(SeatClass.ECONOMY, Integer.parseInt(parts[9].trim()));
                    flight.setAvailableSeats(SeatClass.BUSINESS, Integer.parseInt(parts[10].trim()));
                    flight.setAvailableSeats(SeatClass.FIRST_CLASS, Integer.parseInt(parts[11].trim()));
                    
                    flights.add(flight);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading flights: " + e.getMessage());
        }
        return flights;
    }

    public static void saveBooking(Booking booking) {
        try (FileWriter fw = new FileWriter(BOOKINGS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(String.format("%s|%s|%s|%s|%s|%.2f|%s",
                booking.getBookingId(),
                booking.getCustomer().getUsername(),
                booking.getFlight().getFlightNumber(),
                booking.getStatus(),
                booking.getSeatClass(),
                booking.calculateTotalPrice(),
                booking.getBookingTime().format(dateFormatter)));
        } catch (IOException e) {
            System.err.println("Error saving booking: " + e.getMessage());
        }
    }

    public static List<Booking> loadBookings(List<User> users, List<Flight> flights) {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    Customer customer = (Customer) users.stream()
                        .filter(u -> u.getUsername().equals(parts[1]))
                        .findFirst()
                        .orElse(null);
                    
                    Flight flight = flights.stream()
                        .filter(f -> f.getFlightNumber().equals(parts[2]))
                        .findFirst()
                        .orElse(null);
                    
                    if (customer != null && flight != null) {
                        Booking booking = new Booking(customer, flight, new ArrayList<>(), SeatClass.valueOf(parts[4]));
                        booking.setStatus(BookingStatus.valueOf(parts[3]));
                        bookings.add(booking);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    public static void savePassenger(Passenger passenger, String bookingId) {
        try (FileWriter fw = new FileWriter(PASSENGERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(String.format("%s|%s|%s|%s|%s|%s",
                bookingId,
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getPassportNumber(),
                passenger.getNationality(),
                passenger.getDateOfBirth()));
        } catch (IOException e) {
            System.err.println("Error saving passenger: " + e.getMessage());
        }
    }

    public static List<Passenger> loadPassengers(String bookingId) {
        List<Passenger> passengers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSENGERS_FILE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].equals(bookingId)) {
                    Passenger passenger = new Passenger(
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5]
                    );
                    passengers.add(passenger);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading passengers: " + e.getMessage());
        }
        return passengers;
    }

    public static void initializeFiles() {
        try {
            createFileIfNotExists(USERS_FILE, "username|password|name|email|contactInfo|address|role");
            createFileIfNotExists(FLIGHTS_FILE, "flightNumber|airline|origin|destination|departureTime|arrivalTime|economyPrice|businessPrice|firstClassPrice|economySeats|businessSeats|firstClassSeats");
            createFileIfNotExists(BOOKINGS_FILE, "bookingId|customerUsername|flightNumber|status|seatClass|totalPrice|bookingTime");
            createFileIfNotExists(PASSENGERS_FILE, "bookingId|firstName|lastName|passportNumber|nationality|dateOfBirth");
            
            addExampleDataIfEmpty();
        } catch (IOException e) {
            System.err.println("Error initializing files: " + e.getMessage());
        }
    }

    private static void addExampleDataIfEmpty() {
        try {
            if (new File(USERS_FILE).length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                    writer.write("admin|Admin123!|Admin User|admin@example.com|1234567890|Admin Office|ADMINISTRATOR\n");
                    writer.write("agent1|Agent123!|John Agent|agent@example.com|9876543210|Agent Office|AGENT\n");
                    writer.write("customer1|Customer123!|Jane Customer|customer@example.com|5555555555|123 Main St|CUSTOMER\n");
                }
            }

            if (new File(FLIGHTS_FILE).length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FLIGHTS_FILE, true))) {
                    writer.write("FL101|Airline1|New York|London|2024-03-20 10:00|2024-03-20 22:00|500.00|1000.00|2000.00|100|50|20\n");
                    writer.write("FL102|Airline2|London|Paris|2024-03-21 14:00|2024-03-21 16:00|300.00|600.00|1200.00|80|40|15\n");
                }
            }

            if (new File(BOOKINGS_FILE).length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true))) {
                    writer.write("BK001|customer1|FL101|CONFIRMED|ECONOMY|500.00|2024-03-19 15:00\n");
                }
            }

            if (new File(PASSENGERS_FILE).length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSENGERS_FILE, true))) {
                    writer.write("BK001|Jane|Customer|P123456|USA|1990-01-01\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error adding example data: " + e.getMessage());
        }
    }

    private static void createFileIfNotExists(String filename, String header) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(header);
            }
        }
    }
} 