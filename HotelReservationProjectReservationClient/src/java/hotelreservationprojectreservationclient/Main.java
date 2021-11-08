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
import javax.ejb.EJB;
import util.exceptions.EmployeeNotFoundException;

/**
 *
 * @author ryo20
 */
public class Main {

    @EJB
    private static HotelReservationBeanRemote hotelReservationBean;

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;

    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;

    @EJB
    private static BookingSessionBeanRemote bookingSessionBeanRemote;

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, bookingSessionBeanRemote, roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, customerSessionBeanRemote, hotelReservationBean);
        mainApp.runApp();
    }
}
