
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class UiApp extends Application {

    private final Hotel hotel = new Hotel("Simple Hotel");

    // UI widgets shared across sections
    private final TableView<Room> roomsTable = new TableView<>();
    private final TableView<Reservation> reservationsTable = new TableView<>();

    @Override
    public void start(Stage stage) {
        // seed a few rooms for quick testing
        hotel.addRoom(new Room(101, "Single", 60.0));
        hotel.addRoom(new Room(102, "Double", 85.0));
        hotel.addRoom(new Room(201, "Suite", 150.0));

        TabPane tabs = new TabPane(
                addRoomTab(),
                bookRoomTab(),
                cancelReservationTab(),
                availableRoomsTab(),
                reservationsTab()
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        var root = new BorderPane(tabs);
        root.setPadding(new Insets(12));
        stage.setTitle("Hotel Reservation (Frontend)");
        stage.setScene(new Scene(root, 900, 560));
        stage.show();

        refreshRoomsTable();
        refreshReservationsTable();
    }

    // ---------- Tabs ----------

    private Tab addRoomTab() {
        TextField tfNumber = new TextField(); tfNumber.setPromptText("Room number");
        TextField tfType = new TextField(); tfType.setPromptText("Room type (Single/Double/Suite)");
        TextField tfPrice = new TextField(); tfPrice.setPromptText("Price per night");
        Button btnAdd = new Button("Add Room");

        Label msg = new Label();

        btnAdd.setOnAction(e -> {
            try {
                int number = Integer.parseInt(tfNumber.getText().trim());
                String type = tfType.getText().trim();
                double price = Double.parseDouble(tfPrice.getText().trim());

                if (type.isEmpty() || price <= 0) {
                    msg.setText("Invalid inputs.");
                    return;
                }
                boolean ok = hotel.addRoom(new Room(number, type, price));
                if (ok) {
                    msg.setText("Room added.");
                    tfNumber.clear(); tfType.clear(); tfPrice.clear();
                    refreshRoomsTable();
                } else {
                    msg.setText("Room number already exists.");
                }
            } catch (Exception ex) {
                msg.setText("Please enter valid values (number / price).");
            }
        });

        GridPane form = grid(2, 8);
        form.add(new Label("Room number:"), 0, 0); form.add(tfNumber, 1, 0);
        form.add(new Label("Room type:"),   0, 1); form.add(tfType,   1, 1);
        form.add(new Label("Price/night:"), 0, 2); form.add(tfPrice,  1, 2);
        form.add(btnAdd, 1, 3); form.add(msg, 1, 4);

        VBox box = new VBox(12, form, titled("Rooms", roomsTable));
        VBox.setVgrow(roomsTable, Priority.ALWAYS);
        return new Tab("Add room", box);
    }

    private Tab bookRoomTab() {
        TextField tfCustomer = new TextField(); tfCustomer.setPromptText("Customer name");
        TextField tfRoomNo = new TextField(); tfRoomNo.setPromptText("Room number");
        DatePicker dpIn = new DatePicker(); dpIn.setPromptText("Check-in");
        DatePicker dpOut = new DatePicker(); dpOut.setPromptText("Check-out");
        Button btnBook = new Button("Book Room");
        Label msg = new Label();

        btnBook.setOnAction(e -> {
            try {
                String customer = tfCustomer.getText().trim();
                int roomNo = Integer.parseInt(tfRoomNo.getText().trim());
                LocalDate in = dpIn.getValue();
                LocalDate out = dpOut.getValue();

                if (customer.isEmpty() || in == null || out == null) {
                    msg.setText("Please fill all fields.");
                    return;
                }
                if (hotel.getRoomByNumber(roomNo) == null) {
                    msg.setText("Room not found.");
                    return;
                }
                var res = hotel.bookRoom(roomNo, customer, in, out);
                if (res != null) {
                    msg.setText("Booked! " + res);
                    refreshReservationsTable();
                } else {
                    msg.setText("Booking failed (dates/availability).");
                }
            } catch (Exception ex) {
                msg.setText("Please check values (room number / dates).");
            }
        });

        GridPane form = grid(2, 8);
        form.add(new Label("Customer:"), 0, 0); form.add(tfCustomer, 1, 0);
        form.add(new Label("Room no.:"), 0, 1); form.add(tfRoomNo,   1, 1);
        form.add(new Label("Check-in:"), 0, 2); form.add(dpIn,       1, 2);
        form.add(new Label("Check-out:"),0, 3); form.add(dpOut,      1, 3);
        form.add(btnBook, 1, 4); form.add(msg, 1, 5);

        VBox box = new VBox(12, form, titled("Rooms (for reference)", roomsTable));
        VBox.setVgrow(roomsTable, Priority.ALWAYS);
        return new Tab("Book room", box);
    }

    private Tab cancelReservationTab() {
        TextField tfId = new TextField(); tfId.setPromptText("Reservation ID");
        Button btnCancel = new Button("Cancel");
        Label msg = new Label();

        btnCancel.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfId.getText().trim());
                boolean ok = hotel.cancelReservation(id);
                msg.setText(ok ? "Cancelled." : "Reservation not found.");
                if (ok) refreshReservationsTable();
            } catch (Exception ex) {
                msg.setText("Please enter a valid reservation ID.");
            }
        });

        GridPane form = grid(2, 8);
        form.add(new Label("Reservation ID:"), 0, 0); form.add(tfId, 1, 0);
        form.add(btnCancel, 1, 1); form.add(msg, 1, 2);

        VBox box = new VBox(12, form, titled("Reservations", reservationsTable));
        VBox.setVgrow(reservationsTable, Priority.ALWAYS);
        return new Tab("Cancel reservation", box);
    }

    private Tab availableRoomsTab() {
        DatePicker dpIn = new DatePicker(); dpIn.setPromptText("Check-in");
        DatePicker dpOut = new DatePicker(); dpOut.setPromptText("Check-out");
        Button btnCheck = new Button("Check availability");
        Label msg = new Label();
        TableView<Room> availableTable = new TableView<>();

        setupRoomsTable(availableTable);

        btnCheck.setOnAction(e -> {
            LocalDate in = dpIn.getValue();
            LocalDate out = dpOut.getValue();
            if (in == null || out == null) {
                msg.setText("Please select both dates.");
                return;
            }
            List<Room> available = hotel.listAvailableRooms(in, out);
            availableTable.getItems().setAll(available);
            msg.setText(available.isEmpty() ? "No rooms available." : "Found " + available.size() + " room(s).");
        });

        GridPane form = grid(2, 8);
        form.add(new Label("Check-in:"), 0, 0); form.add(dpIn,  1, 0);
        form.add(new Label("Check-out:"),0, 1); form.add(dpOut, 1, 1);
        form.add(btnCheck, 1, 2); form.add(msg, 1, 3);

        VBox box = new VBox(12, form, titled("Available rooms", availableTable));
        VBox.setVgrow(availableTable, Priority.ALWAYS);
        return new Tab("View available rooms", box);
    }

    private Tab reservationsTab() {
        VBox box = new VBox(12, titled("All reservations", reservationsTable));
        VBox.setVgrow(reservationsTable, Priority.ALWAYS);
        return new Tab("Reservations", box);
    }

    // ---------- Helpers ----------

    private TitledPane titled(String title, TableView<?> table) {
        var pane = new TitledPane(title, table);
        pane.setCollapsible(false);
        return pane;
    }

    private GridPane grid(int hgap, int vgap) {
        GridPane gp = new GridPane();
        gp.setHgap(hgap);
        gp.setVgap(vgap);
        gp.setPadding(new Insets(8));
        return gp;
    }

    private void refreshRoomsTable() {
        setupRoomsTable(roomsTable);
        roomsTable.getItems().setAll(hotel.getAllRooms());
    }

    private void refreshReservationsTable() {
        setupReservationsTable(reservationsTable);
        reservationsTable.getItems().setAll(hotel.getAllReservations());
    }

    private void setupRoomsTable(TableView<Room> table) {
        table.getColumns().clear();
        TableColumn<Room, Number> cNum = new TableColumn<>("Number");
        cNum.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRoomNumber()));
        TableColumn<Room, String> cType = new TableColumn<>("Type");
        cType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRoomType()));
        TableColumn<Room, Number> cPrice = new TableColumn<>("Price/night");
        cPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPricePerNight()));
        table.getColumns().addAll(cNum, cType, cPrice);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupReservationsTable(TableView<Reservation> table) {
        table.getColumns().clear();
        TableColumn<Reservation, Number> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getReservationId()));
        TableColumn<Reservation, String> cName = new TableColumn<>("Customer");
        cName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCustomerName()));
        TableColumn<Reservation, Number> cRoom = new TableColumn<>("Room");
        cRoom.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getRoomNumber()));
        TableColumn<Reservation, String> cIn = new TableColumn<>("Check-in");
        cIn.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCheckIn().toString()));
        TableColumn<Reservation, String> cOut = new TableColumn<>("Check-out");
        cOut.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCheckOut().toString()));
        table.getColumns().addAll(cId, cName, cRoom, cIn, cOut);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
