/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author ryo20
 */
public class HoRSClientModule {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;

    public HoRSClientModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
    }
    
    public void runEmployeeLoginPage() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Login with your username and password!");
        
        
        
        //if correct credentials are given, instantiate the required client class (check enum type), pass in the remote beans, and call the runMainMenu() method of each class.
        //dont forget to also pass in the EmployeeEntity
    }
}
