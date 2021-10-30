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
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author Jorda
 */
@Remote
public interface CustomerSessionBeanRemote {
    
    public Customer retrieveCustomerByCustomerId(Long customerId)throws CustomerNotFoundException;
    
    public Customer registerAsCustomer(Customer newCustomer);
    
    public List<Customer> retrieveAllCustomer();
    
    public Customer retrieveCustomerByUsername(String username);
    
    public Customer customerLogin(String username, String password)throws LoginCredentialsInvalidException;
    
}
