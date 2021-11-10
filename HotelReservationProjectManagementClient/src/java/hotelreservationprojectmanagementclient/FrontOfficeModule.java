/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateful.HotelReservationBeanRemote;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exceptions.RoomNotFoundException;
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
    private Employee employee;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote,
            RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote,
            HotelReservationBeanRemote hotelReservationBeanRemote, Employee employee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
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
            System.out.println("3. Create-in Guest");
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
            System.out.print("Enter Departure Date (dd/mm/yyyy)> ");
            startDateString = inputDateFormat.parse(sc.nextLine().trim());
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            endDateString = outputDateFormat.parse(sc.nextLine().trim());

            if (startDateString.compareTo(endDateString) > 0) {
                System.out.println("Invalid Operation - start date exceed end date");
                System.out.println("Cancelling Operation...");
                return null;
            }

            List<RoomType> roomTypes = roomSessionBeanRemote.walkInSearchRoom(startDateString, endDateString);
            
            for (RoomType rt : roomTypes) {
                System.out.println("List of available Room Types and quantities:");
                System.out.println("Room Type Name: " + rt.getRoomName() + " Quantity Left: " + rt.getRoomInventory());
            }
            
            System.out.println("\nInput a Room Type Name> ");
            roomTypeName = sc.nextLine().trim();
            roomType = roomTypeSessionBeanRemote.getRoomTypeByName(roomTypeName);
            
            System.out.print("Input the number of rooms you want (that are of this Room Type)> ");
            
            while (numOfRooms != 404 || numOfRooms > roomType.getRoomInventory()) {
                try {
                    numOfRooms = Integer.parseInt(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException ex) {
                    numOfRooms = 404;
                    System.out.println("Enter a valid number!");
                }
            }
            
            Booking booking = new Booking(numOfRooms, startDateString, endDateString);
            booking.setRoomType(roomType);
            
            return booking;

        } catch (RoomNotFoundException ex) {
            System.out.println("No rooms are available!");
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type not found!");
        }
        return null;
    }

    private void walkInReserveRoom(Scanner sc) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("\nYou are now reserving a Room for a walk-in customer");
        System.out.println("---------------------------------------------------\n");
        List<Booking> availableRooms = walkInSearchRoom(sc);
        Integer option = 0;
        for(Booking bookings : availableRooms) {
            System.out.println("\nOption " + (option + 1) + ".");
            System.out.println("Rate Type name: " + bookings.getRoomType().getRoomName());      
        }
        Integer response = 0;
        while (true) {
            System.out.print("Please Select an Option given above> ");
            response = sc.nextInt();
            if (response < 1 || response > availableRooms.size()) {
                System.out.print("Invalid input> ");
            } else {
                RoomType roomType = availableRooms.get(response - 1).getRoomType();
                Date startDate = availableRooms.get(response - 1).getCheckInDate();
                Date endDate = availableRooms.get(response - 1).getCheckInDate();
                bookingSessionBeanRemote.
            }
        }

    }

    private void checkInGuest(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkoutGuest(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
