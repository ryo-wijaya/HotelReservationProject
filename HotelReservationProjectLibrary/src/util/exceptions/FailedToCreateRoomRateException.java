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
public class FailedToCreateRoomRateException extends Exception {

    /**
     * Creates a new instance of <code>FailedToCreateRoomRateException</code>
     * without detail message.
     */
    public FailedToCreateRoomRateException() {
    }

    /**
     * Constructs an instance of <code>FailedToCreateRoomRateException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FailedToCreateRoomRateException(String msg) {
        super(msg);
    }
}
