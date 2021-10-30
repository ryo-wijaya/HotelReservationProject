/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author ryo20
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewEmployee(Employee employee) {
        em.persist(employee);
        em.flush();
        return employee.getEmployeeId();
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
