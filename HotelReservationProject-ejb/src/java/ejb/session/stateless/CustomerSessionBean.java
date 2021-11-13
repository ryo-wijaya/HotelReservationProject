/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Customer;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.CustomerNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author Jorda
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CustomerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    // create noew customer
    @Override
    public long registerAsCustomer(Customer newCustomer) throws SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(newCustomer);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCustomer);
                em.flush();
                return newCustomer.getCustomerId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new SQLIntegrityViolationException(ex.getMessage());
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
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
