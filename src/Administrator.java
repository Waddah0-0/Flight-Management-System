import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class Administrator extends User {
    private String adminId;
    private int securityLevel;
    private List<String> systemLogs;

    public Administrator(String username, String password, String name, String email, 
                        String contactInfo, int securityLevel) {
        super(username, password, name, email, contactInfo, UserRole.ADMINISTRATOR);
        this.adminId = UUID.randomUUID().toString();
        this.securityLevel = securityLevel;
        this.systemLogs = new ArrayList<>();
    }

    public String getAdminId() {
        return adminId;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        if (securityLevel >= 1 && securityLevel <= 5) {
            this.securityLevel = securityLevel;
        } else {
            throw new IllegalArgumentException("Security level must be between 1 and 5");
        }
    }

    public List<String> getSystemLogs() {
        return new ArrayList<>(systemLogs);
    }

    public void addSystemLog(String log) {
        if (log == null || log.trim().isEmpty()) {
            throw new IllegalArgumentException("Log message cannot be null or empty");
        }
        systemLogs.add(log);
    }

    @Override
    public boolean login(String username, String password) {
        if (!isLoggedIn()) {
            boolean success = super.login(username, password);
            if (success) {
                String logMessage = String.format("Administrator %s (ID: %s) logged in at %s", 
                    getName(), adminId, LocalDateTime.now());
                addSystemLog(logMessage);
                System.out.println("Administrator logged in successfully");
            }
            return success;
        }
        return false;
    }

    @Override
    public void logout() {
        if (isLoggedIn()) {
            super.logout();
            String logMessage = String.format("Administrator %s (ID: %s) logged out at %s", 
                getName(), adminId, LocalDateTime.now());
            addSystemLog(logMessage);
            System.out.println("Administrator logged out successfully");
        } else {
            throw new IllegalStateException("Administrator is not logged in");
        }
    }

    @Override
    public void updateProfile() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Administrator must be logged in to update profile");
        }
        String logMessage = String.format("Administrator %s (ID: %s) updated profile at %s", 
            getName(), adminId, LocalDateTime.now());
        addSystemLog(logMessage);
        System.out.println("Administrator profile updated successfully");
    }

    public User createUser(String username, String password, String name, String email, 
                          String contactInfo, UserRole role) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Administrator must be logged in to create users");
        }

        if (securityLevel < 3) {
            throw new IllegalStateException("Insufficient security level to create users");
        }

        User newUser = null;
        switch (role) {
            case CUSTOMER:
                newUser = new Customer(username, password, name, email, contactInfo, "");
                break;
            case AGENT:
                newUser = new Agent(username, password, name, email, contactInfo, "");
                break;
            case ADMINISTRATOR:
                if (securityLevel < 5) {
                    throw new IllegalStateException("Only level 5 administrators can create other administrators");
                }
                newUser = new Administrator(username, password, name, email, contactInfo, 1);
                break;
            default:
                throw new IllegalArgumentException("Invalid user role");
        }

        String logMessage = String.format("New %s created by Administrator %s (ID: %s) at %s", 
            role, getName(), adminId, LocalDateTime.now());
        addSystemLog(logMessage);
        System.out.println("User created successfully");
        return newUser;
    }

    public void modifySystemSettings(String setting, String value) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Administrator must be logged in to modify settings");
        }

        if (securityLevel < 4) {
            throw new IllegalStateException("Insufficient security level to modify system settings");
        }

        // In a real application, this would update actual system settings
        String logMessage = String.format("System setting '%s' modified to '%s' by Administrator %s (ID: %s) at %s", 
            setting, value, getName(), adminId, LocalDateTime.now());
        addSystemLog(logMessage);
        System.out.println("System setting modified successfully");
    }

    public void manageUserAccess(User user, boolean grantAccess) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Administrator must be logged in to manage user access");
        }

        if (securityLevel < 3) {
            throw new IllegalStateException("Insufficient security level to manage user access");
        }

        // In a real application, this would update user access permissions
        String logMessage = String.format("User access %s for user %s by Administrator %s (ID: %s) at %s", 
            grantAccess ? "granted" : "revoked", user.getUsername(), getName(), adminId, LocalDateTime.now());
        addSystemLog(logMessage);
        System.out.println("User access " + (grantAccess ? "granted" : "revoked") + " successfully");
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId='" + adminId + '\'' +
                ", username='" + getUsername() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", securityLevel=" + securityLevel +
                ", logCount=" + systemLogs.size() +
                '}';
    }
} 