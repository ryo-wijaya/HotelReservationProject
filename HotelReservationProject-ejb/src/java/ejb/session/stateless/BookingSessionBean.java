/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.BookingNotFoundException;

/**
 *
 * @author Jorda
 */
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal, BookingSessionBeanRemote {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    @Override
    public Booking createNewBooking(Booking booking, Long roomTypeId) {
        em.persist(booking);
        em.flush();
        return booking;
    }

    @Override
    public List<Booking> retrieveBookings() throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b FROM Booking b");
        List<Booking> bookings = query.getResultList();
        if (!bookings.isEmpty()) {
            for (Booking b : bookings) {
                b.getPartner();
                b.getRoomType();
                b.getCustomer();
                b.getRooms().size();
            }
            return bookings;
        } else {
            throw new BookingNotFoundException();
        }
    }

    @Override
    public Booking retrieveBookingByBookingId(Long bookingId) throws BookingNotFoundException {
        Booking booking = em.find(Booking.class, bookingId);
        if (booking != null) {
            booking.getPartner();
            booking.getRoomType();
            booking.getCustomer();
            booking.getRooms().size();
            return booking;
        } else {
            throw new BookingNotFoundException("Booking ID" + bookingId + " does not exist!");
        }
    }

    public List<Booking> getAllBookingsByPartnerId(Long partnerId) throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.partner.partnerId = :inPartnerId");
        query.setParameter("inPartnerId", partnerId);
        List<Booking> bookings = query.getResultList();

        if (!bookings.isEmpty()) {
            for (Booking b : bookings) {
                b.getPartner();
                b.getRoomType();
                b.getCustomer();
                b.getRooms().size();
            }
            return bookings;
        } else {
            throw new BookingNotFoundException();
        }
    }

    public List<Booking> getAllBookingsByCustomerId(Long customerId) throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.customer.CustomerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        List<Booking> bookings = query.getResultList();

        if (!bookings.isEmpty()) {
            for (Booking b : bookings) {
                b.getPartner();
                b.getRoomType();
                b.getCustomer();
                b.getRooms().size();
            }
            return bookings;
        } else {
            throw new BookingNotFoundException();
        }
    }
}
