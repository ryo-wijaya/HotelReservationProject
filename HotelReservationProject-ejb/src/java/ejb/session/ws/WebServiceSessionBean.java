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
import entity.Partner;
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
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.RoomNotFoundException;

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
        for(Booking booking : bookings) {
            em.detach(booking);
            booking.setPartner(null);
        }
        return partner;
    }
    
    @WebMethod(operationName = "walkInSearchRoom")
    public List<RoomType> walkInSearchRoom(@WebParam(name = "startDate") Date startDate, @WebParam(name = "endDate") Date endDate) throws LoginCredentialsInvalidException, RoomNotFoundException {
        return roomSessionBean.walkInSearchRoom(startDate, endDate);
    }
}
