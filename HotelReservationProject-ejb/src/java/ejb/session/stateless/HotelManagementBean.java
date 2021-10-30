/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class HotelManagementBean implements HotelManagementBeanRemote {
    
    @EJB
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;
    
    public void createNewRoomType(RoomType roomType) {
        roomTypeSessionBeanLocal.createNewRoomType(roomType);
    }
    
    @Override
    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException { 
        return roomTypeSessionBeanLocal.retrieveRoomTypes();
    }
    @Override
    public RoomType getRoomTypeByName(String roomType) {
        return roomTypeSessionBeanLocal.getRoomTypeByName(roomType);
    }
    
}
