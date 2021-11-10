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
import util.exceptions.CustomerNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.TypeOneNotFoundException;

/**
 *
 * @author Jorda
 */
@Local
public interface BookingSessionBeanLocal {

    public long createNewBooking(Booking booking, Long roomTypeId) throws RoomTypeNotFoundException;

    public List<Booking> retrieveBookings() throws BookingNotFoundException;

    public Booking retrieveBookingByBookingId(Long bookingId) throws BookingNotFoundException;

    public List<Booking> getAllBookingsByPartnerId(Long partnerId) throws BookingNotFoundException;

    public List<Booking> getAllBookingsByCustomerId(Long customerId) throws BookingNotFoundException;

    public Double getPublishRatePriceOfBooking(Long bookingId) throws RoomRateNotFoundException;

    public List<Booking> retrieveUnallocatedBookings() throws BookingNotFoundException;

    public List<Booking> retrieveTypeTwoBookings() throws BookingNotFoundException;

    public List<Booking> retrieveTypeOneBookings() throws TypeOneNotFoundException;

    public long createNewBookingWithCustomer(Booking booking, Long roomTypeId, Long customerId) throws RoomTypeNotFoundException, CustomerNotFoundException, EntityInstanceExistsInCollectionException;

    public long createNewBookingWithPartner(Booking booking, Long roomTypeId, Long partnerId) throws RoomTypeNotFoundException, EntityInstanceExistsInCollectionException, NoPartnersFoundException;
    public Double getRateForOnlineBooking(Long bookingId);
    
}
