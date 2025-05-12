import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactInfo;
    private UserRole role;
    private boolean isLoggedIn;

    public User(String username, String password, String name, String email, String contactInfo, UserRole role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info cannot be null or empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.setPassword(password);
        this.name = name;
        this.setEmail(email);
        this.contactInfo = contactInfo;
        this.role = role;
        this.isLoggedIn = false;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public UserRole getRole() {
        return role;
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        
        if (password.length() < 8) return false;
        
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        
        if (!Pattern.compile("[a-z]").matcher(password).find()) return false;
        
        if (!Pattern.compile("\\d").matcher(password).find()) return false;
        
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) return false;
        
        return true;
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        
        if (this.username.equals(username) && this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    public void logout() {
        if (!isLoggedIn) {
            throw new IllegalStateException("User is not logged in");
        }
        this.isLoggedIn = false;
    }

    public void updateProfile() {
        if (!isLoggedIn) {
            throw new IllegalStateException("User must be logged in to update profile");
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isLoggedIn=" + isLoggedIn +
                '}';
    }
} 