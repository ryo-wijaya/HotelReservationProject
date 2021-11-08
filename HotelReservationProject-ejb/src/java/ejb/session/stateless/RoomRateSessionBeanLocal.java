/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RateType;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomRateNotFoundException;

/**
 *
 * @author ryo20
 */
@Local
public interface RoomRateSessionBeanLocal {

    public void deleteRoomRate(Long id) throws RoomRateNotFoundException;

    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRate> retrieveRoomRates() throws RoomRateNotFoundException;

    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) throws FailedToCreateRoomRateException;

    public void updateRoomRate(RoomRate roomRate);
    
}
