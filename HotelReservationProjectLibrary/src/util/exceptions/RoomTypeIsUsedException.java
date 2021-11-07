/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exceptions;

/**
 *
 * @author Jorda
 */
public class RoomTypeIsUsedException extends Exception{

    /**
     * Creates a new instance of <code>RoomTypeIsUsedException</code> without
     * detail message.
     */
    public RoomTypeIsUsedException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeIsUsedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeIsUsedException(String msg) {
        super(msg);
    }
}
