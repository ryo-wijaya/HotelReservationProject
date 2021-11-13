/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
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
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.RateType;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.FailedToCreateRoomRateException;
import util.exceptions.InputDataValidationException;
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

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //creating a new roomRate involves adding it to a given RoomType as well
    @Override
    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) throws FailedToCreateRoomRateException {
        try {
            em.persist(roomRate);
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            roomType.addToListOfRoomRate(roomRate);
            return roomRate.getRoomRateId();
        } catch (EntityInstanceExistsInCollectionException ex) {
            throw new FailedToCreateRoomRateException();
        }
    }

    @Override
    public void updateRoomRate(RoomRate roomRate) throws InputDataValidationException, RoomRateNotFoundException, RoomTypeNotFoundException {
        if (roomRate != null && roomRate.getRoomRateId() != null) {

            Set<ConstraintViolation<RoomRate>> constraintViolations = validator.validate(roomRate);

            if (constraintViolations.isEmpty()) {

                RoomRate roomRateToUpdate = this.getRoomRateById(roomRate.getRoomRateId());

                roomRateToUpdate.setRateType(roomRate.getRateType());
                roomRateToUpdate.setEnabled(roomRate.getEnabled());
                roomRateToUpdate.setStartDate(roomRate.getStartDate());
                roomRateToUpdate.setEndDate(roomRate.getEndDate());
                roomRateToUpdate.setPrice(roomRate.getPrice());
   
            } else {
                throw new InputDataValidationException();
            }
        } else {
            throw new RoomTypeNotFoundException();
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

    @Override
    public void deleteRoomRate(Long id) throws RoomRateNotFoundException {
        RoomRate roomRateToDelete = this.getRoomRateById(id);
        roomRateToDelete.setEnabled(Boolean.FALSE);
    }

    @Override
    public List<RoomRate> getRoomRateByRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);

        if (!roomType.getListOfRoomRates().isEmpty()) {
            return roomType.getListOfRoomRates();
        } else {
            throw new RoomRateNotFoundException();
        }
    }

    public RoomRate getNormalRateForRoomType(Long roomTypeId) throws RoomTypeNotFoundException, RoomRateNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);

        for (RoomRate rr : roomType.getListOfRoomRates()) {
            if (rr.getRateType() == RateType.NORMALRATE) {
                return rr;
            }
        }

        //nothing to return
        throw new RoomRateNotFoundException();
    }
}
