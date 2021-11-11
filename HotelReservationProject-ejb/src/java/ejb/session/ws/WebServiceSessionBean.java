/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.BookingSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author ryo20
 */
@WebService(serviceName = "WebServiceSessionBean")
@Stateless()
public class WebServiceSessionBean {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private BookingSessionBeanLocal bookingSessionBean;

    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "doLogin")
    public Partner doLogin(@WebParam String username, @WebParam String password) throws LoginCredentialsInvalidException {
        return partnerSessionBean.partnerLogin(username, password);
    }
    
}
