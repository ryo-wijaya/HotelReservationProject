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
public class SQLIntegrityViolationException extends Exception {

    /**
     * Creates a new instance of <code>SQLIntegrityViolationException</code>
     * without detail message.
     */
    public SQLIntegrityViolationException() {
    }

    /**
     * Constructs an instance of <code>SQLIntegrityViolationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SQLIntegrityViolationException(String msg) {
        super(msg);
    }
}
