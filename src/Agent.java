import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Agent extends User {
    private String agentId;
    private String department;
    private double commission;
    private List<Booking> managedBookings;

    public Agent(String username, String password, String name, String email, 
                String contactInfo, String department) {
        super(username, password, name, email, contactInfo, UserRole.AGENT);
        this.agentId = UUID.randomUUID().toString();
        this.department = department;
        this.commission = 0.0;
        this.managedBookings = new ArrayList<>();
    }

    public String getAgentId() {
        return agentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        if (commission >= 0) {
            this.commission = commission;
        } else {
            throw new IllegalArgumentException("Commission cannot be negative");
        }
    }

    public List<Booking> getManagedBookings() {
        return new ArrayList<>(managedBookings);
    }

    public void addManagedBooking(Booking booking) {
        managedBookings.add(booking);
    }

    @Override
    public boolean login(String username, String password) {
        if (!isLoggedIn()) {
            boolean success = super.login(username, password);
            if (success) {
                System.out.println("Agent " + getName() + " (ID: " + agentId + ") logged in successfully");
            }
            return success;
        }
        return false;
    }

    @Override
    public void logout() {
        if (isLoggedIn()) {
            super.logout();
            System.out.println("Agent " + getName() + " (ID: " + agentId + ") logged out successfully");
        } else {
            throw new IllegalStateException("Agent is not logged in");
        }
    }

    @Override
    public void updateProfile() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Agent must be logged in to update profile");
        }
        System.out.println("Agent profile updated successfully");
    }

    public Flight createFlight(String flightNumber, String airline, String origin, 
                             String destination, LocalDateTime departureTime, 
                             LocalDateTime arrivalTime) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Agent must be logged in to create flights");
        }

        if (flightNumber == null || airline == null || origin == null || 
            destination == null || departureTime == null || arrivalTime == null) {
            throw new IllegalArgumentException("All flight details must be provided");
        }

        if (departureTime.isAfter(arrivalTime)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }

        Flight flight = new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime);
        System.out.println("Flight " + flightNumber + " created successfully");
        return flight;
    }

    public Booking createBookingForCustomer(Customer customer, Flight flight, 
                                          List<Passenger> passengers, SeatClass seatClass) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Agent must be logged in to create bookings");
        }

        if (customer == null || flight == null || passengers == null || seatClass == null) {
            throw new IllegalArgumentException("All booking details must be provided");
        }

        if (passengers.isEmpty()) {
            throw new IllegalArgumentException("At least one passenger must be specified");
        }

        // Check if flight has available seats
        if (!flight.hasAvailableSeats(seatClass, passengers.size())) {
            throw new IllegalStateException("Not enough seats available in " + seatClass + " class");
        }

        Booking booking = new Booking(customer, flight, passengers, seatClass);
        managedBookings.add(booking);
        
        // Calculate and update commission
        double bookingCommission = calculateCommission(booking);
        this.commission += bookingCommission;

        System.out.println("Booking created successfully for customer " + customer.getName());
        System.out.println("Commission earned: $" + bookingCommission);
        return booking;
    }

    public boolean modifyBooking(Booking booking, SeatClass newSeatClass) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Agent must be logged in to modify bookings");
        }

        if (booking == null || newSeatClass == null) {
            throw new IllegalArgumentException("Booking and seat class must be provided");
        }

        if (!managedBookings.contains(booking)) {
            throw new IllegalArgumentException("Booking not found in agent's managed bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify a cancelled booking");
        }

        // Check if new seat class is available
        if (!booking.getFlight().hasAvailableSeats(newSeatClass, booking.getPassengers().size())) {
            throw new IllegalStateException("Not enough seats available in " + newSeatClass + " class");
        }

        // Update booking
        booking.setSeatClass(newSeatClass);
        System.out.println("Booking modified successfully to " + newSeatClass + " class");
        return true;
    }

    public List<Booking> generateBookingReport(LocalDateTime startDate, LocalDateTime endDate) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Agent must be logged in to generate reports");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates must be provided");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        return managedBookings.stream()
            .filter(booking -> {
                LocalDateTime bookingTime = booking.getCreationTime();
                return !bookingTime.isBefore(startDate) && !bookingTime.isAfter(endDate);
            })
            .collect(Collectors.toList());
    }

    private double calculateCommission(Booking booking) {
        // Simple commission calculation: 5% of the booking total
        return booking.getTotalPrice() * 0.05;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId='" + agentId + '\'' +
                ", username='" + getUsername() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + department + '\'' +
                ", commission=" + commission +
                ", managedBookings=" + managedBookings.size() +
                '}';
    }
} 