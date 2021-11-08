/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectreservationclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import javax.ejb.EJB;
import util.exceptions.EmployeeNotFoundException;

/**
 *
 * @author ryo20
 */
public class Main {

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    //@EJB
    //private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    
    
    
    public static void main(String[] args) {
        try {
            System.out.println("starting!");
            // System.out.println("id: " + employeeSessionBeanRemote.getEmployeeById(1l));
            System.out.println(roomRateSessionBeanRemote.getRoomRateById(1l));
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
}
