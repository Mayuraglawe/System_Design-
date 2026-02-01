import java.util.*;

/**
 * Comprehensive demonstration of all SOLID principles
 * S - Single Responsibility Principle
 * O - Open/Closed Principle  
 * L - Liskov Substitution Principle
 * I - Interface Segregation Principle
 * D - Dependency Inversion Principle
 */

// ===== SINGLE RESPONSIBILITY PRINCIPLE (SRP) =====
// Each class has only one reason to change

// Class responsible ONLY for user data
class User {
    private String id;
    private String name;
    private String email;
    private String password;
    
    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}

// Class responsible ONLY for password validation
class PasswordValidator {
    public boolean isValid(String password) {
        return password != null && 
               password.length() >= 8 && 
               password.matches(".*[A-Z].*") && 
               password.matches(".*[a-z].*") && 
               password.matches(".*\\d.*");
    }
    
    public String getValidationMessage(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        return "Password is valid";
    }
}

// Class responsible ONLY for email operations
class EmailService {
    public void sendWelcomeEmail(User user) {
        System.out.println("Sending welcome email to: " + user.getEmail());
        System.out.println("Subject: Welcome " + user.getName() + "!");
        System.out.println("Email sent successfully.\n");
    }
    
    public void sendPasswordResetEmail(User user) {
        System.out.println("Sending password reset email to: " + user.getEmail());
        System.out.println("Subject: Password Reset Request");
        System.out.println("Reset email sent successfully.\n");
    }
}

// Class responsible ONLY for user data persistence
class UserRepository {
    private Map<String, User> users = new HashMap<>();
    
    public void save(User user) {
        users.put(user.getId(), user);
        System.out.println("User " + user.getName() + " saved to database.");
    }
    
    public User findById(String id) {
        return users.get(id);
    }
    
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}

// ===== INTERFACE SEGREGATION PRINCIPLE (ISP) =====
// Clients should not be forced to depend on interfaces they don't use

// Separate interfaces for different functionalities
interface Readable {
    String read();
}

interface Writable {
    void write(String content);
}

interface Printable {
    void print();
}

interface Scannable {
    String scan();
}

interface Faxable {
    void fax(String content);
}

// ===== OPEN/CLOSED PRINCIPLE (OCP) =====
// Open for extension, closed for modification

// Abstract base for notification system
abstract class NotificationSender {
    protected String message;
    protected String recipient;
    
    public NotificationSender(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }
    
    public abstract void send();
    
    // Template method - common functionality
    protected void logNotification() {
        System.out.println("Notification logged: " + message.substring(0, Math.min(20, message.length())) + "...");
    }
}

// Extensions of NotificationSender (OCP: Open for extension)
class EmailNotificationSender extends NotificationSender {
    public EmailNotificationSender(String message, String recipient) {
        super(message, recipient);
    }
    
    @Override
    public void send() {
        System.out.println("Sending EMAIL to: " + recipient);
        System.out.println("Message: " + message);
        logNotification();
        System.out.println("Email sent successfully!\n");
    }
}

class SMSNotificationSender extends NotificationSender {
    public SMSNotificationSender(String message, String recipient) {
        super(message, recipient);
    }
    
    @Override
    public void send() {
        System.out.println("Sending SMS to: " + recipient);
        System.out.println("Message: " + message);
        logNotification();
        System.out.println("SMS sent successfully!\n");
    }
}

class PushNotificationSender extends NotificationSender {
    public PushNotificationSender(String message, String recipient) {
        super(message, recipient);
    }
    
    @Override
    public void send() {
        System.out.println("Sending PUSH notification to: " + recipient);
        System.out.println("Message: " + message);
        logNotification();
        System.out.println("Push notification sent successfully!\n");
    }
}

// ===== DEPENDENCY INVERSION PRINCIPLE (DIP) =====
// Depend on abstractions, not concretions

// High-level abstraction for data storage
interface DataStorage {
    void save(String key, String data);
    String retrieve(String key);
    boolean exists(String key);
}

// High-level abstraction for logging
interface Logger {
    void log(String message);
    void error(String message);
    void info(String message);
}

// Low-level implementations
class DatabaseStorage implements DataStorage {
    private Map<String, String> database = new HashMap<>();
    
    @Override
    public void save(String key, String data) {
        database.put(key, data);
        System.out.println("Data saved to database: " + key);
    }
    
    @Override
    public String retrieve(String key) {
        return database.get(key);
    }
    
