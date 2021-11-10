/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
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
        if (roomType.getEnabled()) {
            room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            em.persist(room);
            em.flush();
            return room.getRoomId();
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public List<Room> retrieveRooms() throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.enabled = :inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        List<Room> listOfRooms = query.getResultList();
        if (!listOfRooms.isEmpty()) {
            for (Room r : listOfRooms) {
                r.getBookings().size();
                r.getRoomType();
            }
            return listOfRooms;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException {
        Room room = em.find(Room.class, id);
        if (room != null && room.isEnabled()) {
            room.getBookings().size();
            room.getRoomType();
            return room;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :inRoomNumber AND r.enabled = :inEnabled");
        query.setParameter("inRoomNumber", roomNumber);
        query.setParameter("inEnabled", Boolean.TRUE);
        Room room = (Room) query.getSingleResult();
        if (room != null) {
            room.getRoomType();
            room.getBookings().size();
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
        try {
            Room roomToDelete = this.getRoomByRoomNumber(roomNumber);
            RoomType roomTypeOfDeletedRoom = roomTypeSessionBeanLocal.getRoomTypeById(roomToDelete.getRoomType().getRoomTypeId());
            roomTypeOfDeletedRoom.setRoomInventory(roomTypeOfDeletedRoom.getRoomInventory() + 1);
            roomToDelete.getBookings().clear();
            roomToDelete.setEnabled(false);
        } catch (RoomTypeNotFoundException ex) {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public boolean checkForRoomTypeUsage(String typeName) throws RoomTypeNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeByName(typeName);
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :inRoomType AND r.enabled = :inEnabled");
        query.setParameter("inRoomType", roomType);
        query.setParameter("inEnabled", Boolean.TRUE);
        List<Room> rooms = query.getResultList();
        return rooms.isEmpty();
    }

    @Override
    public List<RoomType> walkInSearchRoom(Date startDate, Date endDate) throws RoomNotFoundException {
        List<Room> rooms = this.retrieveRooms();
        List<Room> freeRooms = new ArrayList<>();
        

        for (Room r : rooms) {
            Boolean thisRoomWillBeFree = true;

            for (Booking b : r.getBookings()) {
                if (startDate.before(b.getCheckOutDate())) { //THIS MEANS THAT THERES CLASH
                    if (endDate.after(b.getCheckInDate())) {
                        thisRoomWillBeFree = false;
                    }
                }
            }
            if (thisRoomWillBeFree) {
                freeRooms.add(r);
            }
        }
        return null;
    }
}
