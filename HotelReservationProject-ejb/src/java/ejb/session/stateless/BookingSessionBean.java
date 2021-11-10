/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.RoomRate;
import entity.RoomType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.exceptions.BookingNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author Jorda
 */
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal, BookingSessionBeanRemote {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    @Override
    public long createNewBooking(Booking booking, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
        if (roomType.getEnabled()) {
            booking.setRoomType(roomType);
            em.persist(booking);
            em.flush();
            return booking.getBookingId();
        } else {
            throw new RoomTypeNotFoundException();
        }
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

    @Override
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

    @Override
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
    
    @Override
    public Double getPublishRatePriceOfBooking(Long bookingId) throws RoomRateNotFoundException {
        Booking booking = em.find(Booking.class, bookingId);
        Double price = 0.0;
        RoomType roomType = booking.getRoomType();
        Integer numOfRooms = booking.getNumberOfRooms();
        RoomRate publishedRate = null;
        
        for (RoomRate rr : roomType.getListOfRoomRates()) {
            if (rr.getRateType() == RateType.PUBLISHRATE) {
                publishedRate = rr;
            }
        }
        
        if (publishedRate == null) {
            throw new RoomRateNotFoundException();
        }
        
        price = publishedRate.getPrice() * numOfRooms * ChronoUnit.DAYS.between(LocalDate.parse((CharSequence) booking.getCheckInDate()), 
                LocalDate.parse((CharSequence) booking.getCheckOutDate()));
        return price;
    }
}
