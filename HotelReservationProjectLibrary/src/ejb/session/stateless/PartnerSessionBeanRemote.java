/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface PartnerSessionBeanRemote {
    
    public long createNewPartner(Partner newPartner) throws SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException;

    public List<Partner> retrieveAllPartners() throws NoPartnersFoundException;

    public Partner retrievePartnerByPartnerId(Long partnerId)throws NoPartnersFoundException;

    public Partner retrievePartnerByUsername(String username) throws NoPartnersFoundException;

    public Partner partnerLogin(String username, String password) throws LoginCredentialsInvalidException;
    
}
