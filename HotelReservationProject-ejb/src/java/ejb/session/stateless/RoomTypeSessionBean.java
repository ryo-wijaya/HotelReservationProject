/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanLocal, RoomTypeSessionBeanRemote {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRoomType(RoomType roomType) {
        em.persist(roomType);
        em.flush();
        return roomType.getRoomTypeId();
    }

    @Override
    public List<RoomType> retrieveRoomTypes() throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> listOfRoomTypes = query.getResultList();
        if (listOfRoomTypes != null) {
            return listOfRoomTypes;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getRoomTypeById(Long id) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, id);
        if (roomType != null) {
            roomType.getListOfRoomRates().size();
            return roomType;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.roomName = :inRoomTypeName");
        query.setParameter("inRoomTypeName", roomTypeName);
        RoomType roomtype = (RoomType) query.getSingleResult();

        if (roomtype != null) {
            return roomtype;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getRoomTypeByRank(int roomRank) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.ranking = :inRank");
        query.setParameter("inRank", roomRank);
        RoomType roomtype = (RoomType) query.getSingleResult();

        if (roomtype != null) {
            return roomtype;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public List<RoomType> getRoomTypeBelowRanking(Integer ranking) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.ranking >= :inRankng ORDER BY rt.ranking ASC");
        query.setParameter("inRanking", ranking);
        List<RoomType> listOfRoomTypes = query.getResultList();
        if (listOfRoomTypes != null) {
            return listOfRoomTypes;
        } else {
            return null;
        }
    }

    @Override
    public List<RoomType> getRoomTypeBetweenRanking(Integer newranking, Integer oldranking) {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.ranking >= :innewRankng AND rt.ranking < :inoldRanking ORDER BY rt.ranking ASC");
        query.setParameter("innewRanking", newranking);
        query.setParameter("inoldRanking", oldranking);
        List<RoomType> listOfRoomTypes = query.getResultList();
        if (listOfRoomTypes != null) {
            return listOfRoomTypes;
        } else {
            return null;
        }
    }

    @Override
    public void updateRoomType(RoomType roomType) {
        em.merge(roomType);
    }

    //deleting a roomType involves deleting all its associated RoomRates 
    @Override
    public void deleteRoomType(Long id) throws RoomTypeNotFoundException {
        try {
            RoomType roomTypeToDelete = this.getRoomTypeById(id);
            for (RoomRate rr : roomTypeToDelete.getListOfRoomRates()) {
                roomRateSessionBeanLocal.deleteRoomRate(rr.getRoomRateId());
            }
            roomTypeToDelete.getListOfRoomRates().clear();
            em.remove(roomTypeToDelete);

        } catch (RoomTypeNotFoundException | RoomRateNotFoundException ex) {
            //we don't have to worry about a RoomRate not being found, but we still have to catch the potential exception
            throw new RoomTypeNotFoundException();
        }
    }
    
    @Override
    public List<RoomRate> getRoomRate(String roomName, RateType rateType) throws RoomTypeNotFoundException {
        RoomType roomType = getRoomTypeByName(roomName);
        List<RoomRate> roomRates = roomType.getListOfRoomRates();
        List<RoomRate> filteredRates = new ArrayList<>();
        for (RoomRate roomRate : roomRates) {
            if (roomRate.getRateType().equals(rateType)) {
                filteredRates.add(roomRate);
            }
        }
        return filteredRates;
    }
    
    /*
    @Override
    public List<RoomRate> getRoomRateByRoomTypeRankAndRateType(int roomRank, RateType rateType) throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.ranking =:inRoomRank");
        query.setParameter("inRoomRank", roomRank);
        RoomType roomtype = (RoomType) query.getSingleResult();
        List<RoomRate> filteredRates = new ArrayList<>();
        
        for (RoomRate rr : roomtype.getListOfRoomRates()) {
            if (rr.getRateType() == rateType) {
                filteredRates.add(rr);
            }
        }
        
        if (filteredRates.isEmpty()) {
            throw new RoomRateNotFoundException();
        } else {
            return filteredRates;
        }
    }
    */
    
    @Override
    public void changeNextHigherRoomTypeNameForAChangedRoomTypeName(String oldRoomTypeName, String newRoomTypeName) {
        Query query1 = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.roomName = :inOldName");
        Query query2 = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.NextHigherRoomType = :inNewName");
        query1.setParameter("inOldName", newRoomTypeName);
        query2.setParameter("inNewName", newRoomTypeName);
        
        RoomType roomTypeWithNewName = (RoomType) query1.getSingleResult();
        roomTypeWithNewName.setRoomName(newRoomTypeName);
        
        List<RoomType> roomTypesToChange = query2.getResultList();
        for (RoomType rt : roomTypesToChange) {
            rt.setNextHigherRoomType(newRoomTypeName);
        }
    }
}
