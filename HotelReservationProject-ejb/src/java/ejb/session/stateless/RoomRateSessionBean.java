/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RatePerNight;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;
    
    
    //creating a new roomRate involves adding it to a given RoomType as well
    @Override
    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) throws FailedToCreateRoomRateException {
        try {
            em.persist(roomRate);
            RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);
            roomType.addToListOfRoomRate(roomRate);
            return roomRate.getRoomRateId();
        } catch (RoomTypeNotFoundException | EntityInstanceExistsInCollectionException ex) {
            throw new FailedToCreateRoomRateException();
        }
    }

    @Override
    public List<RoomRate> retrieveRoomRates() throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        List<RoomRate> listOfRoomRates = query.getResultList();
        if (listOfRoomRates != null) {
            return listOfRoomRates;
        } else {
            throw new RoomRateNotFoundException();
        }
    }

    @Override
    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, id);
        if (roomRate != null) {
            return roomRate;
        } else {
            throw new RoomRateNotFoundException();
        }
    }

    //No em.merge() since we are doing the update in a managed context
    @Override
    public void updateRoomRate(Long id, RatePerNight newRatePerNight) throws RoomRateNotFoundException {
        try {
            RoomRate roomRateToUpdate = this.getRoomRateById(id);
            roomRateToUpdate.setRatePerNight(newRatePerNight);
        } catch (RoomRateNotFoundException ex) {
            throw new RoomRateNotFoundException();
        }
    }

    @Override
    public void deleteRoomRate(Long id) throws RoomRateNotFoundException {
        try {
            RoomRate roomRateToDelete = this.getRoomRateById(id);
            em.remove(roomRateToDelete);
        } catch (RoomRateNotFoundException ex) {
            throw new RoomRateNotFoundException();
        }
    }
}
