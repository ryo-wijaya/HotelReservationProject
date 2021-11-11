/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojecthrsclient;

import entity.Booking;
import entity.Partner;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import util.enumeration.PartnerType;
import util.exceptions.BookingNotFoundException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
public class HotelReservationProjectHRSClient {

    private static Partner currentPartner;

    public static void main(String[] args) {
        runMainMenu();
    }

    public static void runMainMenu() {

        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\nWelcome to the Holiday Reservation System for Partners!");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            while (true) {
                System.out.print("Enter an option> ");

                try {
                    response = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    response = 404;
                }

                if (response == 1) {
                    try {
                        doLogin();
                        if (currentPartner.getPartnerType() == PartnerType.PARTNEREMPLOYEE) {
                            runPartnerEmployeeMenu();
                        } else if (currentPartner.getPartnerType() == PartnerType.PARTNERRESERVATIONMANAGER) {
                            runPartnerManagerMenu();
                        }
                    } catch (LoginCredentialsInvalidException ex) {
                        System.out.println("Invalid login credentials!");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }

            if (response == 2) {
                System.out.println("Exiting application!");
                break;
            }
        }
    }

    public static void doLogin() throws LoginCredentialsInvalidException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("\nYou are now logging in!");
        System.out.println("-----------------------\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            //call web service

        } else {
            throw new LoginCredentialsInvalidException("Invalid login credential!");
        }
    }

    private static void runPartnerEmployeeMenu() {
        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\nYou are logged in as a Partner Employee!");
            System.out.println("----------------------------------------\n");
            System.out.println("1: Search Room");
            System.out.println("2: Exit\n");
            while (true) {
                System.out.print("Enter an option> ");

                try {
                    response = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    response = 404;
                }

                if (response == 1) {
                    searchRoom();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }

            if (response == 2) {
                System.out.println("Logging out!");
                break;
            }
        }
    }

    private static void runPartnerManagerMenu() {
        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\nYou are logged in as a Partner Manager!");
            System.out.println("---------------------------------------\n");
            System.out.println("1: Search Room");
            System.out.println("2: Reserve Room");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All Reservations");
            System.out.println("5: Exit\n");
            while (true) {
                System.out.print("Enter an option> ");

                try {
                    response = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException ex) {
                    response = 404;
                }

                if (response == 1) {
                    searchRoom();
                } else if (response == 2) {
                    reserveRoom();
                } else if (response == 3) {
                    viewReservationDetails();
                } else if (response == 4) {
                    viewAllPartnerReservation();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
            if (response == 2) {
                System.out.println("Logging out!");
                break;
            }
        }
    }

    private static Booking searchRoom() {
        try {
            //instantiating web service _service class and assigning port
            WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
            WebServiceSessionBean port = service.getWebServiceSessionBeanPort();

            Scanner sc = new Scanner(System.in);
            System.out.println("\nYou are a partner search a room");
            System.out.println("-------------------------------\n");
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Date startDateString;
            Date endDateString;
            String roomTypeName;
            RoomType roomType;
            int numOfRooms = 0;
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            startDateString = inputDateFormat.parse(sc.nextLine().trim());
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            endDateString = outputDateFormat.parse(sc.nextLine().trim());

            if (startDateString.compareTo(endDateString) > 0) {
                System.out.println("Invalid Operation - start date exceed end date");
                System.out.println("Cancelling Operation...");
                return null;
            }

            List<RoomType> fakeRoomTypes = port.walkInSearchRoom(startDateString, endDateString); //use web service bean

            for (RoomType rt : fakeRoomTypes) {
                System.out.println("List of available Room Types and quantities:");
                System.out.println("Room Type Name: " + rt.getRoomName() + " Quantity Left: " + rt.getRoomInventory());
            }

            System.out.println("\nInput a Room Type Name> ");
            roomTypeName = sc.nextLine().trim();
            RoomType realRoomType = port.getRoomTypeByName(roomTypeName);  //use web service bean

            System.out.print("Input the number of rooms you want (that are of this Room Type)> ");

            while (numOfRooms != 404 || numOfRooms > realRoomType.getRoomInventory()) {
                try {
                    numOfRooms = Integer.parseInt(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    numOfRooms = 404;
                    System.out.println("Enter a valid number!");
                }
            }

            Booking booking = new Booking(numOfRooms, startDateString, endDateString);
            booking.setRoomType(realRoomType);

            Double price = port.getRateForOnlineBooking(booking.getBookingId());  //use web service bean

            System.out.println("\n Price for a booking like this would be: " + price + "\n");
            return booking;

        } catch (RoomNotFoundException ex) {
            System.out.println("No rooms are available!");
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type not found!");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("No published rate found!");
        }
        return null;
    }

    private static void reserveRoom() {

        try {
            //instantiating web service _service class and assigning port
            WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
            WebServiceSessionBean port = service.getWebServiceSessionBeanPort();

            Scanner sc = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            System.out.println("\nYou are now reserving a Hotel Room");
            System.out.println("------------------------------------\n");
            Booking availableBooking = searchRoom();
            RoomType roomType = availableBooking.getRoomType();
            Date checkIn = availableBooking.getCheckInDate();
            Date checkOut = availableBooking.getCheckInDate();
            Integer numOfRoom = availableBooking.getNumberOfRooms();
            Booking booking = new Booking(numOfRoom, checkIn, checkOut);
            port.createNewBookingWithPartner(booking, roomType.getRoomTypeId(), currentPartner.getCustomerId());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room not found!");
        } catch (CustomerNotFoundException ex) {
            System.out.println("Customer not found!");
        } catch (EntityInstanceExistsInCollectionException ex) {
            System.out.println("Room not found!");
        }
    }

    private static void viewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        Long bookingId;
        System.out.println("\nViewing my reservation details!");
        System.out.println("-------------------------------\n");
        System.out.print("Enter a Booking ID>");

        while (true) {
            try {
                bookingId = Long.parseLong(sc.nextLine().trim());
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Please input a valid ID format");
            }
        }

        List<Booking> bookings = currentPartner.getBookings();
        for (Booking booking : bookings) {
            if (Objects.equals(booking.getBookingId(), bookingId)) {
                System.out.println("Booking Id: " + booking.getBookingId());
                System.out.println("Check In Date: " + booking.getCheckInDate());
                System.out.println("Check Out Date: " + booking.getCheckOutDate());
                System.out.println("Room Type: " + booking.getRoomType());
                System.out.println("Number of rooms: " + booking.getNumberOfRooms());
            }
        }
    }

    private static void viewAllPartnerReservation() {
        //instantiating web service _service class and assigning port
        WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
        WebServiceSessionBean port = service.getWebServiceSessionBeanPort();

        System.out.println("\nViewing all my reservations!");
        System.out.println("----------------------------\n");
        try {
            List<Booking> bookings = port.getAllBookingsByPartnerId(currentPartner.getPartnerId());
            for (Booking b : bookings) {
                System.out.println("Booking ID: " + b.getBookingId());
                System.out.println("Start Date: " + b.getCheckInDate());
                System.out.println("End Date: " + b.getCheckOutDate());
                System.out.println("Room Type: " + b.getRoomType());
                System.out.println("Number of rooms: " + b.getNumberOfRooms());
                System.out.println("");
            }
        } catch (BookingNotFoundException ex) {
            System.out.println("Booking not found!");
        }
    }
}
