/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createNewRoomType(RoomType roomType);

    public List<RoomType> retrieveRoomTypes() throws RoomTypeNotFoundException;

    public RoomType getRoomTypeById(Long id) throws RoomTypeNotFoundException;

    public void deleteRoomType(Long id) throws RoomTypeNotFoundException;

    public RoomType getRoomTypeByName(String roomName);

    public List<RoomType> getRoomTypeBelowRanking(Integer ranking);

    public void updateRoomType(Long id, String newName, Integer ranking) throws RoomTypeNotFoundException;

    public List<RoomType> getRoomTypeBetweenRanking(Integer newranking, Integer oldranking);
    
}
