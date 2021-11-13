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
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.EmployeeRole;
import util.enumeration.PartnerType;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.NonUniqueCredentialsException;

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
        boolean logout = false;
        while (!logout) {
            System.out.println("\n-You are currently logged in as a System Administrator-");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("1. Create new Employee");
            System.out.println("2. View all Employees");
            System.out.println("3. Create new Partner");
            System.out.println("4. View all Partners");
            System.out.println("5. Logout");

            System.out.print("Please select an option>");

            try { //fixes nextLine() eating \n problem + when user enters not an Int, catch error
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                choice = 404; //this number just has to be not a valid option
            }

            switch (choice) {
                case 1:
                    this.createNewEmployee();
                    break;
                case 2:
                    this.viewAllEmployees();
                    break;
                case 3:
                    this.createNewPartner();
                    break;
                case 4:
                    this.viewAllPartners();
                    break;
                case 5:
                    logout = true;
                    break;
                default:
                    System.out.println("Please input a valid choice!");
            }
        } 
    }

    public void createNewEmployee() {
        String name;
        String username;
        String password;
        EmployeeRole role = null;
        boolean roleSettled = false;

        System.out.println("\n-Creating a new employee-");
        System.out.println("-------------------------\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a full name>");
        name = sc.nextLine();
        System.out.print("Enter a username>");
        username = sc.next().trim();
        System.out.print("Enter a password>");
        password = sc.next().trim();

        while (!roleSettled) {
            int roleChoice;

            for (int i = 0; i < EmployeeRole.values().length; i++) {
                System.out.println((i + 1) + ". " + EmployeeRole.values()[i]);
            }

            System.out.print("\nEnter a Role (integer), these are your choices>");
            try {
                roleChoice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                roleChoice = 404;
            }

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
        Employee newEmployee = new Employee(name, username, password, role);
        employeeSessionBeanRemote.createNewEmployee(newEmployee);
        System.out.println("Employee successfully created!");
    }

    public void viewAllEmployees() {
        try {
            List<Employee> listOfEmployees = employeeSessionBeanRemote.retrieveAllEmployees();
            System.out.println("\n-Viewing a list of Employees-");
            System.out.println("-----------------------------\n");
            for (Employee e : listOfEmployees) {
                System.out.println("Name: " + e.getName() + " | Username: " + e.getUsername() + " | Role: " + e.geteRole());
            }
        } catch (EmployeeNotFoundException ex) {
            System.out.println("No employees exist in the database!");
        }
    }

    public void createNewPartner() {
        String name;
        String username;
        String password;
        PartnerType type = null;
        boolean roleSettled = false;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n-Creating a new Partner-");
        System.out.println("------------------------\n");
        System.out.print("Enter a full name>");
        name = sc.nextLine();
        System.out.print("Enter a username>");
        username = sc.next().trim();
        System.out.print("Enter a password>");
        password = sc.next().trim();

        while (!roleSettled) {
            int roleChoice;

            for (int i = 0; i < PartnerType.values().length; i++) {
                System.out.println((i + 1) + ". " + PartnerType.values()[i]);
            }

            System.out.print("Enter a partner type (integer), these are your choices>");
            try {
                roleChoice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                roleChoice = 404;
            }

            switch (roleChoice) {
                case 1:
                    type = PartnerType.PARTNEREMPLOYEE;
                    roleSettled = true;
                    break;
                case 2:
                    type = PartnerType.PARTNERRESERVATIONMANAGER;
                    roleSettled = true;
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        }

        Partner partner = new Partner(name, username, password, type);
        try {
            partnerSessionBeanRemote.createNewPartner(partner);
        } catch (NonUniqueCredentialsException ex) {
            System.out.println("NonUnique Credentials!");
        }
    }

    public void viewAllPartners() {
        try {
            List<Partner> listOfPartners = partnerSessionBeanRemote.retrieveAllPartners();
            System.out.println("\n-Viewing a list of Partners-");
            System.out.println("----------------------------\n");
            for (Partner p : listOfPartners) {
                System.out.println("Name: " + p.getName() + " | Username: " + p.getUserName() + " | PartnerType: " + p.getPartnerType());
            }
        } catch (NoPartnersFoundException ex) {
            System.out.println("No partners exist in the Database!");
        }
    }
}