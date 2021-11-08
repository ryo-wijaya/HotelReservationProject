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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanLocal, RoomRateSessionBeanRemote {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    //creating a new roomRate involves adding it to a given RoomType as well
    @Override
    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) throws FailedToCreateRoomRateException {
        try {
            Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.roomTypeId = :inRoomTypeId");
            query.setParameter("inRoomTypeId", roomTypeId);
            em.persist(roomRate);
            RoomType roomType = (RoomType) query.getSingleResult();
            roomType.addToListOfRoomRate(roomRate);
            return roomRate.getRoomRateId();
        } catch (EntityInstanceExistsInCollectionException ex) {
            throw new FailedToCreateRoomRateException();
        }
    }
    
    @Override
    public void updateRoomRate(RoomRate roomRate) {
        em.merge(roomRate);
    }

    @Override
    public List<RoomRate> retrieveRoomRates() throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.enabled = :inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        List<RoomRate> listOfRoomRates = query.getResultList();
        
        if (!listOfRoomRates.isEmpty()) {
            return listOfRoomRates;
        } else {
            throw new RoomRateNotFoundException();
        }
    }

    @Override
    public RoomRate getRoomRateById(Long id) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, id);
        if (roomRate != null && roomRate.getEnabled()) {
            return roomRate;
        } else {
            throw new RoomRateNotFoundException();
        }
    }

    /* There might be multiple room rates with the same rate type tho?
    public RoomRate getRoomRateByRatePerNight(RateType ratePerNight) throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.rateType = :inRateType AND rr.enabled = :inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        query.setParameter("inRateType", ratePerNight);
        try {
            RoomRate roomRate = (RoomRate) query.getSingleResult();
            return roomRate;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomRateNotFoundException();
        }
    }
    */

    @Override
    public void deleteRoomRate(Long id) throws RoomRateNotFoundException {
        RoomRate roomRateToDelete = this.getRoomRateById(id);
        roomRateToDelete.setEnabled(Boolean.FALSE);
    }
}
