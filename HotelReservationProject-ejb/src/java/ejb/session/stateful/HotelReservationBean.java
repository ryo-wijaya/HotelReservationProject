/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.RoomRateSessionBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Stateful;

/**
 *
 * @author ryo20
 */
@Stateful
public class HotelReservationBean implements HotelReservationBeanRemote {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    
}