    @Override
    public boolean exists(String key) {
        return database.containsKey(key);
    }
}

class FileStorage implements DataStorage {
    private Map<String, String> fileSystem = new HashMap<>();
    
    @Override
    public void save(String key, String data) {
        fileSystem.put(key, data);
        System.out.println("Data saved to file: " + key);
    }
    
    @Override
    public String retrieve(String key) {
        return fileSystem.get(key);
    }
    
    @Override
    public boolean exists(String key) {
        return fileSystem.containsKey(key);
    }
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
    
    @Override
    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }
    
    @Override
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }
}

// ===== LISKOV SUBSTITUTION PRINCIPLE (LSP) =====
// Subtypes must be substitutable for their base types

// Base shape class
abstract class Shape {
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
    
    public void displayInfo() {
        System.out.println(getClass().getSimpleName() + " - Area: " + 
                         String.format("%.2f", calculateArea()) + 
                         ", Perimeter: " + String.format("%.2f", calculatePerimeter()));
    }
}

// LSP-compliant implementations
class Rectangle extends Shape {
    protected double width;
    protected double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}

class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Triangle extends Shape {
    private double side1, side2, side3;
    
    public Triangle(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }
    
    @Override
    public double calculateArea() {
        // Using Heron's formula
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
    
    @Override
    public double calculatePerimeter() {
        return side1 + side2 + side3;
    }
}

// High-level class that depends on abstractions (DIP)
class UserService {
    private final DataStorage dataStorage;
    private final Logger logger;
    private final PasswordValidator passwordValidator;
    private final EmailService emailService;
    
    // Constructor injection (DIP: depending on abstractions)
    public UserService(DataStorage dataStorage, Logger logger, 
                      PasswordValidator passwordValidator, EmailService emailService) {
        this.dataStorage = dataStorage;
        this.logger = logger;
        this.passwordValidator = passwordValidator;
        this.emailService = emailService;
    }
    
    public boolean registerUser(User user) {
        logger.info("Attempting to register user: " + user.getName());
        
        try {
            // Validate password
            if (!passwordValidator.isValid(user.getPassword())) {
                logger.error("Registration failed: " + passwordValidator.getValidationMessage(user.getPassword()));
                return false;
            }
            
            // Check if user already exists
            if (dataStorage.exists(user.getEmail())) {
                logger.error("Registration failed: User already exists with email " + user.getEmail());
                return false;
            }
            
            // Save user
            dataStorage.save(user.getEmail(), user.getId());
            logger.info("User registered successfully: " + user.getName());
            
            // Send welcome email
            emailService.sendWelcomeEmail(user);
            
            return true;
        } catch (Exception e) {
            logger.error("Registration failed: " + e.getMessage());
            return false;
        }
    }
}

// ISP-compliant device implementations
class SimplePrinter implements Printable {
    @Override
    public void print() {
        System.out.println("Simple Printer: Document printed successfully.");
    }
}

class Scanner implements Scannable {
    @Override
    public String scan() {
        System.out.println("Scanner: Document scanned successfully.");
        return "Scanned content";
    }
}

class AllInOneDevice implements Printable, Scannable, Faxable {
    @Override
    public void print() {
        System.out.println("All-in-One: Document printed successfully.");
    }
    
    @Override
    public String scan() {
        System.out.println("All-in-One: Document scanned successfully.");
        return "Scanned content";
    }
    
    @Override
    public void fax(String content) {
        System.out.println("All-in-One: Fax sent - " + content);
    }
}

// Notification system using OCP
class NotificationService {
    private List<NotificationSender> senders = new ArrayList<>();
    
    public void addSender(NotificationSender sender) {
        senders.add(sender);
    }
    
