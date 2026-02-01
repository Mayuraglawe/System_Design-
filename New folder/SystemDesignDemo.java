import java.util.*;

// --- Core Concept: Observer Pattern for Decoupled Communication ---
interface Observer {
    void update(String message);
}

// --- Core Concept: Singleton for Global Resource Management ---
class NotificationManager {
    private static NotificationManager instance;
    private List<Observer> users = new ArrayList<>();

    private NotificationManager() {
    } // Private constructor [web:3]

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void subscribe(Observer user) {
        users.add(user);
    }

    public void notifyAll(String message) {
        for (Observer user : users) {
            user.update(message);
        }
    }
}

// --- Core Concept: Factory Pattern for Scalable Object Creation ---
interface Notification {
    void send();
}

class EmailNotification implements Notification {
    public void send() {
        System.out.println("Sending Email...");
    }
}

class SMSNotification implements Notification {
    public void send() {
        System.out.println("Sending SMS...");
    }
}

class NotificationFactory {
    public static Notification createNotification(String type) {
        if (type.equalsIgnoreCase("EMAIL"))
            return new EmailNotification();
        if (type.equalsIgnoreCase("SMS"))
            return new SMSNotification();
        return null;
    }
}

// --- Implementation ---
class NotificationUser implements Observer {
    private String name;

    public NotificationUser(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}

public class SystemDesignDemo {
    public static void main(String[] args) {
        // Initialize Manager (Singleton)
        NotificationManager manager = NotificationManager.getInstance();

        // Add Subscribers (Observer)
        manager.subscribe(new NotificationUser("Mayur"));
        manager.subscribe(new NotificationUser("Admin"));

        // Create and Send (Factory)
        Notification note = NotificationFactory.createNotification("EMAIL");
        note.send();

        // Broadcast Message
        manager.notifyAll("System Update Scheduled for 2:00 AM.");
    }
}
