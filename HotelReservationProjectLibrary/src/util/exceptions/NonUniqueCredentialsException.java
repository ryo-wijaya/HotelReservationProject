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
public class NonUniqueCredentialsException extends Exception {

    /**
     * Creates a new instance of <code>NonUniqueCredentialsException</code>
     * without detail message.
     */
    public NonUniqueCredentialsException() {
    }

    /**
     * Constructs an instance of <code>NonUniqueCredentialsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NonUniqueCredentialsException(String msg) {
        super(msg);
    }
}
