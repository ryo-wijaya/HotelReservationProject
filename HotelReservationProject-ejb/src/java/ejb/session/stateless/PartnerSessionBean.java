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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.NoPartnersFoundException;

/**
 *
 * @author Jorda
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public PartnerSessionBean() {
    }

    @Override
    public Partner createNewPartner(Partner newPartner) {
        em.persist(newPartner);
        em.flush();
        return newPartner;
        // throw partnerusernameexistexcpetion and unknownpersistenceexception
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
    public Partner retrievePartnerByUsername(String username) {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        return (Partner) query.getSingleResult();
        // catch noResultException | NonUniqueResultException
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
        } catch (LoginCredentialsInvalidException ex) {
            throw new LoginCredentialsInvalidException();
        }
    }

}
