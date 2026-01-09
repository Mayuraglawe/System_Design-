import java.util.*;

// Abstract base class representing a Vehicle
abstract class Vehicle {
    protected String brand;
    protected String model;
    protected double fuelLevel;
    protected boolean isStarted;
    protected double maxSpeed;
    
    public Vehicle(String brand, String model, double maxSpeed) {
        this.brand = brand;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.fuelLevel = 100.0; // Start with full fuel
        this.isStarted = false;
    }
    
    // Abstract methods that subclasses must implement
    public abstract void start();
    public abstract void stop();
    public abstract double calculateFuelConsumption(double distance);
    public abstract void accelerate(double speed);
    public abstract String getVehicleType();
    
    // Common methods with default implementation
    public void refuel(double amount) {
        this.fuelLevel = Math.min(100.0, this.fuelLevel + amount);
        System.out.println(getVehicleInfo() + " refueled. Current fuel level: " + fuelLevel + "%");
    }
    
    public boolean canTravel(double distance) {
        return calculateFuelConsumption(distance) <= fuelLevel;
    }
    
    public String getVehicleInfo() {
        return getVehicleType() + " (" + brand + " " + model + ")";
    }
    
    // Getters
    public double getFuelLevel() { return fuelLevel; }
    public boolean isStarted() { return isStarted; }
    public double getMaxSpeed() { return maxSpeed; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
}

// Car implementation
class Car extends Vehicle {
    private int numberOfDoors;
    private boolean isLuxury;
    
    public Car(String brand, String model, int numberOfDoors, boolean isLuxury) {
        super(brand, model, 180.0); // Max speed 180 km/h
        this.numberOfDoors = numberOfDoors;
        this.isLuxury = isLuxury;
    }
    
    @Override
    public void start() {
        if (!isStarted) {
            if (fuelLevel > 0) {
                isStarted = true;
                System.out.println(getVehicleInfo() + " started with ignition key.");
            } else {
                System.out.println(getVehicleInfo() + " cannot start - no fuel!");
            }
        } else {
            System.out.println(getVehicleInfo() + " is already running.");
        }
    }
    
    @Override
    public void stop() {
        if (isStarted) {
            isStarted = false;
            System.out.println(getVehicleInfo() + " stopped and engine turned off.");
        } else {
            System.out.println(getVehicleInfo() + " is already stopped.");
        }
    }
    
    @Override
    public double calculateFuelConsumption(double distance) {
        // Cars consume 8L per 100km (8% fuel per 100km)
        double baseFuelConsumption = (distance / 100) * 8.0;
        return isLuxury ? baseFuelConsumption * 1.5 : baseFuelConsumption; // Luxury cars consume more
    }
    
    @Override
    public void accelerate(double speed) {
        if (!isStarted) {
            System.out.println(getVehicleInfo() + " must be started before accelerating!");
            return;
        }
        
        if (speed <= maxSpeed) {
            System.out.println(getVehicleInfo() + " accelerating to " + speed + " km/h smoothly.");
            fuelLevel -= 0.5; // Small fuel consumption for acceleration
        } else {
            System.out.println(getVehicleInfo() + " cannot exceed maximum speed of " + maxSpeed + " km/h!");
        }
    }
    
    @Override
    public String getVehicleType() {
        return "Car";
    }
    
    public void openTrunk() {
        System.out.println(getVehicleInfo() + " trunk opened.");
    }
}

// Truck implementation
class Truck extends Vehicle {
    private double cargoCapacity;
    private double currentCargo;
    
    public Truck(String brand, String model, double cargoCapacity) {
        super(brand, model, 120.0); // Max speed 120 km/h
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = 0.0;
    }
    
    @Override
    public void start() {
        if (!isStarted) {
            if (fuelLevel > 0) {
                isStarted = true;
                System.out.println(getVehicleInfo() + " started with diesel engine warming up.");
            } else {
                System.out.println(getVehicleInfo() + " cannot start - no fuel!");
            }
        } else {
            System.out.println(getVehicleInfo() + " is already running.");
        }
    }
    
    @Override
    public void stop() {
        if (isStarted) {
            isStarted = false;
            System.out.println(getVehicleInfo() + " stopped with air brake system engaged.");
        } else {
            System.out.println(getVehicleInfo() + " is already stopped.");
        }
    }
    
