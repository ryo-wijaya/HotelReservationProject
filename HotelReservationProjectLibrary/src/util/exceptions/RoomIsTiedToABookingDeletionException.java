/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exceptions;

/**
 *
 * @author ryo20
 */
public class RoomIsTiedToABookingDeletionException extends Exception {

    /**
     * Creates a new instance of
     * <code>RoomIsTiedToABookingDeletionException</code> without detail
     * message.
     */
    public RoomIsTiedToABookingDeletionException() {
    }

    /**
     * Constructs an instance of
     * <code>RoomIsTiedToABookingDeletionException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public RoomIsTiedToABookingDeletionException(String msg) {
        super(msg);
    }
}
