import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Map<SeatClass, Integer> availableSeats;
    private Map<SeatClass, Double> prices;

    public Flight(String flightNumber, String airline, String origin, String destination,
                 LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = new HashMap<>();
        this.prices = new HashMap<>();
        
        // Initialize seats for each class
        for (SeatClass seatClass : SeatClass.values()) {
            availableSeats.put(seatClass, 0);
            prices.put(seatClass, 0.0);
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public int getAvailableSeats(SeatClass seatClass) {
        return availableSeats.getOrDefault(seatClass, 0);
    }

    public void setAvailableSeats(SeatClass seatClass, int count) {
        if (count >= 0) {
            availableSeats.put(seatClass, count);
        } else {
            throw new IllegalArgumentException("Seat count cannot be negative");
        }
    }

    public double getPrice(SeatClass seatClass) {
        return prices.getOrDefault(seatClass, 0.0);
    }

    public void setPrice(SeatClass seatClass, double price) {
        if (price >= 0) {
            prices.put(seatClass, price);
        } else {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public boolean checkAvailability(SeatClass seatClass) {
        return getAvailableSeats(seatClass) > 0;
    }

    public boolean reserveSeat(SeatClass seatClass) {
        if (checkAvailability(seatClass)) {
            availableSeats.put(seatClass, availableSeats.get(seatClass) - 1);
            return true;
        }
        return false;
    }

    public void releaseSeat(SeatClass seatClass) {
        availableSeats.put(seatClass, availableSeats.get(seatClass) + 1);
    }

    public double calculatePrice(SeatClass seatClass) {
        return prices.getOrDefault(seatClass, 0.0);
    }

    public boolean hasAvailableSeats(SeatClass seatClass, int count) {
        return getAvailableSeats(seatClass) >= count;
    }
} 