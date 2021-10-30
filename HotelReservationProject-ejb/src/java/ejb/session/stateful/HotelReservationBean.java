/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.BookingSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.RoomType;
import javax.ejb.EJB;
import javax.ejb.Stateful;

/**
 *
 * @author ryo20
 */
@Stateful
public class HotelReservationBean implements HotelReservationBeanRemote {

    @EJB
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    public void createNewRoomType(RoomType roomType) {
        roomTypeSessionBeanLocal.createNewRoomType(roomType);
    }
}
