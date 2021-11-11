/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojecthrsclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.enumeration.BookingExceptionType;
import ws.client.Booking;
import ws.client.BookingNotFoundException;
import ws.client.BookingNotFoundException_Exception;
import ws.client.EntityInstanceExistsInCollectionException;
import ws.client.EntityInstanceExistsInCollectionException_Exception;
import ws.client.EntityInstanceMissingInCollectionException_Exception;
import ws.client.LoginCredentialsInvalidException;
import ws.client.LoginCredentialsInvalidException_Exception;
import ws.client.NoPartnersFoundException_Exception;
import ws.client.Partner;
import ws.client.PartnerType;
import ws.client.RoomNotFoundException;
import ws.client.RoomNotFoundException_Exception;
import ws.client.RoomType;
import ws.client.RoomTypeNotFoundException;
import ws.client.RoomTypeNotFoundException_Exception;
import ws.client.WebServiceSessionBean;
import ws.client.WebServiceSessionBean_Service;

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
                    doLogin();
                    if (currentPartner.getPartnerType() == PartnerType.PARTNEREMPLOYEE) {
                        runPartnerEmployeeMenu();
                    } else if (currentPartner.getPartnerType() == PartnerType.PARTNERRESERVATIONMANAGER) {
                        runPartnerManagerMenu();
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

    public static void doLogin() {
        WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
        WebServiceSessionBean port = service.getWebServiceSessionBeanPort();
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("\nYou are now logging in!");
        System.out.println("-----------------------\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        try {
            if (username.length() > 0 && password.length() > 0) {
                //call web service
                long currentPartnerId = port.doLogin(username, password).getPartnerId();
            }
        } catch (LoginCredentialsInvalidException_Exception ex) {
            System.out.println("Invalid login credentials");
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
            GregorianCalendar gc = new GregorianCalendar();
            Date startDateString;
            Date endDateString;
            XMLGregorianCalendar start = null;
            XMLGregorianCalendar end = null;
            String roomTypeName;
            RoomType roomType;
            int numOfRooms = 0;
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            startDateString = inputDateFormat.parse(sc.nextLine().trim());
            gc.setTime(startDateString);
            start = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            endDateString = outputDateFormat.parse(sc.nextLine().trim());
            gc.setTime(endDateString);
            end = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            if (startDateString.compareTo(endDateString) > 0) {
                System.out.println("Invalid Operation - start date exceed end date");
                System.out.println("Cancelling Operation...");
                return null;
            }

            List<RoomType> fakeRoomTypes = port.walkInSearchRoom(start, end);

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

            Booking booking = new Booking();
            booking.setNumberOfRooms(numOfRooms);
            booking.setCheckInDate(start);
            booking.setCheckOutDate(end);
            booking.setRoomType(realRoomType);

            Double price = port.getRateForOnlineBooking(booking.getBookingId());  //use web service bean

            System.out.println("\n Price for a booking like this would be: " + price + "\n");
            return booking;

        } catch (DatatypeConfigurationException ex) {
            System.out.println("No rooms are available!");
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomNotFoundException_Exception ex) {
            System.out.println("Room Type not found!");
        } catch (RoomTypeNotFoundException_Exception ex) {
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
            GregorianCalendar gc = new GregorianCalendar();
            XMLGregorianCalendar checkIn;
            XMLGregorianCalendar checkOut;
            System.out.println("\nYou are now reserving a Hotel Room");
            System.out.println("------------------------------------\n");
            Booking availableBooking = searchRoom();
            RoomType roomType = availableBooking.getRoomType();
            checkIn = availableBooking.getCheckInDate();
            checkOut = availableBooking.getCheckInDate();
            Date checkInDate = checkIn.toGregorianCalendar().getTime();
            Date checkOutDate = checkOut.toGregorianCalendar().getTime();
            Integer numOfRoom = availableBooking.getNumberOfRooms();
            Booking booking = new Booking();
            booking.setCheckInDate(checkIn);
            booking.setCheckOutDate(checkOut);
            booking.setNumberOfRooms(numOfRoom);
            booking.setBookingExceptionType(ws.client.BookingExceptionType.NONE);
            booking.setPreBooking(Boolean.TRUE);
            port.createNewBookingWithPartner(booking, roomType.getRoomTypeId(), currentPartner.getPartnerId());
        } catch (BookingNotFoundException_Exception | EntityInstanceExistsInCollectionException_Exception | NoPartnersFoundException_Exception | RoomTypeNotFoundException_Exception ex) {
            Logger.getLogger(HotelReservationProjectHRSClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void viewReservationDetails() {
        //maybe use the bean to get bookings for partner so its updated

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
        } catch (BookingNotFoundException_Exception | EntityInstanceMissingInCollectionException_Exception ex) {
            Logger.getLogger(HotelReservationProjectHRSClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
