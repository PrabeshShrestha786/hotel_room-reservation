import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String name;
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private int nextReservationId = 1;

    public Hotel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // ----- Room management -----
    public boolean addRoom(Room room) {
        // avoid duplicate room numbers
        if (getRoomByNumber(room.getRoomNumber()) != null) {
            return false;
        }
        rooms.add(room);
        return true;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomByNumber(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }

    // ----- Availability -----
    public List<Room> listAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (isRoomAvailable(r.getRoomNumber(), checkIn, checkOut)) {
                available.add(r);
            }
        }
        return available;
    }

    public boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation res : reservations) {
            if (res.getRoomNumber() == roomNumber && res.overlaps(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    // ----- Booking / cancel -----
    public Reservation bookRoom(int roomNumber, String customerName,
                                LocalDate checkIn, LocalDate checkOut) {
        Room room = getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room " + roomNumber + " does not exist.");
            return null;
        }
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            System.out.println("Dates are invalid (check-out must be after check-in).");
            return null;
        }
        if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
            System.out.println("Room " + roomNumber + " is not available for those dates.");
            return null;
        }
        Reservation res = new Reservation(nextReservationId++, customerName, roomNumber, checkIn, checkOut);
        reservations.add(res);
        return res;
    }

    public boolean cancelReservation(int reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId() == reservationId) {
                reservations.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}
