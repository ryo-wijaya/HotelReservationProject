/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.BookingSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Booking;
import entity.Customer;
import entity.Partner;
import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exceptions.BookingNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.EntityInstanceMissingInCollectionException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@WebService(serviceName = "WebServiceSessionBean")
@Stateless()
public class WebServiceSessionBean {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    @EJB
    private BookingSessionBeanLocal bookingSessionBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "doLogin")
    public Partner doLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws LoginCredentialsInvalidException {
        Partner partner = partnerSessionBean.partnerLogin(username, password);
        em.detach(partner);
        List<Booking> bookings = partner.getBookings();
        for (Booking booking : bookings) {
            em.detach(booking);
            booking.setPartner(null);
        }
        return partner;
    }

    @WebMethod(operationName = "walkInSearchRoom")
    public List<RoomType> walkInSearchRoom(@WebParam(name = "startDate") Date startDate, @WebParam(name = "endDate") Date endDate) throws RoomNotFoundException {
        return roomSessionBean.walkInSearchRoom(startDate, endDate);
    }

    @WebMethod(operationName = "getRoomTypeByName")
    public RoomType getRoomTypeByName(@WebParam(name = "roomName") String roomName) throws RoomTypeNotFoundException {
        return roomTypeSessionBean.getRoomTypeByName(roomName);
    }

    @WebMethod(operationName = "createNewBookingWithPartner")
    public void createNewBookingWithPartner(@WebParam(name = "booking") Booking booking, @WebParam(name = "roomTypeId") long roomTypeId, @WebParam(name = "partnerId") long partnerId) throws RoomTypeNotFoundException, EntityInstanceExistsInCollectionException, NoPartnersFoundException, BookingNotFoundException {
        bookingSessionBean.createNewBookingWithPartner(booking, roomTypeId, partnerId);
    }

    @WebMethod(operationName = "getAllBookingsByPartnerId")
    public List<Booking> getAllBookingsByPartnerId(@WebParam(name = "partnerId") long partnerId) throws BookingNotFoundException, EntityInstanceMissingInCollectionException {
        List<Booking> bookings = bookingSessionBean.getAllBookingsByPartnerId(partnerId);
        for (Booking booking : bookings) {
            em.detach(booking);

            Partner partner = booking.getPartner();
            em.detach(partner);
            partner.removeBooking(booking);

            Customer customer = booking.getCustomer();
            em.detach(customer);
            customer.removeBooking(booking);

            for (Room room : booking.getRooms()) {
                em.detach(room);
                room.removeBookings(booking);
            }
        }
        return bookings;
    }

    @WebMethod(operationName = "getRateForOnlineBooking")
    public Double getRateForOnlineBooking(@WebParam(name = "bookingId") long bookingId) throws RoomRateNotFoundException {
        return bookingSessionBean.getRateForOnlineBooking(bookingId);
    }
    
    @WebMethod(operationName = "retrievePartnerByPartnerId")
    public Partner retrievePartnerByPartnerId(@WebParam(name = "partnerId") long partnerId) throws NoPartnersFoundException {
        Partner partner = partnerSessionBean.retrievePartnerByPartnerId(partnerId);
        em.detach(partner);
        List<Booking> bookings = partner.getBookings();
        for (Booking booking : bookings) {
            em.detach(booking);
            booking.setPartner(null);
        }
        return partner;
    }
}
