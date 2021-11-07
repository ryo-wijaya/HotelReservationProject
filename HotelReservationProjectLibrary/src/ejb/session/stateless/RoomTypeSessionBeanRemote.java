/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RateType;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Remote
public interface RoomTypeSessionBeanRemote {

    public Long createNewRoomType(RoomType roomType);

    public List<RoomType> retrieveRoomTypes() throws RoomTypeNotFoundException;

    public RoomType getRoomTypeById(Long id) throws RoomTypeNotFoundException;

    public void deleteRoomType(Long id) throws RoomTypeNotFoundException;

    public RoomType getRoomTypeByName(String roomName) throws RoomTypeNotFoundException;
    
    public List<RoomType> getRoomTypeBelowRanking(Integer ranking);

    public void updateRoomType(RoomType roomType);
    
    public List<RoomType> getRoomTypeBetweenRanking(Integer newranking, Integer oldranking);
    
    public RoomType getRoomTypeByRank(int roomRank) throws RoomTypeNotFoundException;
    
    public List<RoomRate> getRoomRate(String roomName, RateType rateType) throws RoomTypeNotFoundException;
    
    public void changeNextHigherRoomTypeNameForAChangedRoomTypeName(String oldRoomTypeName, String newRoomTypeName);
}
