/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exceptions.EmployeeNotFoundException;
import util.exceptions.LoginCredentialsInvalidException;

/**
 *
 * @author ryo20
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee createNewEmployee(Employee employee);

    public List<Employee> retrieveAllEmployees() throws EmployeeNotFoundException;

    public Employee getEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee login(String username, String password) throws LoginCredentialsInvalidException;

    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException;
    
}
