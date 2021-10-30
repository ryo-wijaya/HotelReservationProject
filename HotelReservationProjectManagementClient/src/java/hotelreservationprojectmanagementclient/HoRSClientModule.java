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
    
    private FrontOfficeModule frontOfficeModule;
    private SystemAdministrationModule systemAdministrationModule;
    private HotelOperationModule hotelOperationModule;
    
    private Employee currentEmployee;

    public HoRSClientModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
    }
    
    public void runEmployeeLoginPage(){
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
            
            while(true){
                System.out.println("*** Welcome to HoRS management client System ***\n");
                System.out.println("1: Login");
                System.out.println("2: Exit\n");
                response = 0;               
                while (response < 1 || response > 2){
                    System.out.print("> ");      
                    response = sc.nextInt();        
                    if(response == 1){
                        try {
                            doLogin();
                            System.out.print("Login successful!\n");
                            if(currentEmployee.getErole() == SYSTEMADMINISTRATOR){
                                systemAdministrationModule = new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, currentEmployee);
                                systemAdministrationModule.runMainMenu();
                            } 
                            else if(currentEmployee.getErole() == OPERATIONMANAGER || currentEmployee.getErole() == SALESMANAGER){
                                hotelOperationModule = new HotelOperationModule(customerSessionBeanRemote, employeeSessionBeanRemote, partnerSessionBeanRemote, currentEmployee);
                                hotelOperationModule.runMainMenu();
                            }
                            else if(currentEmployee.getErole() == GUESTRELATIONSOFFICER){
                                frontOfficeModule = new FrontOfficeModule(employeeSessionBeanRemote, currentEmployee);
                                frontOfficeModule.runMainMenu();
                            }
                        }
                        catch (LoginCredentialsInvalidException ex) {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }                  
                    }
                    else if(response == 2){
                        break;
                    }
                    else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                if (response == 2) {
                    break;
                }
            }
        //if correct credentials are given, instantiate the required client class (check enum type), pass in the remote beans, and call the runMainMenu() method of each class.
        //dont forget to also pass in the EmployeeEntity
    }
    
    public void doLogin() throws LoginCredentialsInvalidException{
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("*** HoRS management client System :: Login ***\n");
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