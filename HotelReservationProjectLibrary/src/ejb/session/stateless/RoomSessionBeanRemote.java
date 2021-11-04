/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;

/**
 *
 * @author ryo20
 */
@Remote
public interface RoomSessionBeanRemote {

    public Long createNewRoom(Room room);

    public List<Room> retrieveRooms() throws RoomNotFoundException;

    public Room getRoomById(Long id) throws RoomNotFoundException;
    
    public void deleteRoom(Long id) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException;
}
