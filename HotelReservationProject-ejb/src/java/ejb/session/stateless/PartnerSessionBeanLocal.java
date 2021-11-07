/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.PartnerNotFoundException;

/**
 *
 * @author Jorda
 */
@Local
public interface PartnerSessionBeanLocal {

    public Partner createNewPartner(Partner newPartner);

    public List<Partner> retrieveAllPartners() throws PartnerNotFoundException;

    public Partner retrievePartnerByPartnerId(Long partnerId)throws PartnerNotFoundException;

    public Partner retrievePartnerByUsername(String username);

    public Partner partnerLogin(String username, String password) throws LoginCredentialsInvalidException;
    
}