    public void sendAllNotifications() {
        System.out.println("=== Sending All Notifications ===");
        for (NotificationSender sender : senders) {
            sender.send();
        }
    }
}

// Shape calculator demonstrating LSP
class ShapeCalculator {
    public void displayShapeInfo(List<Shape> shapes) {
        System.out.println("=== Shape Information ===");
        double totalArea = 0;
        double totalPerimeter = 0;
        
        for (Shape shape : shapes) {
            shape.displayInfo(); // LSP: works with any Shape subclass
            totalArea += shape.calculateArea();
            totalPerimeter += shape.calculatePerimeter();
        }
        
        System.out.println("Total Area: " + String.format("%.2f", totalArea));
        System.out.println("Total Perimeter: " + String.format("%.2f", totalPerimeter));
        System.out.println();
    }
}

// Main demonstration class
public class SOLID_Principles_Demo {
    public static void main(String[] args) {
        System.out.println("=== SOLID PRINCIPLES COMPREHENSIVE DEMONSTRATION ===\n");
        
        // ===== SINGLE RESPONSIBILITY PRINCIPLE DEMO =====
        System.out.println("1. SINGLE RESPONSIBILITY PRINCIPLE (SRP)");
        System.out.println("Each class has one reason to change:\n");
        
        PasswordValidator validator = new PasswordValidator();
        EmailService emailService = new EmailService();
        UserRepository repository = new UserRepository();
        
        User user1 = new User("1", "John Doe", "john@example.com", "SecurePass123");
        
        System.out.println("Password validation: " + validator.getValidationMessage(user1.getPassword()));
        repository.save(user1);
        emailService.sendWelcomeEmail(user1);
        
        // ===== OPEN/CLOSED PRINCIPLE DEMO =====
        System.out.println("\n2. OPEN/CLOSED PRINCIPLE (OCP)");
        System.out.println("Adding new notification types without modifying existing code:\n");
        
        NotificationService notificationService = new NotificationService();
        notificationService.addSender(new EmailNotificationSender("Welcome to our platform!", "user@example.com"));
        notificationService.addSender(new SMSNotificationSender("Your code is: 123456", "+1234567890"));
        notificationService.addSender(new PushNotificationSender("You have a new message!", "user123"));
        
        notificationService.sendAllNotifications();
        
        // ===== LISKOV SUBSTITUTION PRINCIPLE DEMO =====
        System.out.println("3. LISKOV SUBSTITUTION PRINCIPLE (LSP)");
        System.out.println("Substituting subclasses for base class without breaking functionality:\n");
        
        List<Shape> shapes = Arrays.asList(
            new Rectangle(5, 4),
            new Circle(3),
            new Triangle(3, 4, 5)
        );
        
        ShapeCalculator calculator = new ShapeCalculator();
        calculator.displayShapeInfo(shapes);
        
        // ===== INTERFACE SEGREGATION PRINCIPLE DEMO =====
        System.out.println("4. INTERFACE SEGREGATION PRINCIPLE (ISP)");
        System.out.println("Clients depend only on interfaces they use:\n");
        
        SimplePrinter printer = new SimplePrinter();
        Scanner scanner = new Scanner();
        AllInOneDevice allInOne = new AllInOneDevice();
        
        // Each device implements only the interfaces it needs
        printer.print();
        scanner.scan();
        
        allInOne.print();
        allInOne.scan();
        allInOne.fax("Important document");
        
        System.out.println();
        
        // ===== DEPENDENCY INVERSION PRINCIPLE DEMO =====
        System.out.println("5. DEPENDENCY INVERSION PRINCIPLE (DIP)");
        System.out.println("High-level modules depend on abstractions, not concretions:\n");
        
        // Using different implementations without changing UserService
        DataStorage databaseStorage = new DatabaseStorage();
        DataStorage fileStorage = new FileStorage();
        Logger logger = new ConsoleLogger();
        
        // UserService depends on abstractions, not concrete implementations
        UserService userServiceWithDB = new UserService(databaseStorage, logger, validator, emailService);
        UserService userServiceWithFile = new UserService(fileStorage, logger, validator, emailService);
        
        User user2 = new User("2", "Jane Smith", "jane@example.com", "AnotherPass456");
        User user3 = new User("3", "Bob Wilson", "bob@example.com", "MyPassword789");
        
        System.out.println("Using Database Storage:");
        userServiceWithDB.registerUser(user2);
        
        System.out.println("\nUsing File Storage:");
        userServiceWithFile.registerUser(user3);
        
        // ===== SUMMARY =====
        System.out.println("\n=== SOLID PRINCIPLES SUMMARY ===");
        System.out.println("✅ SRP: Each class has a single responsibility");
        System.out.println("✅ OCP: Open for extension, closed for modification");
        System.out.println("✅ LSP: Subclasses are substitutable for their base classes");
        System.out.println("✅ ISP: Interfaces are segregated by client needs");
        System.out.println("✅ DIP: Dependencies are inverted to depend on abstractions");
        System.out.println("\nThese principles lead to:");
        System.out.println("• More maintainable code");
        System.out.println("• Better testability");
        System.out.println("• Increased flexibility");
        System.out.println("• Reduced coupling");
        System.out.println("• Enhanced code reusability");
    }
}