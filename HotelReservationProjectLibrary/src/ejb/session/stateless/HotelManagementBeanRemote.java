/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Remote
public interface HotelManagementBeanRemote {

    public void createNewRoomType(RoomType roomType);

    public List<RoomType> viewAllRoomTypes() throws RoomTypeNotFoundException;

    public RoomType getRoomTypeByName(String roomType);
}
