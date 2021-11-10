/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.exceptions.BookingNotFoundException;
import util.exceptions.RoomNotFoundException;

/**
 *
 * @author Jorda
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateRooms")
    public void allocateRooms() {
        try {
            List<Booking> bookings = bookingSessionBeanLocal.retrieveUnallocatedBookings();
            for (Booking b : bookings) {
                roomSessionBeanLocal.findARoomAndAddToIt(b.getBookingId());
            }
        } catch (BookingNotFoundException ex) {
            System.out.println("No bookings to allocate!");
        } catch (RoomNotFoundException ex) {
            System.out.println("No room's to allocate!");
        }
    }
}