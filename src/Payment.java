import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private String paymentId;
    private double amount;
    private String currency;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime transactionTime;
    private String transactionDetails;

    public Payment(double amount, String currency, PaymentMethod method) {
        this.paymentId = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.transactionTime = LocalDateTime.now();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(String details) {
        this.transactionDetails = details;
    }

    public boolean processPayment() {
        // TODO: Implement actual payment processing logic
        this.status = PaymentStatus.COMPLETED;
        return true;
    }

    public boolean refundPayment() {
        if (status == PaymentStatus.COMPLETED) {
            // TODO: Implement actual refund logic
            this.status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }
} 