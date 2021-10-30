/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.partnerNotFoundException;

/**
 *
 * @author ryo20
 */
public class HotelOperationModule {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Employee currentEmployee;

    public HotelOperationModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote,
            PartnerSessionBeanRemote partnerSessionBeanRemote, Employee employee) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.currentEmployee = employee;
    }

    public void runMainMenu() {
        if (currentEmployee.getErole() == EmployeeRole.OPERATIONMANAGER) {
            this.runOperationsMenu();
        } else if (currentEmployee.getErole() == EmployeeRole.SALESMANAGER) {
            this.runSalesMenu();
        }
    }

    private void runOperationsMenu() {
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        int submoduleOption;
        while (!logout) {
            System.out.println("You are currently logged in as an Operation Manager");
            System.out.println("1. Room Types");
            System.out.println("2. Rooms");
            System.out.println("3. Logout");
            System.out.print("Please select a sub-module to work with>");
            submoduleOption = sc.nextInt();
            int option;

            if (submoduleOption == 1) {
                boolean returnBack = false;
                while (!returnBack) {
                    System.out.println("You are now in the Room Type sub-module");
                    System.out.println("1. Create new Room Type");
                    System.out.println("2. Update a Room Type");
                    System.out.println("3. Delete a Room Type");
                    System.out.println("4. View All Room Types");
                    System.out.println("5. View Room Type details");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");
                    option = sc.nextInt();

                    switch (option) {
                        case 1:
                            this.createNewRoomType();
                            break;
                        case 2:
                            this.updateARoomType();
                            break;
                        case 3:
                            this.deleteARoomType();
                            break;
                        case 4:
                            this.viewAllRoomTypes();
                            break;
                        case 5:
                            this.viewRoomTypeDetails();
                            break;
                        case 6:
                            returnBack = true;
                            break;
                        default:
                            System.out.println("Please input a valid choice");
                    }
                }
            } else if (submoduleOption == 2) {
                boolean returnBack = false;
                while (!returnBack) {
                    System.out.println("You are now in the Room sub-module");
                    System.out.println("1. Create new Room");
                    System.out.println("2. Update a Room");
                    System.out.println("3. Delete a Room");
                    System.out.println("4. View All Rooms");
                    System.out.println("5. View Room Allocation Exception Report");
                    System.out.println("6. Back to sub-module page");
                    System.out.print("Please select an option>");
                    option = sc.nextInt();

                    switch (option) {
                        case 1:
                            this.createNewRoom();
                            break;
                        case 2:
                            this.updateARoom();
                            break;
                        case 3:
                            this.deleteARoom();
                            break;
                        case 4:
                            this.viewAllRooms();
                            break;
                        case 5:
                            this.viewRoomAllocationExceptionReport();
                            break;
                        case 6:
                            returnBack = true;
                            break;
                        default:
                            System.out.println("Please input a valid choice");
                    }
                }
            } else if (submoduleOption == 3) {
                logout = true;
                break;
            }
        }
    }

    private void runSalesMenu() {
        Scanner sc = new Scanner(System.in);
        boolean logout = false;
        int option;
        while (!logout) {
            System.out.println("You are currently logged in as an Sales Manager");
            System.out.println("1. Create new Room Rate");
            System.out.println("2. Update a Room Rate");
            System.out.println("3. Delete a Room Rates");
            System.out.println("4. View all Room Rates");
            System.out.println("5. View Room Rates details");
            System.out.print("Please select an option>");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    this.createNewRoomRate();
                    break;
                case 2:
                    this.updateARoomRate();
                    break;
                case 3:
                    this.deleteARoomRate();
                    break;
                case 4:
                    this.viewAllRoomRates();
                    break;
                case 5:
                    this.viewRoomRateDetails();
                    break;
                case 6:
                    logout = true;
                    break;
                default:
                    System.out.println("Please input a valid choice");
            }
        }
    }
}
