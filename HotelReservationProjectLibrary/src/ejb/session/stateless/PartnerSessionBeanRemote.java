/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.partnerNotFoundException;

/**
 *
 * @author Jorda
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Partner createNewPartner(Partner newPartner);

    public List<Partner> retrieveAllPartners();

    public Partner retrievePartnerByPartnerId(Long partnerId)throws partnerNotFoundException;

    public Partner retrievePartnerByUsername(String username);

    public Partner partnerLogin(String username, String password) throws LoginCredentialsInvalidException;
    
}
