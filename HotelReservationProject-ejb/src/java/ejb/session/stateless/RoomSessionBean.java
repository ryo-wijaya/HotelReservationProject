/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanLocal, RoomSessionBeanRemote {

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
        Query query = em.createQuery("SELECT r FROM Room");
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

    //no merge needed as this is a managed context
    public void updateRoom(Long id, Long roomTypeId) throws RoomNotFoundException {
        try {
            Room roomToUpdate = this.getRoomById(id);
            RoomType roomTypeToAdd = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);
            roomToUpdate.setRoomType(roomTypeToAdd);
        } catch (RoomNotFoundException | RoomTypeNotFoundException ex) {
            //roomType exception should not trigger, but we just catch it for compile safety
            throw new RoomNotFoundException();
        }
    }

    //deleting a roomType involves deleting all its associated RoomRates
    /*
    @Override
    public void deleteRoomType(Long id) throws RoomTypeNotFoundException {
        try {
            RoomType roomTypeToDelete = this.getRoomTypeById(id);
            for (RoomRate rr : roomTypeToDelete.getListOfRoomRates()) {
                roomRateSessionBeanLocal.deleteRoomRate(rr.getRoomRateId());
            }
            roomTypeToDelete.getListOfRoomRates().clear();
            em.remove(roomTypeToDelete);

        } catch (RoomTypeNotFoundException | RoomRateNotFoundException ex) {
            //we don't have to worry about a RoomRate not being found, but we still have to catch the potential exception
            throw new RoomTypeNotFoundException();
        }
    }
*/
}
