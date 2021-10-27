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
    public Customer registerAsCustomer(Customer newCustomer)
    {
        em.persist(newCustomer);
        em.flush();
        return newCustomer;
        // throw customer username exits exception. unkownPersistenceexepction
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId)
    {
        Customer customer = em.find(Customer.class, customerId);
        
        if(customer != null)
        {
            return customer;
        }
        else 
        {
            //throw new CustomerNotFoundException("Customer ID" + customerId + " does not exitst!");
            return null;
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
        Query query = em.createQuery("SELECT s FROM Customer c WHERE s.userName = :inUsername");
        query.setParameter("inUserName", username);
        // catch noresult exception | nonuniqueresultexception
        return (Customer)query.getSingleResult();
    }
    
    @Override
    public Customer customerLogin(String username, String password)
    {
        Customer customer = retrieveCustomerByUsername(username);
        
        if(customer.getPassword().equals(password))
        {
            customer.getBookings().size();
            return customer;
        }
        else 
        {
            //throw new LoginCredentialsInvalidException("Username does not exist or invalid password!");
            return null;
        }
    }
    
    
}
