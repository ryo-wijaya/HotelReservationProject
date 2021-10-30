/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;

/**
 *
 * @author ryo20
 */
public class SystemAdministrationModule {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Employee employee;

    public SystemAdministrationModule(CustomerSessionBeanRemote customerSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            PartnerSessionBeanRemote partnerSessionBeanRemote, Employee employee) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employee = employee;
    }
    
    
}
