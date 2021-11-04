/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;

/**
 *
 * @author ryo20
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createNewRoom(Room room);

    public List<Room> retrieveRooms() throws RoomNotFoundException;

    public Room getRoomById(Long id) throws RoomNotFoundException;
    
    public void deleteRoom(Long id) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException;

    public void updateRoom(Long id, Long roomTypeId) throws RoomNotFoundException;
}
