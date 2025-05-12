import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Flight flight;
    private List<Passenger> passengers;
    private SeatClass seatClass;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private List<String> specialRequests;

    public Booking(Customer customer, Flight flight, List<Passenger> passengers, SeatClass seatClass) {
        this.bookingId = UUID.randomUUID().toString();
        this.customer = customer;
        this.flight = flight;
        this.passengers = new ArrayList<>(passengers);
        this.seatClass = seatClass;
        this.status = BookingStatus.RESERVED;
        this.bookingTime = LocalDateTime.now();
        this.specialRequests = new ArrayList<>();
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public List<String> getSpecialRequests() {
        return new ArrayList<>(specialRequests);
    }

    public void addSpecialRequest(String request) {
        specialRequests.add(request);
    }

    public double calculateTotalPrice() {
        return flight.calculatePrice(seatClass) * passengers.size();
    }

    public boolean confirmBooking() {
        if (status == BookingStatus.RESERVED) {
            status = BookingStatus.CONFIRMED;
            return true;
        }
        return false;
    }

    public boolean cancelBooking() {
        if (status != BookingStatus.CANCELLED) {
            status = BookingStatus.CANCELLED;
            flight.releaseSeat(seatClass);
            return true;
        }
        return false;
    }

    public void setSeatClass(SeatClass seatClass) {
        if (seatClass == null) {
            throw new IllegalArgumentException("Seat class cannot be null");
        }
        this.seatClass = seatClass;
    }

    public LocalDateTime getCreationTime() {
        return bookingTime;
    }

    public double getTotalPrice() {
        return calculateTotalPrice();
    }
} 