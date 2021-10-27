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
public class LoginCredentialsInvalidException extends Exception {

    /**
     * Creates a new instance of <code>LoginCredentialsInvalid</code> without
     * detail message.
     */
    public LoginCredentialsInvalidException() {
    }

    /**
     * Constructs an instance of <code>LoginCredentialsInvalid</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginCredentialsInvalidException(String msg) {
        super(msg);
    }
}
