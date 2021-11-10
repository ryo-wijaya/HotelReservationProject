/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.NoPartnersFoundException;
import util.exceptions.NonUniqueCredentialsException;

/**
 *
 * @author Jorda
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;


    @Override
    public long createNewPartner(Partner newPartner) throws NonUniqueCredentialsException {
        //DO BEAN VALIDATION INSTEAD OF TRUE
        if (true) {
            em.persist(newPartner);
            em.flush();
            return newPartner.getPartnerId();
        } else {
            throw new NonUniqueCredentialsException();
        }
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
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
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
