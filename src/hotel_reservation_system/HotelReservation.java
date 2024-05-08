package hotel_reservation_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservation {

	private static final String url = "jdbc:mysql://127.0.0.1:3306/jspiders";
	private static final String userName = "root";
	private static final String password = "Password@1234";

	// Main Method
	public static void main(String[] args) {

		// STEP 1 : Loading the Driver.....
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			// STEP 2 : Establishing the Connection.....
			Connection connection = DriverManager.getConnection(url, userName, password);
			Statement statement = connection.createStatement();

			System.out.println("Connection successful!");
			while (true) {
				System.out.println();
				System.out.println("HOTEL MANAGEMENT SYSTEM");
				System.out.println();
				System.out.println("1. Reserve a room");
				System.out.println("2. View Reservations");
				System.out.println("3. Get Room Number");
				System.out.println("4. Update Reservations");
				System.out.println("5. Delete Reservations");
				System.out.println("0. Exit");
				System.out.print("Choose an option: ");

				Scanner scanner = new Scanner(System.in);
				int choice = scanner.nextInt();

				switch (choice) {
				case 1:
					reserveRoom(connection, statement, scanner);
					break;
				case 2:
					viewReservations(connection, statement);
					break;
				case 3:
					getRoomNumber(connection, statement, scanner);
					break;
				case 4:
					updateReservation(connection, statement, scanner);
					break;
				case 5:
					deleteReservation(connection, statement, scanner);
					break;
				case 0:
					scanner.close();
					connection.close();
					statement.close();
					exit();
					return;
				default:
					System.out.println("Invalid choice. Try again.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	// Method
	private static void reserveRoom(Connection connection, Statement statement, Scanner scanner) throws SQLException {
		System.out.print("Enter The Guest Name.  :  ");
		String guestName = scanner.next();
		scanner.nextLine();
		System.out.print("Enter contact number: ");
		long contactNumber = scanner.nextLong();
		// scanner.next();

		System.out.print("Enter room number: ");
		int roomNumber = scanner.nextInt();

		String insertQuery = "INSERT INTO reservations (guest_name, contact_number, room_number) " + "VALUES ('"
				+ guestName + "', " + contactNumber + ", " + roomNumber + ")";

		int affectedRows = statement.executeUpdate(insertQuery);
		if (affectedRows > 0) {
			System.out.println("Reservation successful!");
		} else {
			System.out.println("Reservation failed.");
		}
	}

	// Method
	private static void viewReservations(Connection connection, Statement statement) throws SQLException {
		String sql = "SELECT reservation_id, guest_name, room_number, contact_number from reservations";

		try (ResultSet resultSet = statement.executeQuery(sql)) {
			System.out.println("Current Reservations:");
			System.out.println("+----------------+-----------------+---------------+-----------------+");
			System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number  |");
			System.out.println("+----------------+-----------------+---------------+-----------------+");

			while (resultSet.next()) {
				int reservationID = resultSet.getInt(1);
				String guestName = resultSet.getString(2);
				int roomNumber = resultSet.getInt(3);
				String contactNumber = resultSet.getString(4);

				System.out.printf("| %-14d | %-15s | %-13d | %-20s |\n", reservationID, guestName, roomNumber,
						contactNumber);
				System.out.println("+----------------+-----------------+---------------+----------------------+");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method
	private static void getRoomNumber(Connection connection, Statement statement, Scanner scanner) {

		System.out.println("Enter Reservation ID");
		int reservationID = scanner.nextInt();

		String sql = "SELECT room_number , guest_name from reservations where reservation_id = " + reservationID;

		try (ResultSet resultSet = statement.executeQuery(sql)) {

			if (resultSet.next()) {
				int roomNumber = resultSet.getInt("room_number");
				String name = resultSet.getString("guest_name");
//				String Name  = resultSet.getString(guest_name);
				System.out.println("Room number for " + reservationID + " : " + roomNumber + "\nGuest Name for "
						+ reservationID + " : " + name);
			} else {
				System.out.println("Reservation not found for the given ID and guest name.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method
	private static void updateReservation(Connection connection, Statement statement, Scanner scanner)
			throws SQLException {
		System.out.print("Enter Reservation ID to update  :   ");
		int reservationID = scanner.nextInt();
		scanner.nextLine(); // Consume the newline character

		try {
			if (!reservationExists(connection, statement, reservationID)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}
			System.out.print("Enter new guest name: ");
			String newGuestName = scanner.nextLine();
//	        System.out.print("Enter new room number: ");
//	        int newRoomNumber = scanner.nextInt();
//	        System.out.print("Enter new contact number: ");
//	        String newContactNumber = scanner.next();

			String SQl = "UPDATE reservations SET guest_name = '" + newGuestName + "'where reservation_id = "
					+ reservationID;
			int affectedRows = statement.executeUpdate(SQl);
			if (affectedRows > 0) {
				System.out.println("Reservation updated successfully!");
			} else {
				System.out.println("Reservation update failed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method
	private static boolean reservationExists(Connection connection, Statement statement, int reservationID) {
		try {
			String sql = "SELECT reservation_id from reservations where reservation_id = " + reservationID;
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet.next(); // If there's a result, the reservation exists
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Handle database errors as needed
		}
	}

	// Method
	private static void deleteReservation(Connection connection, Statement statement, Scanner scanner) {
		try {
			System.out.print("Enter reservation ID to delete: ");
			int reservationId = scanner.nextInt();

			if (!reservationExists(connection, statement, reservationId)) {
				System.out.println("Reservation not found for the given ID.");
				return;
			}

			String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;
			int affectedRows = statement.executeUpdate(sql);

			if (affectedRows > 0) {
				System.out.println("Reservation deleted successfully!");
			} else {
				System.out.println("Reservation deletion failed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Method
	public static void exit() throws InterruptedException {
		System.out.print("Exiting System");
		int i = 10;
		while (i != 0) {
			System.out.print(".");
			Thread.sleep(500);
			i--;
		}
		System.out.println();
		System.out.println("ThankYou For Using Hotel Reservation System!!!");

	}
}