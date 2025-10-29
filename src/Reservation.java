import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private String customerName;
    private int roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Reservation(int reservationId, String customerName, int roomNumber,
                       LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // simple overlap check: true if date ranges intersect
    public boolean overlaps(LocalDate in, LocalDate out) {
        // assume checkOut is the day the guest leaves (not staying the night)
        return !(out.isEqual(checkIn) || out.isBefore(checkIn)
              || in.isEqual(checkOut) || in.isAfter(checkOut));
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + reservationId +
                ", customer='" + customerName + '\'' +
                ", room=" + roomNumber +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}