    @Override
    public double calculateFuelConsumption(double distance) {
        // Trucks consume 25L per 100km, more when loaded
        double baseFuelConsumption = (distance / 100) * 25.0;
        double loadFactor = 1 + (currentCargo / cargoCapacity) * 0.8; // Up to 80% more fuel when fully loaded
        return baseFuelConsumption * loadFactor;
    }
    
    @Override
    public void accelerate(double speed) {
        if (!isStarted) {
            System.out.println(getVehicleInfo() + " must be started before accelerating!");
            return;
        }
        
        if (speed <= maxSpeed) {
            System.out.println(getVehicleInfo() + " accelerating to " + speed + " km/h (heavy acceleration due to weight).");
            fuelLevel -= 1.0; // Higher fuel consumption for acceleration
        } else {
            System.out.println(getVehicleInfo() + " cannot exceed maximum speed of " + maxSpeed + " km/h!");
        }
    }
    
    @Override
    public String getVehicleType() {
        return "Truck";
    }
    
    public void loadCargo(double weight) {
        if (currentCargo + weight <= cargoCapacity) {
            currentCargo += weight;
            System.out.println(getVehicleInfo() + " loaded " + weight + " tons. Current cargo: " + currentCargo + "/" + cargoCapacity + " tons.");
        } else {
            System.out.println(getVehicleInfo() + " cannot load " + weight + " tons. Exceeds capacity!");
        }
    }
    
    public void unloadCargo(double weight) {
        if (currentCargo >= weight) {
            currentCargo -= weight;
            System.out.println(getVehicleInfo() + " unloaded " + weight + " tons. Current cargo: " + currentCargo + "/" + cargoCapacity + " tons.");
        } else {
            System.out.println(getVehicleInfo() + " cannot unload " + weight + " tons. Not enough cargo loaded!");
        }
    }
}

// Motorcycle implementation
class Motorcycle extends Vehicle {
    private boolean hasSidecar;
    
    public Motorcycle(String brand, String model, boolean hasSidecar) {
        super(brand, model, 200.0); // Max speed 200 km/h
        this.hasSidecar = hasSidecar;
    }
    
    @Override
    public void start() {
        if (!isStarted) {
            if (fuelLevel > 0) {
                isStarted = true;
                System.out.println(getVehicleInfo() + " started with kick-start or electric start.");
            } else {
                System.out.println(getVehicleInfo() + " cannot start - no fuel!");
            }
        } else {
            System.out.println(getVehicleInfo() + " is already running.");
        }
    }
    
    @Override
    public void stop() {
        if (isStarted) {
            isStarted = false;
            System.out.println(getVehicleInfo() + " stopped and engine turned off.");
        } else {
            System.out.println(getVehicleInfo() + " is already stopped.");
        }
    }
    
    @Override
    public double calculateFuelConsumption(double distance) {
        // Motorcycles are fuel efficient: 3L per 100km
        double baseFuelConsumption = (distance / 100) * 3.0;
        return hasSidecar ? baseFuelConsumption * 1.3 : baseFuelConsumption; // Sidecar increases consumption
    }
    
    @Override
    public void accelerate(double speed) {
        if (!isStarted) {
            System.out.println(getVehicleInfo() + " must be started before accelerating!");
            return;
        }
        
        if (speed <= maxSpeed) {
            System.out.println(getVehicleInfo() + " accelerating to " + speed + " km/h rapidly.");
            fuelLevel -= 0.3; // Lower fuel consumption for acceleration
        } else {
            System.out.println(getVehicleInfo() + " cannot exceed maximum speed of " + maxSpeed + " km/h!");
        }
    }
    
    @Override
    public String getVehicleType() {
        return "Motorcycle";
    }
    
    public void wheelie() {
        if (isStarted && !hasSidecar) {
            System.out.println(getVehicleInfo() + " performing a wheelie!");
        } else if (hasSidecar) {
            System.out.println(getVehicleInfo() + " cannot perform wheelie with sidecar!");
        } else {
            System.out.println(getVehicleInfo() + " must be started to perform wheelie!");
        }
    }
}

// Electric Car implementation
class ElectricCar extends Vehicle {
    private double batteryLevel;
    private double batteryCapacity;
    
