/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RatePerNight;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomRateNotFoundException;

/**
 *
 * @author ryo20
 */
@Local
public interface RoomRateSessionBeanLocal {

    public void deleteRoomRate(Long id) throws RoomRateNotFoundException;

    public void updateRoomRate(Long id, RatePerNight newRatePerNight) throws RoomRateNotFoundException;

    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRate> retrieveRoomRates() throws RoomRateNotFoundException;

    public Long createNewRoomRate(RoomRate roomRate, int roomRank) throws FailedToCreateRoomRateException;
    
}
