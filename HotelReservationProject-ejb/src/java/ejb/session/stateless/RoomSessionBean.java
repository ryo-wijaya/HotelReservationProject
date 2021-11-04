/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanLocal, RoomSessionBeanRemote {

    @EJB
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRoom(Room room) {
        em.persist(room);
        em.flush();
        return room.getRoomId();
    }

    @Override
    public List<Room> retrieveRooms() throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r");
        List<Room> listOfRooms = query.getResultList();
        if (listOfRooms != null) {
            return listOfRooms;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException {
        Room room = em.find(Room.class, id);
        if (room != null) {
            return room;
        } else {
            throw new RoomNotFoundException();
        }
    }
    
    
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :inRoomNumber");
        query.setParameter("inRoomNumber", roomNumber);
        Room room = (Room) query.getSingleResult();
        if (room != null) {
            return room;
        } else {
            throw new RoomNotFoundException();
        }
    }

    public void updateRoom(Room room) throws RoomNotFoundException {
        //add bean validators
        em.merge(room);
    }

    //deleting a roomType involves deleting all its associated RoomRates
    @Override
    public void deleteRoom(Long id) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException {
        Query query = em.createQuery("SELECT b FROM Booking b");
        List<Booking> bookings = bookingSessionBeanLocal.retrieveAllProducts();
        Room roomToDelete = this.getRoomById(id);

        for (Booking b : bookings) {
            if (b.getRooms().contains(roomToDelete)) {
                throw new RoomIsTiedToABookingDeletionException();
            }
        }

        roomToDelete.getBookings().clear();
        em.remove(roomToDelete);
    }
}
