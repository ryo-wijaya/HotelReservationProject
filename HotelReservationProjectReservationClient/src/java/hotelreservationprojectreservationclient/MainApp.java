/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectreservationclient;

import ejb.session.stateful.HotelReservationBeanRemote;
import ejb.session.stateless.BookingSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Booking;
import entity.Customer;
import entity.RoomType;
import java.text.ParseException;
import entity.Room;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exceptions.BookingNotFoundException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author Jorda
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private HotelReservationBeanRemote hotelReservationBeanRemote;

    private Customer currentCustomer;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, BookingSessionBeanRemote bookingSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, HotelReservationBeanRemote hotelReservationBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.hotelReservationBeanRemote = hotelReservationBeanRemote;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HoRS Reservation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful as " + currentCustomer.getUserName() + "!\n");
                        mainMenu();
                    } catch (LoginCredentialsInvalidException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    registerGuest();
                } else if (response == 3) {
                    searchHotelRoom();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HoRS Reservation Client ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: Reserve Hotel Room");
            System.out.println("3: View My Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Exit\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    searchHotelRoom();
                } else if (response == 2) {
                    reserveHotelRoom();
                } else if (response == 3) {
                    viewMyReservationDetails();
                } else if (response == 4) {
                    viewAllMyReservations();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    public void doLogin() throws LoginCredentialsInvalidException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Welcome to HoRS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new LoginCredentialsInvalidException("Missing login credential!");
        }
    }

    public void registerGuest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** POS System :: System Administration :: Create New Staff ***\n");
        Customer customer = new Customer();
        System.out.print("Enter Email> ");
        customer.setEmail(sc.nextLine().trim());
        System.out.print("Enter Username> ");
        customer.setUserName(sc.nextLine().trim());
        System.out.print("Enter Password> ");
        customer.setPassword(sc.nextLine().trim());
        System.out.print("Enter PhoneNumber> ");
        customer.setPhoneNumber(sc.nextLine().trim());
        System.out.print("Enter Passport Number> ");
        customer.setPassportNumber(sc.nextLine().trim());

        long newCustomerId = customerSessionBeanRemote.registerAsCustomer(customer);
        System.out.println("Customer created successfully!: " + newCustomerId + "\n");
    }

    public Booking searchHotelRoom() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("\nYou are now searching a Room for a online guest");
            System.out.println("-----------------------------------------------\n");
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

            Double price = bookingSessionBeanRemote.getRateForOnlineBooking(booking);

            System.out.println("\n Price for a booking like this would be: " + price + "\n");
            return booking;

        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type not found!");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Room Rate not found!");
        }
        return null;
    }

    public void reserveHotelRoom() {
        try {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            System.out.println("\nYou are now reserving a Hotel Room");
            System.out.println("------------------------------------\n");
            Booking availableBooking = searchHotelRoom();
            RoomType roomType = availableBooking.getRoomType();
            Date checkIn = availableBooking.getCheckInDate();
            Date checkOut = availableBooking.getCheckOutDate();
            Integer numOfRoom = availableBooking.getNumberOfRooms();
            Booking booking = new Booking(numOfRoom, checkIn, checkOut);
            long bookingId = bookingSessionBeanRemote.createNewBookingWithCustomer(booking, roomType.getRoomTypeId(), currentCustomer.getCustomerId());
            System.out.println("What is todays date? (dd/mm/yyyy)> ");
            Date cDate = inputDateFormat.parse(sc.nextLine().trim());
            System.out.println("What time is the reservation made");
            Double rtime = sc.nextDouble();
            if(booking.getCheckInDate().equals(cDate) && rtime >= 2){
                roomSessionBeanRemote.findARoomAndAddToIt(bookingId);
            }
            System.out.println("Hotel room(s) successfully reserved!");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room type not found!");
        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer not found!");
        } catch (EntityInstanceExistsInCollectionException ex) {
            System.out.println("customer not found!");
        } catch (ParseException ex) {
            System.out.println("invalid date!");
        } catch (RoomNotFoundException ex) {
            System.out.println("Room not found!");
        } catch (BookingNotFoundException ex) {
            System.out.println("Booking not found!");
        }
    }

    public void viewMyReservationDetails() {
        try {
            Scanner sc = new Scanner(System.in);
            Long bookingId;
            System.out.println("\nViewing my reservation details!");
            System.out.println("-------------------------------\n");
            List<Booking> bookings = customerSessionBeanRemote.retrieveCustomerByCustomerId(currentCustomer.getCustomerId()).getBookings();
            if (bookings.isEmpty()) {
                System.out.print("No existing reservations!\n");
                return;
            }
            System.out.print("Enter a Booking ID>");
            while (true) {
                try {
                    bookingId = Long.parseLong(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    System.out.println("Please input a valid ID format");
                }
            }
            for (Booking booking : bookings) {
                if (Objects.equals(booking.getBookingId(), bookingId)) {
                    System.out.println("Booking Id: " + booking.getBookingId());
                    System.out.println("Check In Date: " + booking.getCheckInDate());
                    System.out.println("Check Out Date: " + booking.getCheckOutDate());
                    System.out.println("Room Type: " + booking.getRoomType());
                    System.out.println("Number of rooms: " + booking.getNumberOfRooms());
                }
            }
        } catch (CustomerNotFoundException ex) {
            System.out.println("customer not found");
        }
    }

    public void viewAllMyReservations() {
        System.out.println("\nViewing all my reservations!");
        System.out.println("----------------------------\n");
        //List<Booking> bookings = bookingSessionBeanRemote.getAllBookingsByCustomerId(currentCustomer.getCustomerId());
        List<Booking> bookings = currentCustomer.getBookings();
        if (bookings.isEmpty()) {
            System.out.print("No existing reservations!\n");
            return;
        }
        for (Booking b : bookings) {
            System.out.println("Booking ID: " + b.getBookingId());
            System.out.println("Start Date: " + b.getCheckInDate());
            System.out.println("End Date: " + b.getCheckOutDate());
            System.out.println("Room Type: " + b.getRoomType());
            System.out.println("Number of rooms: " + b.getNumberOfRooms());
            System.out.println("");
        }
    }
}
