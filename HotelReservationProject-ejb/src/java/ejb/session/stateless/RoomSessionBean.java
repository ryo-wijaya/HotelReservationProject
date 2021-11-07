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
    public Long createNewRoom(Room room, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);
        
        room.setRoomType(roomType);
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
    
    
    @Override
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

    @Override
    public void updateRoom(Room room) throws RoomNotFoundException {
        //add bean validators
        em.merge(room);
    }

    //deleting a roomType involves deleting all its associated RoomRates
    @Override
    public void deleteRoomByRoomNumber(String roomNumber) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException {
        List<Booking> bookings = bookingSessionBeanLocal.retrieveAllProducts();
        Room roomToDelete = this.getRoomByRoomNumber(roomNumber);

        for (Booking b : bookings) {
            if (b.getRooms().contains(roomToDelete)) {
                throw new RoomIsTiedToABookingDeletionException();
            }
            else if (b.getRooms().contains(roomToDelete)){
                b.getRooms().remove(roomToDelete);
            }
        }
        
        roomToDelete.getBookings().clear();
        roomToDelete.setEnabled(false);
    }
    @Override
    public boolean checkForRoomTypeUsage(String typeName) throws RoomTypeNotFoundException{
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeByName(typeName);
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :inRoomType");
        query.setParameter("inRoomType", roomType);
        List<Room> rooms = query.getResultList();
        return rooms.isEmpty();
    }
}
