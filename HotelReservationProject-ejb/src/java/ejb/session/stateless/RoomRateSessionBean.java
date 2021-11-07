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
    public List<RoomRate> retrieveRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        List<RoomRate> listOfRoomRates = query.getResultList();
        return listOfRoomRates;
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

    //needs to make room rate bidirectional. ADD in RoomTpyeName -> getRoomTypeByName -> add with the query WHERE rr.roomType = roomType
    public RoomRate getRoomRateByRatePerNight(RateType ratePerNight) {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.ratePerNight = :inRatePerNight");
        query.setParameter("inRatePerNight", ratePerNight);
        return (RoomRate) query.getSingleResult();
    }

    @Override
    public void updateRoomRate(Long id, RateType newRatePerNight) throws RoomRateNotFoundException {
        RoomRate roomRateToUpdate = this.getRoomRateById(id);
        roomRateToUpdate.setRateType(newRatePerNight);
    }

    @Override
    public void deleteRoomRate(Long id) throws RoomRateNotFoundException {
        RoomRate roomRateToDelete = this.getRoomRateById(id);
        roomRateToDelete.setEnabled(Boolean.FALSE);
    }
}
