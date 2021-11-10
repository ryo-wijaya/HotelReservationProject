/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createNewRoom(Room room, Long roomTypeId) throws RoomTypeNotFoundException;

    public List<Room> retrieveRooms() throws RoomNotFoundException;

    public Room getRoomById(Long id) throws RoomNotFoundException;
    
    public void deleteRoomByRoomNumber(String roomNumber) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException;

    public void updateRoom(Room room) throws RoomNotFoundException;

    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public boolean checkForRoomTypeUsage(String typeName) throws RoomTypeNotFoundException;

    public List<RoomType> walkInSearchRoom(Date startDate, Date endDate) throws RoomNotFoundException;

    public List<Room> retrieveRoomsByRoomType(Long roomTypeId) throws RoomNotFoundException;

    public void findARoomAndAddToIt(Long bookingId) throws RoomNotFoundException;
}
