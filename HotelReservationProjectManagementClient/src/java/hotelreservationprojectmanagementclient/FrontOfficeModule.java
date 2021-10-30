/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelreservationprojectmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;

/**
 *
 * @author ryo20
 */
public class FrontOfficeModule {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    
    
}
