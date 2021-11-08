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
import entity.Employee;
import entity.Room;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exceptions.RoomNotFoundException;

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

    private void walkInSearchRoom(Scanner sc) {
        System.out.println("\nYou are now searching a Room for a walk-in customer");
        System.out.println("---------------------------------------------------\n");
        String startDateString;
        String endDateString;
        int[] sDate;
        int[] eDate;

        System.out.print("Input new Start Date in dd/mm/yyyy (with the slashes)>");
        startDateString = sc.next();
        System.out.print("Input new End Date in dd/mm/yyyy (with the slashes)>");
        endDateString = sc.next();
        sDate = Arrays.stream(startDateString.split("/")).mapToInt(Integer::parseInt).toArray();
        eDate = Arrays.stream(endDateString.split("/")).mapToInt(Integer::parseInt).toArray();
        Date startDate = new Date(sDate[3], sDate[2], sDate[1]);
        Date endDate = new Date(eDate[3], eDate[2], eDate[1]);
        
        if (startDate.compareTo(endDate) > 0) {
            System.out.println("Invalid Operation - start date exceed end date");
            System.out.println("Cancelling Operation...");
            return;
        }
        
        try {
            List<Room> rooms = roomSessionBeanRemote.walkInSearchRoom(startDate, endDate);
            
            
        } catch (RoomNotFoundException ex) {
            System.out.println("No rooms are available!");
        }
    }

    private void walkInReserveRoom(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkInGuest(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkoutGuest(Scanner sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
