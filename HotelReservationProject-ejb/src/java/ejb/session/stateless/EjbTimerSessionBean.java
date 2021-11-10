/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.exceptions.BookingNotFoundException;

/**
 *
 * @author Jorda
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private BookingSessionBeanLocal bookingSessionBean;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateRooms")
    public void allocateRooms(){
        try {
            List<Booking> bookings = bookingSessionBean.retrieveUnallocatedBookings();
            for(Booking booking : bookings){
                RoomType roomType = booking.getRoomType();
                Integer numOfRooms = booking.getNumberOfRooms();
                Integer count = 0;
                while(count < numOfRooms){
                    //List<Room> availableRooms = 
                    
                    
                    count++;
                }
            }
        } catch (BookingNotFoundException ex) {
            System.out.println("No bookings to allocate!");
        }
        
    }
    
}
