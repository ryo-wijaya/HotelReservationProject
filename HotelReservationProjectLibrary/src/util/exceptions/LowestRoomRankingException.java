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
public class LowestRoomRankingException extends Exception{

    /**
     * Creates a new instance of <code>LowestRoomRankingException</code> without
     * detail message.
     */
    public LowestRoomRankingException() {
    }

    /**
     * Constructs an instance of <code>LowestRoomRankingException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LowestRoomRankingException(String msg) {
        super(msg);
    }
}