    public ElectricCar(String brand, String model, double batteryCapacity) {
        super(brand, model, 160.0); // Max speed 160 km/h
        this.batteryCapacity = batteryCapacity;
        this.batteryLevel = batteryCapacity;
        this.fuelLevel = 100.0; // We'll use fuelLevel to represent battery percentage
    }
    
    @Override
    public void start() {
        if (!isStarted) {
            if (batteryLevel > 0) {
                isStarted = true;
                System.out.println(getVehicleInfo() + " started silently (electric motor).");
            } else {
                System.out.println(getVehicleInfo() + " cannot start - battery depleted!");
            }
        } else {
            System.out.println(getVehicleInfo() + " is already running.");
        }
    }
    
    @Override
    public void stop() {
        if (isStarted) {
            isStarted = false;
            System.out.println(getVehicleInfo() + " stopped (regenerative braking engaged).");
            batteryLevel += 1.0; // Small battery recovery from regenerative braking
            fuelLevel = (batteryLevel / batteryCapacity) * 100;
        } else {
            System.out.println(getVehicleInfo() + " is already stopped.");
        }
    }
    
    @Override
    public double calculateFuelConsumption(double distance) {
        // Electric cars: 20 kWh per 100km (represented as percentage)
        return (distance / 100) * 20.0;
    }
    
    @Override
    public void accelerate(double speed) {
        if (!isStarted) {
            System.out.println(getVehicleInfo() + " must be started before accelerating!");
            return;
        }
        
        if (speed <= maxSpeed) {
            System.out.println(getVehicleInfo() + " accelerating to " + speed + " km/h instantly (electric torque).");
            batteryLevel -= 2.0; // Battery consumption for acceleration
            fuelLevel = (batteryLevel / batteryCapacity) * 100;
        } else {
            System.out.println(getVehicleInfo() + " cannot exceed maximum speed of " + maxSpeed + " km/h!");
        }
    }
    
    @Override
    public void refuel(double amount) {
        // Override refuel to represent charging
        batteryLevel = Math.min(batteryCapacity, batteryLevel + amount);
        fuelLevel = (batteryLevel / batteryCapacity) * 100;
        System.out.println(getVehicleInfo() + " charged. Current battery level: " + String.format("%.1f", fuelLevel) + "%");
    }
    
    @Override
    public String getVehicleType() {
        return "Electric Car";
    }
    
    public void enableAutopilot() {
        if (isStarted) {
            System.out.println(getVehicleInfo() + " autopilot engaged.");
        } else {
            System.out.println(getVehicleInfo() + " must be started to enable autopilot!");
        }
    }
}

// Vehicle Management System demonstrating LSP
class VehicleFleetManager {
    private List<Vehicle> fleet;
    
    public VehicleFleetManager() {
        this.fleet = new ArrayList<>();
    }
    
    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
        System.out.println("Added " + vehicle.getVehicleInfo() + " to fleet.");
    }
    
    // LSP in action: This method works with any Vehicle subclass
    public void startAllVehicles() {
        System.out.println("\n=== Starting All Vehicles ===");
        for (Vehicle vehicle : fleet) {
            vehicle.start(); // Polymorphism: each subclass implements start() differently
        }
    }
    
    public void stopAllVehicles() {
        System.out.println("\n=== Stopping All Vehicles ===");
        for (Vehicle vehicle : fleet) {
            vehicle.stop(); // Polymorphism: each subclass implements stop() differently
        }
    }
    
    public void testAcceleration(double targetSpeed) {
        System.out.println("\n=== Testing Acceleration to " + targetSpeed + " km/h ===");
        for (Vehicle vehicle : fleet) {
            vehicle.accelerate(targetSpeed); // Polymorphism in action
        }
    }
    
    public void planTrip(double distance) {
        System.out.println("\n=== Planning Trip of " + distance + " km ===");
        for (Vehicle vehicle : fleet) {
            double fuelNeeded = vehicle.calculateFuelConsumption(distance);
            boolean canMakeTrip = vehicle.canTravel(distance);
            
            System.out.println(vehicle.getVehicleInfo() + ":");
            System.out.println("  Fuel needed: " + String.format("%.2f", fuelNeeded) + "%");
            System.out.println("  Current fuel: " + String.format("%.2f", vehicle.getFuelLevel()) + "%");
            System.out.println("  Can make trip: " + (canMakeTrip ? "YES" : "NO"));
            
            if (!canMakeTrip) {
                double fuelToAdd = fuelNeeded - vehicle.getFuelLevel() + 10; // Extra 10% buffer
                System.out.println("  Needs refueling: " + String.format("%.2f", fuelToAdd) + "%");
                vehicle.refuel(fuelToAdd);
            }
            System.out.println();
        }
    }
    
