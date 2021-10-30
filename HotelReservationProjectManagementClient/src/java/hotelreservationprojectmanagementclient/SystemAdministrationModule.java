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
import entity.Partner;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.EmployeeType;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.partnerNotFoundException;

/**
 *
 * @author ryo20
 */
public class SystemAdministrationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Employee employee;

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote,
            PartnerSessionBeanRemote partnerSessionBeanRemote, Employee employee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employee = employee;
    }

    public void runMainMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("You are currently logged in as a System Administrator");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Create new Employee");
            System.out.println("2. View all Employees");
            System.out.println("3. Create new Partner");
            System.out.println("4. View all Partners");
            System.out.println("5. Logout");

            System.out.print("Please select an option>");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    this.createNewEmployee();
                    break;
                case 2:
                    try {
                        this.viewAllEmployees();
                    } catch (EmployeeNotFoundException ex) {
                        System.out.println("No Employees exist in the database!");
                    }
                    break;
                case 3:
                    this.createNewPartner();
                    break;
                case 4:
                    try {
                        this.viewAllPartners();
                    } catch (partnerNotFoundException ex) {
                        System.out.println("No Partners exists in the database!");
                    }
                    break;
                default:
                    System.out.println("Please input a valid choice");
            }
        }
    }

    public void createNewEmployee() {
        String name;
        String username;
        String password;
        EmployeeRole role = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a full name");
        name = sc.nextLine();
        System.out.print("Enter a username>");
        username = sc.next();
        System.out.print("Enter a password>");
        password = sc.next();
        boolean roleSettled = false;

        while (roleSettled == false) {
            System.out.println("Enter a Role, these are your choices");
            for (int i = 0; i < EmployeeRole.values().length; i++) {
                System.out.println((i + 1) + ". " + EmployeeRole.values()[i]);
            }
            int roleChoice = sc.nextInt();

            switch (roleChoice) {
                case 1:
                    role = EmployeeRole.SYSTEMADMINISTRATOR;
                    roleSettled = true;
                    break;
                case 2:
                    role = EmployeeRole.OPERATIONMANAGER;
                    roleSettled = true;
                    break;
                case 3:
                    role = EmployeeRole.SALESMANAGER;
                    roleSettled = true;
                    break;
                case 4:
                    role = EmployeeRole.GUESTRELATIONSOFFICER;
                    roleSettled = true;
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        }
        //At this point we have all the data we need to create a new employee
        Employee newEmployee = new Employee(name, username, password, role);
        employeeSessionBeanRemote.createNewEmployee(newEmployee);
    }
    

    public void viewAllEmployees() throws EmployeeNotFoundException {
        List<Employee> listOfEmployees = employeeSessionBeanRemote.retrieveAllEmployees();
        System.out.println("Viewing a list of Employees");
        for (Employee e : listOfEmployees) {
            System.out.println("Name: " + e.getName() + " | Username: " + e.getUsername() + " | Role: " + e.getErole());
        }
    }
    

    public void createNewPartner() {
        String name;
        String username;
        String password;
        EmployeeType type = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a full name");
        name = sc.nextLine();
        System.out.print("Enter a username>");
        username = sc.next();
        System.out.print("Enter a password>");
        password = sc.next();
        boolean roleSettled = false;

        while (roleSettled == false) {
            System.out.println("Enter a partner type, these are your choices");
            for (int i = 0; i < EmployeeType.values().length; i++) {
                System.out.println((i + 1) + ". " + EmployeeType.values()[i]);
            }
            int roleChoice = sc.nextInt();

            switch (roleChoice) {
                case 1:
                    type = EmployeeType.PARTNEREMPLOYEE;
                    roleSettled = true;
                    break;
                case 2:
                    type = EmployeeType.PARTNERRESERVATIONMANAGER;
                    roleSettled = true;
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        }
        //At this point we have all the data we need to create a new partner
        Partner partner = new Partner(name, username, password, type);
        partnerSessionBeanRemote.createNewPartner(partner);
    }
    

    public void viewAllPartners() throws partnerNotFoundException {
        List<Partner> listOfPartners = partnerSessionBeanRemote.retrieveAllPartners();
        System.out.println("Viewing a list of Partners");
        for (Partner p : listOfPartners) {
            System.out.println("Name: " + p.getName() + " | Username: " + p.getUserName() + " | PartnerType: " + p.getEmployeeType());
        }
    }
}
