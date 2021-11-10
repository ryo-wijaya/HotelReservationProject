/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RateType;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Remote
public interface RoomRateSessionBeanRemote {

    public void deleteRoomRate(Long id) throws RoomRateNotFoundException;

    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException;
    
    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) throws FailedToCreateRoomRateException;
    
    public void updateRoomRate(RoomRate roomRate);
    
    public List<RoomRate> getRoomRateByRoomType(Long roomTypeId) throws RoomTypeNotFoundException;
    
    public RoomRate getNormalRateForRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException;
}
