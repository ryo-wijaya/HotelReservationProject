/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Remote;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author Jorda
 */
@Remote
public interface CustomerSessionBeanRemote {
    
    public Customer retrieveCustomerByCustomerId(Long customerId)throws CustomerNotFoundException;
    
    public long registerAsCustomer(Customer newCustomer) throws SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException;
    
    public List<Customer> retrieveAllCustomer();
    
    public Customer retrieveCustomerByUsername(String username);
    
    public Customer customerLogin(String username, String password)throws LoginCredentialsInvalidException;
    
}
