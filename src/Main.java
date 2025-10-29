import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Simple Hotel");

        // seed a few rooms (you can remove or change these)
        hotel.addRoom(new Room(101, "Single", 60.0));
        hotel.addRoom(new Room(102, "Double", 85.0));
        hotel.addRoom(new Room(201, "Suite", 150.0));

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to " + hotel.getName());
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) List rooms");
            System.out.println("2) Check available rooms");
            System.out.println("3) Book a room");
            System.out.println("4) List reservations");
            System.out.println("5) Cancel reservation");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    for (Room r : hotel.getAllRooms()) {
                        System.out.println(r);
                    }
                    break;

                case "2":
                    LocalDate in = readDate(sc, "Check-in (yyyy-MM-dd): ");
                    LocalDate out = readDate(sc, "Check-out (yyyy-MM-dd): ");
                    List<Room> available = hotel.listAvailableRooms(in, out);
                    if (available.isEmpty()) {
                        System.out.println("No rooms available.");
                    } else {
                        System.out.println("Available rooms:");
                        for (Room r : available) System.out.println(r);
                    }
                    break;

                case "3":
                    System.out.print("Customer name: ");
                    String name = sc.nextLine().trim();
                    int roomNo = readInt(sc, "Room number: ");
                    LocalDate cin = readDate(sc, "Check-in (yyyy-MM-dd): ");
                    LocalDate cout = readDate(sc, "Check-out (yyyy-MM-dd): ");
                    var res = hotel.bookRoom(roomNo, name, cin, cout);
                    if (res != null) {
                        System.out.println("Booked! " + res);
                    } else {
                        System.out.println("Booking failed.");
                    }
                    break;

                case "4":
                    List<Reservation> all = hotel.getAllReservations();
                    if (all.isEmpty()) System.out.println("No reservations.");
                    else for (Reservation r : all) System.out.println(r);
                    break;

                case "5":
                    int id = readInt(sc, "Reservation ID to cancel: ");
                    boolean ok = hotel.cancelReservation(id);
                    System.out.println(ok ? "Cancelled." : "Reservation not found.");
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
        System.out.println("Bye!");
    }

    private static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = sc.nextLine().trim();
                return LocalDate.parse(s, FMT);
            } catch (Exception e) {
                System.out.println("Invalid date. Use yyyy-MM-dd.");
            }
        }
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
