/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RatePerNight;
import util.exceptions.RoomRateNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    public RoomRate createNewRoomRate(RoomRate roomRate) {
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }
    
    public List<RoomRate> retrieveRoomRates() throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomRate");
        List<RoomRate> listOfRoomRates = query.getResultList();
        if (listOfRoomRates != null) {
            return listOfRoomRates;
        } else {
            throw new RoomRateNotFoundException();
        }
    }
    
    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, id);
        if (roomRate != null) {
            return roomRate;
        } else {
            throw new RoomRateNotFoundException();
        }
    }
    
    public void updateRoomRate(Long id, RatePerNight newRate) throws RoomRateNotFoundException {
        try {
            RoomRate roomRateToUpdate = this.getRoomRateById(id);
            roomRateToUpdate.setRatePerNight(newRate);
            em.merge(roomRateToUpdate);
        } catch (RoomRateNotFoundException ex) {
            throw new RoomRateNotFoundException();
        }
    }
    
    public void deleteRoomRate(Long id) throws RoomRateNotFoundException {
        try {
            RoomRate roomRateToDelete = this.getRoomRateById(id);
            em.remove(roomRateToDelete);
        } catch (RoomRateNotFoundException ex) {
            throw new RoomRateNotFoundException();
        }
    }
}
