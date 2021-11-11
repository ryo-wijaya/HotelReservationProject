/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author Jorda
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CustomerSessionBean() {
    }
    
    // create noew customer
    @Override
    public long registerAsCustomer(Customer newCustomer)
    {
        em.persist(newCustomer);
        em.flush();
        return newCustomer.getCustomerId();
        // throw customer username exits exception. unkownPersistenceexepction
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        
        if(customer != null)
        {
            customer.getBookings().size();
            return customer;
        }
        else 
        {
            throw new CustomerNotFoundException("Customer ID" + customerId + " does not exitst!");
        }
                
    }
    
    @Override
    public List<Customer> retrieveAllCustomer()
    {
        Query query = em.createQuery("SELECT c FROM Customer c");
        return query.getResultList();
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username)
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.userName = :inUsername");
        query.setParameter("inUsername", username);
        // catch noresult exception | nonuniqueresultexception
        return (Customer)query.getSingleResult();
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws LoginCredentialsInvalidException
    {
        try
        {
            Customer customer = retrieveCustomerByUsername(username);
        
            if(customer.getPassword().equals(password))
            {
                customer.getBookings().size();
                return customer;
            }
            else 
            {
                throw new LoginCredentialsInvalidException("Username does not exist or invalid password!");
            }
        }
        catch(LoginCredentialsInvalidException ex)
        {
            throw new LoginCredentialsInvalidException("Username does not exist or invalid password!");
        }
    }
    
    
}
