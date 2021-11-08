/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateful.HotelReservationBeanRemote;
import ejb.session.stateless.BookingSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import static util.enumeration.EmployeeRole.GUESTRELATIONSOFFICER;
import static util.enumeration.EmployeeRole.OPERATIONMANAGER;
import static util.enumeration.EmployeeRole.SALESMANAGER;
import static util.enumeration.EmployeeRole.SYSTEMADMINISTRATOR;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author ryo20
 */
public class HoRSClientModule {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    private FrontOfficeModule frontOfficeModule;
    private SystemAdministrationModule systemAdministrationModule;
    private HotelOperationModule hotelOperationModule;
    
    private Employee currentEmployee;

    public HoRSClientModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, BookingSessionBeanRemote bookingSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, RoomSessionBeanRemote roomSessionBean, RoomRateSessionBeanRemote roomRateSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.bookingSessionBean = bookingSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomSessionBean = roomSessionBean;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }
   
    public void runEmployeeLoginPage(){
        Scanner sc = new Scanner(System.in);
        int response;
            
            while(true){
                System.out.println("-Welcome to HoRS management client System-\n");
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
                            System.out.print("Login successful!");
                            if(currentEmployee.geteRole() == SYSTEMADMINISTRATOR){
                                systemAdministrationModule = new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, currentEmployee);
                                systemAdministrationModule.runMainMenu();
                            } 
                            else if(currentEmployee.geteRole() == OPERATIONMANAGER || currentEmployee.geteRole() == SALESMANAGER){
                                hotelOperationModule = new HotelOperationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, bookingSessionBean, roomTypeSessionBean, roomSessionBean, roomRateSessionBeanRemote, currentEmployee);
                                hotelOperationModule.runMainMenu();
                            }
                            else if(currentEmployee.geteRole() == GUESTRELATIONSOFFICER){
                                frontOfficeModule = new FrontOfficeModule(employeeSessionBeanRemote, currentEmployee);
                                frontOfficeModule.runMainMenu();
                            }
                        }
                        catch (LoginCredentialsInvalidException ex) {
                            System.out.println("Invalid login credentials!");
                        }                  
                    }
                    else if (response == 2){
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!");
                    }
                }
                
                if (response == 2) {
                    System.out.println("Exiting application!");
                    break;
                }
            }
    }
    
    public void doLogin() throws LoginCredentialsInvalidException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("-HoRS management client System Login-");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.login(username, password);
        } 
        else {
            throw new LoginCredentialsInvalidException("Invalid login credential!");
        }
    }
}
