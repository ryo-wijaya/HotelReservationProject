/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateful.HotelReservationBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author ryo20
 */
public class Main {


    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HoRSClientModule hoRSClient = new HoRSClientModule(customerSessionBeanRemote, employeeSessionBeanRemote, partnerSessionBeanRemote);
        hoRSClient.runEmployeeLoginPage();
    }
}
