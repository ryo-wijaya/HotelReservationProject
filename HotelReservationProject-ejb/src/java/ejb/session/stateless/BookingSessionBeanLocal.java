/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.BookingNotFoundException;

/**
 *
 * @author Jorda
 */
@Local
public interface BookingSessionBeanLocal {

    public Booking createNewBooking(Booking booking);

    public List<Booking> retrieveAllProducts();

    public Booking retrieveBookingByBookingId(Long bookingId) throws BookingNotFoundException;
    
}
