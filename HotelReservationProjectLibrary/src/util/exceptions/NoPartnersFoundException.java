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
public class NoPartnersFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoPartnersFoundException</code> without
     * detail message.
     */
    public NoPartnersFoundException() {
    }

    /**
     * Constructs an instance of <code>NoPartnersFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoPartnersFoundException(String msg) {
        super(msg);
    }
}
