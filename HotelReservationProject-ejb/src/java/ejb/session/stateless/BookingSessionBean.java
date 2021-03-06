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
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.BookingExceptionType;
import util.enumeration.RateType;
import util.exceptions.BookingNotFoundException;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.InputDataValidationException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.TypeOneNotFoundException;
import util.exceptions.UnknownPersistenceException;

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
    
    //private final ValidatorFactory validatorFactory;
    //private final Validator validator;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public BookingSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public long createNewBooking(Booking booking, Long roomTypeId) throws RoomTypeNotFoundException, SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException {
        
        Set<ConstraintViolation<Booking>> constraintViolations = validator.validate(booking);
        RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
        if (constraintViolations.isEmpty()) {
            try {
                if (roomType.getEnabled()) {
                    booking.setRoomType(roomType);
                    em.persist(booking);
                    em.flush();
                    System.out.println("num rooms :" + booking.getNumberOfRooms());
                    return booking.getBookingId();
                } else {
                    throw new RoomTypeNotFoundException();
                }
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new SQLIntegrityViolationException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Booking>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
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
    public List<Booking> getBookingsByCheckInDate(Date checkInDate) throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.checkInDate = :inCheckInDate AND b.preBooking = :inPreBooking");
        query.setParameter("inCheckInDate", checkInDate);
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
    public Double getPublishRatePriceOfBooking(Long roomTypeId, Date startDate, Date endDate, Integer numOfRooms) throws RoomRateNotFoundException {
        
        try {
        Double price = 0.0;
        RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
        RoomRate publishedRate = null;

        for (RoomRate rr : roomType.getListOfRoomRates()) {
            if (rr.getRateType() == RateType.PUBLISHRATE) {
                publishedRate = rr;
            }
        }

        if (publishedRate == null) {
            throw new RoomRateNotFoundException();
        }
        
        long diff = endDate.getTime() -  startDate.getTime();
        TimeUnit time = TimeUnit.DAYS; 
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        price = publishedRate.getPrice() * numOfRooms * diffrence;
        return price;
        
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("This exception should not execute");
            throw new RoomRateNotFoundException();
        }
    }

    @Override
    public Double getRateForOnlineBooking(Long roomTypeId, Date startDate, Date endDate, Integer numOfRoom) throws RoomRateNotFoundException {
        Double price = 0.0;
        try {
            RoomType roomType = roomTypeSessionBean.getRoomTypeById(roomTypeId);
            List<RoomRate> roomRates = roomRateSessionBeanLocal.getRoomRateByRoomType(roomType.getRoomTypeId());
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
                    price = price + (peakRate.getPrice() * numOfRoom);
                } else if (promotionRate != null) {
                    price = price + (promotionRate.getPrice() * numOfRoom);
                } else {
                    //No peak or promotion rate
                    normalRate = roomRateSessionBeanLocal.getNormalRateForRoomType(roomTypeId);
                    price = price + (normalRate.getPrice() * numOfRoom);
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
    public List<Booking> rettrieveErrorBooking() throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b from Booking b WHERE b.bookingExceptionType = :inBookingExceptionType");
        query.setParameter("inBookingExceptionType", BookingExceptionType.ERROR);
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
