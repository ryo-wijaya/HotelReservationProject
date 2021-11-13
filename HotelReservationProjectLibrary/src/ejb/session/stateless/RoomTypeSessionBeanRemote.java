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
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author ryo20
 */
@Remote
public interface RoomTypeSessionBeanRemote {

    public Long createNewRoomType(RoomType roomType) throws UnknownPersistenceException, SQLIntegrityViolationException, InputDataValidationException;

    public List<RoomType> retrieveRoomTypes() throws RoomTypeNotFoundException;

    public RoomType getRoomTypeById(Long id) throws RoomTypeNotFoundException;

    public void deleteRoomType(Long id) throws RoomTypeNotFoundException;

    public RoomType getRoomTypeByName(String roomName) throws RoomTypeNotFoundException;
    
    public void updateRoomType(RoomType roomType) throws InputDataValidationException, RoomTypeNotFoundException;
    
    public List<RoomRate> getRoomRateByRoomNameAndRateType(String roomName, RateType rateType) throws RoomTypeNotFoundException;
    
    public void changeNextHigherRoomTypeNameForAChangedRoomTypeName(String oldRoomTypeName, String newRoomTypeName)throws RoomTypeNotFoundException;
    
    public RoomType getTheLowerRoomType(String roomTypeName)throws RoomTypeNotFoundException;
}
