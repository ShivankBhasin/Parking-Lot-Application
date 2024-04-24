import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkingApp {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        ParkingManager parkingManager = new ParkingManager();

        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            while (!"exit".equals(input)) {
                parkingManager.processCommand(input);
                input = scanner.nextLine();
            }
        } else {
            for (String arg : args) {
                parkingManager.processCommand(arg);
            }
        }
    }
}

class ParkingManager {
    private final ParkingLot parkingLot;

    public ParkingManager() {
        
        int defaultCapacity = 10;
        this.parkingLot = new ParkingLot(defaultCapacity);
    }

    public void processCommand(String input) {
        try {
            CommandParser commandParser = new CommandParser(input);
            switch (commandParser.getCommand()) {
                case "create_parking_lot":
                    int capacity = commandParser.getIntParameter();
                    parkingLot.createParkingLot(capacity);
                    break;
                case "park":
                    String regNo = commandParser.getStringParameter(1);
                    String color = commandParser.getStringParameter(2);
                    parkingLot.parkCar(regNo, color);
                    break;
                case "leave":
                    int slotNumber = commandParser.getIntParameter();
                    parkingLot.leave(slotNumber);
                    break;
                case "status":
                    parkingLot.display();
                    break;
                case "registration_numbers_for_cars_with_colour":
                    parkingLot.registrationNumbersForColor(commandParser.getStringParameter());
                    break;
                case "slot_numbers_for_cars_with_colour":
                    parkingLot.slotNumbersForColor(commandParser.getStringParameter());
                    break;
                case "slot_number_for_registration_number":
                    parkingLot.slotNumberForRegistrationNumber(commandParser.getStringParameter());
                    break;
                default:
                    System.out.println("Incorrect command");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " Incorrect Command");
        }
    }
}

class CommandParser {
    private final Pattern commandPattern = Pattern.compile("^([\\S]+)\\s*(.*)$");
    private final String command;
    private final String[] parameters;

    public CommandParser(String input) {
        Matcher matcher = commandPattern.matcher(input);
        if (matcher.matches()) {
            this.command = matcher.group(1);
            this.parameters = matcher.group(2).split("\\s+");
        } else {
            throw new IllegalArgumentException("Invalid command format");
        }
    }

    public String getCommand() {
        return command;
    }

    public String getStringParameter() {
        return parameters.length > 0 ? parameters[0] : null;
    }

    public String getStringParameter(int index) {
        return index < parameters.length ? parameters[index] : null;
    }

    public int getIntParameter() {
        return parameters.length > 0 ? Integer.parseInt(parameters[0]) : 0;
    }
}

class ParkingLot {
    private int capacity;
    private TreeMap<Integer, Car> parkingMap;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.parkingMap = new TreeMap<>();
    }

    public void createParkingLot(int capacity) {
        this.capacity = capacity;
        this.parkingMap.clear(); // Clear existing data if any
        System.out.println("Parking lot created with capacity: " + capacity);
    }

    public String parkCar(String registrationNo, String colour) {
        if (parkingMap.size() < capacity) {
            int slotNumber = findNextAvailableSlot();
            Car car = new Car(registrationNo, colour);
            parkingMap.put(slotNumber, car);
            System.out.println("Allocated slot number: " + slotNumber);
            return "Allocated slot number: " + slotNumber;
        } else {
            System.out.println("Sorry, parking lot is full");
            return "Sorry, parking lot is full";
        }
    }

    public String leave(int slotNumber) {
        if (parkingMap.containsKey(slotNumber)) {
            parkingMap.remove(slotNumber);
            System.out.println("Slot number " + slotNumber + " is free");
            return "Slot number " + slotNumber + " is free";
        } else {
            System.out.println("Slot number " + slotNumber + " not found");
            return "Slot number " + slotNumber + " not found";
        }
    }

public String display() {
    StringBuilder result = new StringBuilder("Slot No.\tRegistration No\tColour\n");

    for (Map.Entry<Integer, Car> entry : parkingMap.entrySet()) {
        int slotNumber = entry.getKey();
        Car car = entry.getValue();
        String registrationNo = car.getRegistrationNo() != null ? car.getRegistrationNo() : "null";
        String colour = car.getColour() != null ? car.getColour() : "null";
        result.append(String.format("%-8d%-20s%s%n", slotNumber, registrationNo, colour));
    }

    System.out.print(result);
    return result.toString();
}




    public void registrationNumbersForColor(String color) {
        // Implement this method
        // Print or return registration numbers for cars with the specified color
    }

    public void slotNumbersForColor(String color) {
        // Implement this method
        // Print or return slot numbers for cars with the specified color
    }

    public void slotNumberForRegistrationNumber(String registrationNo) {
        // Implement this method
        // Print or return slot number for the specified registration number
    }

    private int findNextAvailableSlot() {
        int slotNumber = 1;
        while (parkingMap.containsKey(slotNumber)) {
            slotNumber++;
        }
        return slotNumber;
    }
}

class Car {
    private String registrationNo;
    private String colour;

    public Car(String registrationNo, String colour) {
        this.registrationNo = registrationNo;
        this.colour = colour;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getColour() {
        return colour;
    }
}
