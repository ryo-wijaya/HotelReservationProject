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
import java.util.List;
import java.util.Scanner;
import util.exceptions.BookingNotFoundException;
import util.exceptions.LoginCredentialsInvalidException;

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
        String email = "";
        String password = "";

        System.out.println("*** Welcome to HoRS Reservation Client :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.customerLogin(email, password);
        } else {
            throw new LoginCredentialsInvalidException("Missing login credential!");
        }
    }
    
    public void registerGuest(){
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
        
        Long newCustomerId = customerSessionBeanRemote.registerAsCustomer(customer);
        System.out.println("Customer created successfully!: " + newCustomerId + "\n");
    }

    public void searchHotelRoom() {

    }

    public void reserveHotelRoom() {

    }

    public void viewMyReservationDetails() {
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
            
            //NOT DONE YET
        }

    }

    public void viewAllMyReservations() {
        System.out.println("\nViewing all my reservations!");
        System.out.println("----------------------------\n");
        try {
            List<Booking> bookings = bookingSessionBeanRemote.getAllBookingsByPartnerId(currentCustomer.getCustomerId());
            for (Booking b : bookings) {
                System.out.println("Booking ID: " + b.getBookingId() + "Start Date: " + b.getCheckInDate() + "End Date: " + b.getCheckOutDate());
            }
        } catch (BookingNotFoundException ex) {
            System.out.println("Booking not found!");
        }
    }
}
