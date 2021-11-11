/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Customer;
import entity.Partner;
import entity.RoomRate;
import entity.RoomType;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.BookingExceptionType;
import util.enumeration.RateType;
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
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal, BookingSessionBeanRemote {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

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
    public long createNewBookingWithCustomer(Booking booking, Long roomTypeId, Long customerId) throws RoomTypeNotFoundException, CustomerNotFoundException, EntityInstanceExistsInCollectionException {
        RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
        Customer customer = customerSessionBean.retrieveCustomerByCustomerId(customerId);
        if (roomType.getEnabled()) {
            booking.setRoomType(roomType);
            booking.setCustomer(customer);
            em.persist(booking);
            em.flush();
            customer.addBooking(booking);
            return booking.getBookingId();
        } else {
            throw new RoomTypeNotFoundException();
        }
    }
    
    @Override
    public long createNewBookingWithPartner(Booking booking, Long roomTypeId, Long partnerId) throws RoomTypeNotFoundException, EntityInstanceExistsInCollectionException, NoPartnersFoundException {
        RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
        Partner partner = partnerSessionBean.retrievePartnerByPartnerId(partnerId);
        if (roomType.getEnabled()) {
            booking.setRoomType(roomType);
            booking.setPartner(partner);
            em.persist(booking);
            em.flush();
            partner.addBooking(booking);
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
    public Double getPublishRatePriceOfBooking(Booking booking) throws RoomRateNotFoundException {
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

    @Override
    public Double getRateForOnlineBooking(Booking booking) throws RoomRateNotFoundException {
        Double price = 0.0;
        try {
            System.out.println("booking.getRoomType(): " + booking.getRoomType());
            System.out.println("roomtype's ID: " + booking.getRoomType().getRoomTypeId());
            List<RoomRate> roomRates = roomRateSessionBeanLocal.getRoomRateByRoomType(booking.getRoomType().getRoomTypeId());
            RoomType roomType = booking.getRoomType();
            Integer numOfRooms = booking.getNumberOfRooms();
            Date startDate = booking.getCheckInDate();
            Date endDate = booking.getCheckOutDate();
            RoomRate normalRate = null;

            while (startDate.before(endDate)) {
                RoomRate peakRate = null;
                RoomRate promotionRate = null;

                for (RoomRate rr : roomRates) {
                    //if the startDate is between the dates for each rate (and the rate is not normal or published rate)
                    if (rr.getEndDate() != null && rr.getStartDate().compareTo(startDate) * startDate.compareTo(rr.getEndDate()) >= 0) {
                        if (rr.getRateType() == RateType.PROMOTIONRATE) {
                            promotionRate = rr;
                        } else if (rr.getRateType() == RateType.PEAKRATE) {
                            peakRate = rr;
                        }
                    }
                }

                if (peakRate != null) {
                    price = price + (peakRate.getPrice() * booking.getNumberOfRooms());
                } else if (promotionRate != null) {
                    price = price + (promotionRate.getPrice() * booking.getNumberOfRooms());
                } else {
                    //No peak or promotion rate
                    normalRate = roomRateSessionBeanLocal.getNormalRateForRoomType(booking.getRoomType().getRoomTypeId());
                    price = price + (normalRate.getPrice() * booking.getNumberOfRooms());
                }

                //increment date by 1
                startDate = this.incrementDays(startDate);
            }

        } catch (RoomRateNotFoundException | RoomTypeNotFoundException ex) {
            throw new RoomRateNotFoundException();
        }
        return price;
    }

    private Date incrementDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    @Override
    public List<Booking> retrieveUnallocatedBookings() throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.preBooking = :inPreBooking");
        query.setParameter("inPreBooking", Boolean.TRUE);
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
    public List<Booking> retrieveTypeOneBookings() throws TypeOneNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.bookingExceptionType = :inBookingExceptionType");
        query.setParameter("inBookingExceptionType", BookingExceptionType.TYPE1);
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
            throw new TypeOneNotFoundException();
        }
    }

    @Override
    public List<Booking> retrieveTypeTwoBookings() throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.bookingExceptionType = :inBookingExceptionType");
        query.setParameter("inBookingExceptionType", BookingExceptionType.TYPE2);
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
