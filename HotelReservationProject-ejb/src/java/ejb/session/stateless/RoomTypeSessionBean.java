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
    public RoomType getRoomTypeByName(String roomName) {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.roomName = :inRoomName");
        query.setParameter("inRoomName", roomName);
        return (RoomType) query.getSingleResult();
    }

    
    @Override
    public List<RoomType> getRoomTypeBelowRanking(Integer ranking)
    {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.ranking >= :inRankng ORDER BY rt.ranking ASC");
        query.setParameter("inRanking", ranking);
        List<RoomType> listOfRoomTypes = query.getResultList();
        if (listOfRoomTypes != null) {
            return listOfRoomTypes;
        } else {
            return null;
        }
    }
    
    //no merge needed as this is a managed context
    @Override
    public void updateRoomType(Long id, String newName, Integer ranking) throws RoomTypeNotFoundException {
        RoomType roomTypeToUpdate = this.getRoomTypeById(id);
        roomTypeToUpdate.setRoomName(newName);
        roomTypeToUpdate.setRanking(ranking);
        em.merge(roomTypeToUpdate);
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

}
