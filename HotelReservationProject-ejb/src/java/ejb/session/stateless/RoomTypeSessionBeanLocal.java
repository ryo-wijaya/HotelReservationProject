/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RateType;
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

    public RoomType getRoomTypeByName(String roomName) throws RoomTypeNotFoundException;

    public List<RoomType> getRoomTypeBelowRanking(Integer ranking);

    public void updateRoomType(Long id, String newName, Integer ranking) throws RoomTypeNotFoundException;

    public List<RoomType> getRoomTypeBetweenRanking(Integer newranking, Integer oldranking);

    public RoomType getRoomTypeByRank(int roomRank) throws RoomTypeNotFoundException;

    public RoomRate getRoomRate(String roomName, RateType rateType) throws RoomTypeNotFoundException;

    public List<RoomRate> getRoomRateByRoomTypeRankAndRateType(int roomRank, RateType rateType);
    
}
