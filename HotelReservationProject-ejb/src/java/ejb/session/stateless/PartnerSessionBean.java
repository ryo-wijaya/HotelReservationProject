/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Set;
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
import util.exceptions.InputDataValidationException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.NonUniqueCredentialsException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author Jorda
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;


    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PartnerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    @Override
    public long createNewPartner(Partner newPartner) throws SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(newPartner);
        if (constraintViolations.isEmpty()) {
            try {
            em.persist(newPartner);
            em.flush();
            return newPartner.getPartnerId();
        } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new SQLIntegrityViolationException(ex.getMessage());
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    @Override
    public List<Partner> retrieveAllPartners() throws NoPartnersFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p");
        List<Partner> partners = query.getResultList();
        if (!partners.isEmpty()) {
            return partners;
        } else {
            throw new NoPartnersFoundException();
        }
    }

    @Override
    public Partner retrievePartnerByPartnerId(Long partnerId) throws NoPartnersFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        if (partner != null) {
            return partner;
        } else {
            throw new NoPartnersFoundException();
        }
    }

    @Override
    public Partner retrievePartnerByUsername(String username) throws NoPartnersFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.userName = :inUsername");
        query.setParameter("inUsername", username);
        try {
            Partner partner = (Partner) query.getSingleResult();
            return partner;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoPartnersFoundException();
        }
    }

    @Override
    public Partner partnerLogin(String username, String password) throws LoginCredentialsInvalidException {
        try {
            Partner partner = retrievePartnerByUsername(username);
            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new LoginCredentialsInvalidException();
            }
        } catch (LoginCredentialsInvalidException | NoPartnersFoundException ex) {
            throw new LoginCredentialsInvalidException();
        }
    }

}
