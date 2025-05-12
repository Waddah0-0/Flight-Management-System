import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Passenger {
    private String passengerId;
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String nationality;
    private String dateOfBirth;
    private List<String> specialRequirements;

    public Passenger(String firstName, String lastName, String passportNumber, 
                    String nationality, String dateOfBirth) {
        this.passengerId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.specialRequirements = new ArrayList<>();
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getSpecialRequirements() {
        return new ArrayList<>(specialRequirements);
    }

    public void addSpecialRequirement(String requirement) {
        specialRequirements.add(requirement);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
} 