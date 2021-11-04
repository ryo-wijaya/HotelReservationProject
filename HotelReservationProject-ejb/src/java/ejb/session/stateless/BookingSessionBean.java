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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Booking createNewBooking(Booking booking) {
        em.persist(booking);
        em.flush();
        return booking;
    }
    
    @Override
    public List<Booking> retrieveAllProducts(){
        Query query = em.createQuery("SELECT b FROM Booking b");
        return query.getResultList();
    }
    
    @Override
    public Booking retrieveBookingByBookingId(Long bookingId) throws BookingNotFoundException{
        Booking booking = em.find(Booking.class, bookingId);
        if(booking != null){
            return booking;
        }
        else {
            throw new BookingNotFoundException("Booking ID" + bookingId + " does not exist!");
        }
    }
    
}
