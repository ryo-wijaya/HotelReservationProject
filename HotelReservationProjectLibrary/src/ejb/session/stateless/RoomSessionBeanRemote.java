/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.BookingNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author ryo20
 */
@Remote
public interface RoomSessionBeanRemote {

    public Long createNewRoom(Room room, Long roomTypeId) throws UnknownPersistenceException, SQLIntegrityViolationException, InputDataValidationException, RoomTypeNotFoundException;

    public List<Room> retrieveRooms() throws RoomNotFoundException;

    public Room getRoomById(Long id) throws RoomNotFoundException;

    public void deleteRoomByRoomNumber(String roomNumber) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException;

    public void updateRoom(Room room) throws RoomNotFoundException;

    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;

    public boolean checkForRoomTypeUsage(String typeName) throws RoomTypeNotFoundException;

    public HashMap<Long, Integer> walkInSearchRoom(Date startDate, Date endDate) throws RoomTypeNotFoundException;

    public List<Room> retrieveRoomsByRoomType(Long roomTypeId) throws RoomNotFoundException;

    public void findARoomAndAddToIt(Long bookingId) throws RoomNotFoundException, BookingNotFoundException;
    
    public void updateRoomStatus(long roomId) throws RoomNotFoundException;
}
