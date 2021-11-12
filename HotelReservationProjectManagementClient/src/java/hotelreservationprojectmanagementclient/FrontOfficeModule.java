/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateful.HotelReservationBeanRemote;
import ejb.session.stateless.BookingSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Booking;
import entity.Employee;
import entity.Room;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exceptions.BookingNotFoundException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
public class FrontOfficeModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private HotelReservationBeanRemote hotelReservationBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;
    private Employee employee;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote,
            RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote,
            HotelReservationBeanRemote hotelReservationBeanRemote, BookingSessionBeanRemote bookingSessionBeanRemote, Employee employee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
        this.hotelReservationBeanRemote = hotelReservationBeanRemote;
        this.employee = employee;
    }

    public void runMainMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        boolean logout = false;
        while (!logout) {
            System.out.println("\n-You are currently logged in as a Guest Relation Officer-");
            System.out.println("---------------------------------------------------------\n");
            System.out.println("1. Walk-in Search Room");
            System.out.println("2. Walk-in Reserver Room");
            System.out.println("3. Check-in Guest");
            System.out.println("4. Check-out Guest");
            System.out.println("5. Logout");

            System.out.print("Please select an option>");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                choice = 404;
            }

            switch (choice) {
                case 1:
                    this.walkInSearchRoom(sc);
                    break;
                case 2:
                    this.walkInReserveRoom(sc);
                    break;
                case 3:
                    this.checkInGuest(sc);
                    break;
                case 4:
                    this.checkoutGuest(sc);
                    break;
                case 5:
                    logout = true;
                    break;
                default:
                    System.out.println("Please input a valid choice!");
            }
        }
    }

    private Booking walkInSearchRoom(Scanner sc) {
        try {
            System.out.println("\nYou are now searching a Room for a walk-in customer");
            System.out.println("---------------------------------------------------\n");
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Date startDateString;
            Date endDateString;
            String roomTypeName;
            RoomType roomType;
            int numOfRooms = 0;
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            startDateString = inputDateFormat.parse(sc.nextLine().trim());
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            endDateString = inputDateFormat.parse(sc.nextLine().trim());

            if (startDateString.compareTo(endDateString) > 0) {
                System.out.println("Invalid Operation - start date exceed end date");
                System.out.println("Cancelling Operation...");
                return null;
            }

            // This map contains key value pairs of RoomTypeIds to QuantityOfRoomsAvailable
            HashMap<Long, Integer> map = roomSessionBeanRemote.walkInSearchRoom(startDateString, endDateString);

            //Iterating over each Room Type and Inventory mapping
            for (Map.Entry<Long, Integer> pair : map.entrySet()) {
                System.out.println("Room Type: " + roomTypeSessionBeanRemote.getRoomTypeById(pair.getKey()).getRoomName() + " | "
                        + "Number Of Rooms Left: " + pair.getValue());
            }

            System.out.println("\nInput a Room Type Name> ");
            roomTypeName = sc.nextLine().trim();
            RoomType realRoomType = roomTypeSessionBeanRemote.getRoomTypeByName(roomTypeName);
            if("None".equals(roomTypeName)) {
                return null;
            }

            System.out.print("Input the number of rooms you want (that are of this Room Type)> ");

            while (numOfRooms != 404) {
                try {
                    numOfRooms = Integer.parseInt(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    numOfRooms = 404;
                    System.out.println("Enter a valid number!");
                }
            }

            if (numOfRooms > map.get(realRoomType.getRoomTypeId())) { //checking if the user chose a room number not exceeding the available room type
                return null;
            }

            Booking booking = new Booking(numOfRooms, startDateString, endDateString);
            booking.setRoomType(realRoomType);

            Double price = bookingSessionBeanRemote.getPublishRatePriceOfBooking(booking);

            System.out.println("\n Price for a booking like this would be: " + price + "\n");
            return booking;
            
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type not found!");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Room Rate Not Found!");
        }
        return null;
    }

    private void walkInReserveRoom(Scanner sc) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        try {
            System.out.println("\nYou are now reserving a Room for a walk-in customer");
            System.out.println("---------------------------------------------------\n");
            Booking availableBooking = walkInSearchRoom(sc);
            System.out.println("Please enter a room type name> ");
            RoomType roomType = roomTypeSessionBeanRemote.getRoomTypeByName(sc.nextLine().trim());
            Date checkIn = availableBooking.getCheckInDate();
            Date checkOut = availableBooking.getCheckOutDate();
            Integer numOfRoom = availableBooking.getNumberOfRooms();
            Booking booking = new Booking(numOfRoom, checkIn, checkOut);
            long bookingId = bookingSessionBeanRemote.createNewBooking(booking, roomType.getRoomTypeId());
            System.out.println("What is todays date? (dd/mm/yyyy)> ");
            Date cDate = inputDateFormat.parse(sc.nextLine().trim());
            System.out.println("What time is the reservation made");
            Double rtime = sc.nextDouble();
            if(booking.getCheckInDate().equals(cDate) && rtime >= 2){
                roomSessionBeanRemote.findARoomAndAddToIt(bookingId);
            }
            System.out.println("Hotel room(s) successfully reserved!");
            System.out.println("Your Boooking Id is " + bookingId + "\n");
            /*List<Booking> availableRooms = walkInSearchRoom(sc);
            Integer option = 0;
            for(Booking bookings : availableRooms) {
            System.out.println("\nOption " + (option + 1) + ".");
            System.out.println("Rate Type name: " + bookings.getRoomType().getRoomName());
            }
            Integer response = 0;
            while (true) {
            try {
            System.out.print("Please Select an Option given above> ");
            response = sc.nextInt();
            if (response < 1 || response > availableRooms.size()) {
            System.out.print("Invalid input> ");
            } else {
            RoomType roomType = availableRooms.get(response - 1).getRoomType();
            Date checkIn = availableRooms.get(response - 1).getCheckInDate();
            Date checkOut = availableRooms.get(response - 1).getCheckInDate();
            Integer numOfRoom = availableRooms.get(response - 1).getNumberOfRooms();
            Booking booking = new Booking(numOfRoom, checkIn, checkOut);
            bookingSessionBeanRemote.createNewBooking(booking, roomType.getRoomTypeId());
            }
            }
            catch (RoomTypeNotFoundException ex) {
            System.out.print("Invalid Room Type!");
            }
            }*/
        } catch (RoomTypeNotFoundException ex) {
            System.out.print("Invalid Room Type!");
        } catch (ParseException ex) {
            System.out.print("Invalid Date!");
        } catch (RoomNotFoundException ex) {
            System.out.print("Room not found!");
        } catch (BookingNotFoundException ex) {
            System.out.print("Booking not found!");
        }
    }

    private void checkInGuest(Scanner sc) {
        try {
            System.out.println("\nYou are now checking in a Guest");
            System.out.println("-------------------------------\n");
            System.out.println("Please Enter the Booking Id");
            long bookingId = 0;

            while (bookingId != 404) {
                try {
                    bookingId = Long.parseLong(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Enter a valid format for booking ID");
                }
            }

            Booking booking = bookingSessionBeanRemote.retrieveBookingByBookingId(bookingId);
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Check in Date: " + booking.getCheckInDate());
            System.out.println("Check out Date: " + booking.getCheckOutDate());
            List<Room> rooms = booking.getRooms();
            for (Room room : rooms) {
                System.out.println("Room Number: " + room.getRoomNumber());
                room.setRoomStatus(Boolean.FALSE);
                roomSessionBeanRemote.updateRoom(room);
            }
            System.out.println("Check in completed!");
            System.out.println("-----------------\n");
        } catch (BookingNotFoundException ex) {
            System.out.println("Booking does not exists!");
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        }
    }

    private void checkoutGuest(Scanner sc) {
        try {
            System.out.println("\nYou are now checking out a Guest");
            System.out.println("--------------------------------\n");
            System.out.println("Please Enter the Booking Id");

            long bookingId = 0;
            while (bookingId != 404) {
                try {
                    bookingId = Long.parseLong(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Enter a valid format for booking ID");
                }
            }

            Booking booking = bookingSessionBeanRemote.retrieveBookingByBookingId(bookingId);
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Check in Date: " + booking.getCheckInDate());
            System.out.println("Check out Date: " + booking.getCheckOutDate());
            List<Room> rooms = booking.getRooms();
            for (Room room : rooms) {
                room.setRoomStatus(Boolean.TRUE);
                roomSessionBeanRemote.updateRoom(room);
            }
            System.out.println("Check out completed!");
            System.out.println("------------------\n");
        } catch (BookingNotFoundException ex) {
            System.out.println("Booking does not exists!");
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        }
    }
}