    public void showFleetStatus() {
        System.out.println("\n=== Fleet Status ===");
        for (Vehicle vehicle : fleet) {
            System.out.println(vehicle.getVehicleInfo() + ":");
            System.out.println("  Status: " + (vehicle.isStarted() ? "RUNNING" : "STOPPED"));
            System.out.println("  Fuel/Battery: " + String.format("%.2f", vehicle.getFuelLevel()) + "%");
            System.out.println("  Max Speed: " + vehicle.getMaxSpeed() + " km/h");
            System.out.println();
        }
    }
    
    // Demonstrates that LSP allows us to treat all vehicles uniformly
    public void performMaintenance() {
        System.out.println("\n=== Performing Maintenance ===");
        for (Vehicle vehicle : fleet) {
            // Stop vehicle for maintenance
            vehicle.stop();
            
            // Refuel/recharge
            vehicle.refuel(100.0 - vehicle.getFuelLevel());
            
            System.out.println(vehicle.getVehicleInfo() + " maintenance completed.");
        }
    }
}

// Main class demonstrating LSP and Polymorphism
public class substituin {
    public static void main(String[] args) {
        System.out.println("=== Liskov Substitution Principle (LSP) Demo ===");
        System.out.println("Demonstrating how subclasses can be substituted for their parent class\n");
        
        // Create a fleet manager
        VehicleFleetManager fleetManager = new VehicleFleetManager();
        
        // Create different types of vehicles (LSP: all can be treated as Vehicle)
        Vehicle car1 = new Car("Toyota", "Camry", 4, false);
        Vehicle car2 = new Car("BMW", "7 Series", 4, true);
        Vehicle truck = new Truck("Volvo", "FH16", 40.0);
        Vehicle motorcycle = new Motorcycle("Harley-Davidson", "Street Glide", false);
        Vehicle electricCar = new ElectricCar("Tesla", "Model S", 100.0);
        
        // Add all vehicles to fleet (LSP in action: treating all as Vehicle)
        fleetManager.addVehicle(car1);
        fleetManager.addVehicle(car2);
        fleetManager.addVehicle(truck);
        fleetManager.addVehicle(motorcycle);
        fleetManager.addVehicle(electricCar);
        
        // Show initial fleet status
        fleetManager.showFleetStatus();
        
        // Start all vehicles (polymorphism: each implements start() differently)
        fleetManager.startAllVehicles();
        
        // Test acceleration (polymorphism in action)
        fleetManager.testAcceleration(80.0);
        
        // Plan a trip (demonstrates LSP: same interface, different implementations)
        fleetManager.planTrip(500.0);
        
        // Test high-speed acceleration
        fleetManager.testAcceleration(150.0);
        
        // Perform maintenance on all vehicles
        fleetManager.performMaintenance();
        
        // Final fleet status
        fleetManager.showFleetStatus();
        
        // Demonstrate specific vehicle capabilities (while maintaining LSP)
        System.out.println("\n=== Demonstrating Specific Vehicle Features ===");
        
        // Cast to specific types to access unique methods (when needed)
        if (car1 instanceof Car) {
            ((Car) car1).openTrunk();
        }
        
        if (truck instanceof Truck) {
            Truck t = (Truck) truck;
            t.loadCargo(15.0);
            t.loadCargo(30.0); // This should exceed capacity
        }
        
        if (motorcycle instanceof Motorcycle) {
            ((Motorcycle) motorcycle).wheelie();
        }
        
        if (electricCar instanceof ElectricCar) {
            ((ElectricCar) electricCar).enableAutopilot();
        }
        
        System.out.println("\n=== LSP Summary ===");
        System.out.println("✅ All vehicle subclasses can be used wherever Vehicle is expected");
        System.out.println("✅ Each subclass maintains the contract of the Vehicle interface");
        System.out.println("✅ Polymorphism allows different implementations while maintaining substitutability");
        System.out.println("✅ The fleet manager works with any Vehicle without knowing the specific type");
        System.out.println("✅ Adding new vehicle types doesn't break existing code (Open/Closed Principle)");
    }
}
