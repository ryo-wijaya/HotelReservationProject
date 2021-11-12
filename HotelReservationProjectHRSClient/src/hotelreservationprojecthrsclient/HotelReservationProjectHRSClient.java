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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.sound.sampled.Port;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.Booking;
import ws.client.BookingExceptionType;
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
import ws.client.Room;
import ws.client.RoomNotFoundException;
import ws.client.RoomNotFoundException_Exception;
import ws.client.RoomRateNotFoundException_Exception;
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

    //private static Partner currentPartner;

    public static void main(String[] args) {
        //runMainMenu();
    }

    
    public static void runMainMenu() {

        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            while (true) {
                System.out.println("\nWelcome to the Holiday Reservation System for Partners!");
                System.out.println("-------------------------------------------------------\n");
                System.out.println("1: Login");
                System.out.println("2: Exit\n");
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
                currentPartner = port.doLogin(username, password);
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
            while (true) {
                System.out.println("\nYou are logged in as a Partner Manager!");
                System.out.println("---------------------------------------\n");
                System.out.println("1: Search Room");
                System.out.println("2: Reserve Room");
                System.out.println("3: View Reservation Details");
                System.out.println("4: View All Reservations");
                System.out.println("5: Exit\n");
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
            if (response == 5) {
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
            System.out.print("Enter Check In Date (dd/mm/yyyy)> ");
            startDateString = inputDateFormat.parse(sc.nextLine().trim());
            gc.setTime(startDateString);
            start = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            System.out.print("Enter Check Out Date (dd/mm/yyyy)> ");
            endDateString = inputDateFormat.parse(sc.nextLine().trim());
            gc.setTime(endDateString);
            end = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            if (startDateString.compareTo(endDateString) > 0) {
                System.out.println("Invalid Operation - start date exceed end date");
                System.out.println("Cancelling Operation...");
                return null;
            }

            while (numOfRooms != 404) {
                System.out.print("Input the number of rooms you want (that are of this Room Type)> ");
                try {
                    numOfRooms = Integer.parseInt(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    numOfRooms = 404;
                    System.out.println("Enter a valid number!");
                }
            }
            
           // This map contains key value pairs of RoomTypeIds to QuantityOfRoomsAvailable
            HashMap<Long, Integer> map = port.walkInSearchRoom(start, end);

            //Iterating over each Room Type and Inventory mapping
            //Iterating over each Room Type and Inventory mapping
            for (Map.Entry<Long, Integer> pair : map.entrySet()) {
                roomType = port.getRoomTypeById(pair.getKey());
                if (pair.getValue() >= numOfRooms) {
                    System.out.println("Room Type: " + roomType.getRoomName() + " | " + "Number Of Rooms Left: " + pair.getValue() + " | price for the number of days: " 
                        + port.getRateForOnlineBooking(roomType.getRoomTypeId(), startDateString, endDateString, numOfRooms));
                }
            }

            Booking booking = new Booking();
            booking.setBookingExceptionType(ws.client.BookingExceptionType.NONE);
            booking.setPreBooking(Boolean.TRUE);
            booking.setCheckInDate(null);
            booking.setCheckOutDate(null);
            booking.setNumberOfRooms(numOfRooms);
            booking.setCheckInDate(start);
            booking.setCheckOutDate(end);

            return booking;

        } catch (DatatypeConfigurationException ex) {
            System.out.println("Date Error!");
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomTypeNotFoundException_Exception ex) {
            System.out.println("No published rate found!");
        } catch (RoomRateNotFoundException_Exception ex) {
            System.out.println("Room Rate Not Found");
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
            System.out.print("Please enter a room type name> ");
            RoomType roomType = port.getRoomTypeByName(sc.nextLine().trim());
            checkIn = availableBooking.getCheckInDate();
            checkOut = availableBooking.getCheckOutDate();
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
            System.out.print("What is todays date? (dd/mm/yyyy)> ");
            Date cDate = inputDateFormat.parse(sc.nextLine().trim());
            Double rtime = 0.0;
            while (rtime != 404.0) {
                System.out.print("What time is the reservation made> ");
                try {
                    rtime = Double.parseDouble(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    rtime = 404.0;
                    System.out.println("Enter a valid number!");
                }
            }
            if(booking.getCheckInDate().equals(cDate) && rtime >= 2){
                port.findARoomAndAddToIt(booking.getBookingId());
            }
            System.out.println("Hotel room(s) successfully reserved!");
        } catch (BookingNotFoundException_Exception | EntityInstanceExistsInCollectionException_Exception | NoPartnersFoundException_Exception | RoomTypeNotFoundException_Exception | ParseException | RoomNotFoundException_Exception ex) {
            System.out.println("ERROR!");
        }
    }

    private static void viewReservationDetails() {
        try {
            //maybe use the bean to get bookings for partner so its updated
            WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
            WebServiceSessionBean port = service.getWebServiceSessionBeanPort();
            
            Scanner sc = new Scanner(System.in);
            Long bookingId;
            System.out.println("\nViewing my reservation details!");
            System.out.println("-------------------------------\n");
            List<Booking> bookings = port.retrievePartnerByPartnerId(currentPartner.getPartnerId()).getBookings();
            
            System.out.print("Enter a Booking ID>");
            if (bookings.isEmpty()) {
                System.out.print("No existing reservations!\n");
                return;
            }
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
        } catch (NoPartnersFoundException_Exception ex) {
            System.out.println("partner not found!/n");
        }
    }

    private static void viewAllPartnerReservation() {
        try {
            //instantiating web service _service class and assigning port
            WebServiceSessionBean_Service service = new WebServiceSessionBean_Service();
            WebServiceSessionBean port = service.getWebServiceSessionBeanPort();
            
            System.out.println("\nViewing all my reservations!");
            System.out.println("----------------------------\n");
            List<Booking> bookings = port.retrievePartnerByPartnerId(currentPartner.getPartnerId()).getBookings();
            if(bookings.isEmpty()) {
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
        } catch (NoPartnersFoundException_Exception ex) {
            System.out.println("partner not found!/n");
        }

    }

}
