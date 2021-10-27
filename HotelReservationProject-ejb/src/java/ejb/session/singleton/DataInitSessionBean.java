/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exceptions.EmployeeNotFoundException;
import util.enumeration.EmployeeRole;
import static util.enumeration.EmployeeRole.SYSTEMADMINISTRATOR;

/**
 *
 * @author ryo20
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    
    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.getEmployeeById(1l);
        } catch (EmployeeNotFoundException ex) {
            this.loadData();
        }
    }
    
    public void loadData() {
        Employee admin = new Employee("admin", "password", SYSTEMADMINISTRATOR);
        employeeSessionBeanLocal.createNewEmployee(admin);
    }
}
