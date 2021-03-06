/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanLocal, RoomTypeSessionBeanRemote {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomTypeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewRoomType(RoomType roomType) throws UnknownPersistenceException, SQLIntegrityViolationException, InputDataValidationException {
        Set<ConstraintViolation<RoomType>> constraintViolations = validator.validate(roomType);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(roomType);
                em.flush();
                return roomType.getRoomTypeId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new SQLIntegrityViolationException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<RoomType> retrieveRoomTypes() throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.enabled = :inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        List<RoomType> listOfRoomTypes = query.getResultList();
        if (listOfRoomTypes != null) {
            for (RoomType rt : listOfRoomTypes) {
                rt.getListOfRoomRates().size();
            }
            return listOfRoomTypes;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getRoomTypeById(Long id) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, id);
        if (roomType != null && roomType.getEnabled()) {
            roomType.getListOfRoomRates().size();
            return roomType;
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.roomName = :inRoomTypeName AND r.enabled =:inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        query.setParameter("inRoomTypeName", roomTypeName);

        try {
            RoomType roomType = (RoomType) query.getSingleResult();
            roomType.getListOfRoomRates().size();
            return roomType;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public void updateRoomType(RoomType roomType) throws InputDataValidationException, RoomTypeNotFoundException {
        if (roomType != null && roomType.getRoomTypeId() != null) {
            
            Set<ConstraintViolation<RoomType>>constraintViolations = validator.validate(roomType);
            
            if (constraintViolations.isEmpty()) {
                
                RoomType roomTypeToUpdate = this.getRoomTypeById(roomType.getRoomTypeId());
                
                roomTypeToUpdate.setRoomName(roomType.getRoomName());
                roomTypeToUpdate.setDescription(roomType.getDescription());
                roomTypeToUpdate.setNextHigherRoomType(roomType.getNextHigherRoomType());
                roomTypeToUpdate.setEnabled(roomType.getEnabled());
                roomTypeToUpdate.setAmenities(roomType.getAmenities());
                roomTypeToUpdate.setBeds(roomType.getBeds());
                roomTypeToUpdate.setCapacity(roomType.getCapacity());
                roomTypeToUpdate.setListOfRoomRates(roomType.getListOfRoomRates());
                roomTypeToUpdate.setRoomInventory(roomType.getRoomInventory());
                roomTypeToUpdate.setRoomSize(roomType.getRoomSize());
            } else {
                throw new InputDataValidationException();
            }
        } else {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public void deleteRoomType(Long id) throws RoomTypeNotFoundException {
        boolean roomTypeEmpty = roomSessionBean.checkForRoomTypeUsage(getRoomTypeById(id).getRoomName());
        if (roomTypeEmpty) {
            try {
                RoomType roomTypeToDelete = this.getRoomTypeById(id);
                for (RoomRate rr : roomTypeToDelete.getListOfRoomRates()) {
                    roomRateSessionBeanLocal.deleteRoomRate(rr.getRoomRateId());
                }
                roomTypeToDelete.getListOfRoomRates().clear();
                roomTypeToDelete.setEnabled(false);
            } catch (RoomTypeNotFoundException | RoomRateNotFoundException ex) {
                throw new RoomTypeNotFoundException();
            }
        }
    }

    @Override
    public List<RoomRate> getRoomRateByRoomNameAndRateType(String roomName, RateType rateType) throws RoomTypeNotFoundException {
        RoomType roomType = getRoomTypeByName(roomName);
        List<RoomRate> roomRates = roomType.getListOfRoomRates();
        List<RoomRate> filteredRates = new ArrayList<>();
        for (RoomRate roomRate : roomRates) {
            if (roomRate.getRateType().equals(rateType) && roomRate.getEnabled() == true) {
                filteredRates.add(roomRate);
            }
        }
        return filteredRates;
    }

    @Override
    public void changeNextHigherRoomTypeNameForAChangedRoomTypeName(String oldRoomTypeName, String newRoomTypeName) throws RoomTypeNotFoundException {
        try {
            Query query2 = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.NextHigherRoomType = :inOldName");
            query2.setParameter("inOldName", oldRoomTypeName);

            RoomType roomTypesToChange = (RoomType) query2.getSingleResult();
            roomTypesToChange.setNextHigherRoomType(newRoomTypeName);

            RoomType roomTypeWithNewName = getRoomTypeByName(oldRoomTypeName);
            roomTypeWithNewName.setRoomName(newRoomTypeName);

        } catch (RoomTypeNotFoundException ex) {
            throw new RoomTypeNotFoundException();
        }
    }

    @Override
    public RoomType getTheLowerRoomType(String roomTypeName) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.NextHigherRoomType = :inRoomType");
        query.setParameter("inRoomType", roomTypeName);
        try {
            RoomType roomType = (RoomType) query.getSingleResult();
            roomType.getListOfRoomRates().size();
            return roomType;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException();
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RoomType>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
