/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.BookingNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Remote
public interface BookingSessionBeanRemote {

    public long createNewBooking(Booking booking, Long roomTypeId) throws RoomTypeNotFoundException;

    public List<Booking> retrieveBookings() throws BookingNotFoundException;

    public Booking retrieveBookingByBookingId(Long bookingId) throws BookingNotFoundException;

    public List<Booking> getAllBookingsByPartnerId(Long partnerId) throws BookingNotFoundException;

    public List<Booking> getAllBookingsByCustomerId(Long customerId) throws BookingNotFoundException;
    
    public Double getPublishRatePriceOfBooking(Long bookingId) throws RoomRateNotFoundException;
}
