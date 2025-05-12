import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Customer extends User {
    private String address;
    private List<Booking> bookingHistory;
    private List<String> preferences;

    public Customer(String username, String password, String name, String email, String contactInfo, String address) {
        super(username, password, name, email, contactInfo, UserRole.CUSTOMER);
        this.address = address;
        this.bookingHistory = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Booking> getBookingHistory() {
        return new ArrayList<>(bookingHistory);
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }

    public List<String> getPreferences() {
        return new ArrayList<>(preferences);
    }

    public void addPreference(String preference) {
        if (preference == null || preference.trim().isEmpty()) {
            throw new IllegalArgumentException("Preference cannot be null or empty");
        }
        preferences.add(preference);
        System.out.println("Preference added: " + preference);
    }

    @Override
    public boolean login(String username, String password) {
        if (!isLoggedIn()) {
            boolean success = super.login(username, password);
            if (success) {
                System.out.println("Customer " + getName() + " logged in successfully");
            }
            return success;
        }
        return false;
    }

    @Override
    public void logout() {
        if (isLoggedIn()) {
            super.logout();
            System.out.println("Customer " + getName() + " logged out successfully");
        } else {
            throw new IllegalStateException("Customer is not logged in");
        }
    }

    @Override
    public void updateProfile() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Customer must be logged in to update profile");
        }
        // Profile update logic would typically involve updating user information
        // This could be expanded based on specific requirements
        System.out.println("Customer profile updated successfully");
    }

    public List<Flight> searchFlights(String origin, String destination, String date) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Customer must be logged in to search flights");
        }
        
        List<Flight> availableFlights = new ArrayList<>();
        // In a real application, this would query a database or external service
        // For now, we'll return an empty list
        return availableFlights;
    }

    public Booking createBooking(Flight flight, List<Passenger> passengers) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Customer must be logged in to create a booking");
        }
        
        if (flight == null || passengers == null || passengers.isEmpty()) {
            throw new IllegalArgumentException("Flight and passengers list cannot be null or empty");
        }

        // Create a new booking
        Booking booking = new Booking(this, flight, passengers, SeatClass.ECONOMY); // Default to ECONOMY
        bookingHistory.add(booking);
        
        // In a real application, this would be saved to a database
        System.out.println("Booking created successfully for flight " + flight.getFlightNumber());
        return booking;
    }

    public void cancelBooking(String bookingId) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Customer must be logged in to cancel a booking");
        }

        Booking bookingToCancel = bookingHistory.stream()
            .filter(booking -> booking.getBookingId().equals(bookingId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (bookingToCancel.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Check if cancellation is allowed (e.g., within 24 hours of departure)
        LocalDateTime departureTime = bookingToCancel.getFlight().getDepartureTime();
        if (LocalDateTime.now().plusHours(24).isAfter(departureTime)) {
            throw new IllegalStateException("Cannot cancel booking within 24 hours of departure");
        }

        bookingToCancel.setStatus(BookingStatus.CANCELLED);
        System.out.println("Booking " + bookingId + " cancelled successfully");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", address='" + address + '\'' +
                ", bookingCount=" + bookingHistory.size() +
                ", preferences=" + preferences +
                '}';
    }
} 