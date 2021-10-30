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
public class partnerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>partnerNotFoundException</code> without
     * detail message.
     */
    public partnerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>partnerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public partnerNotFoundException(String msg) {
        super(msg);
    }
}
