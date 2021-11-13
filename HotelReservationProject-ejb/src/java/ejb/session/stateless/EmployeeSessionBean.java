/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.InputDataValidationException;
import util.exceptions.LoginCredentialsInvalidException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author ryo20
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public EmployeeSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewEmployee(Employee employee) throws SQLIntegrityViolationException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(employee);
                em.flush();
                return employee.getEmployeeId();
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
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    @Override
    public List<Employee> retrieveAllEmployees() throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e");
        List<Employee> listOfEmployees = query.getResultList();
        if (listOfEmployees != null) {
            return listOfEmployees;
        } else {
            throw new EmployeeNotFoundException();
        }
    }
    
    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException();
        }
    }
    
    @Override
    public Employee getEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee username invalid!");
        }
    } 
    
    @Override
    public Employee login(String username, String password) throws LoginCredentialsInvalidException {
        try {
            Employee employee = this.getEmployeeByUsername(username);
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new LoginCredentialsInvalidException("Wrong password!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new LoginCredentialsInvalidException();
        }
    }
}
