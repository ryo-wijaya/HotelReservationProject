/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.RoomRate;
import entity.RoomType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exceptions.EmployeeNotFoundException;
import util.enumeration.EmployeeRole;
import static util.enumeration.EmployeeRole.GUESTRELATIONSOFFICER;
import static util.enumeration.EmployeeRole.OPERATIONMANAGER;
import static util.enumeration.EmployeeRole.SALESMANAGER;
import static util.enumeration.EmployeeRole.SYSTEMADMINISTRATOR;
import util.enumeration.RateType;
import static util.enumeration.RateType.NORMALRATE;
import static util.enumeration.RateType.PUBLISHRATE;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomRateNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

/**
 *
 * @author ryo20
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    
    
    
    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.getEmployeeById(1l);
            roomRateSessionBean.getRoomRateById(1l);
            roomTypeSessionBean.getRoomTypeById(1l);
            roomSessionBean.getRoomById(1l);
        } catch (EmployeeNotFoundException | RoomRateNotFoundException | RoomTypeNotFoundException | RoomNotFoundException ex) {
            this.loadData();
        }
    }
    
    public void loadData() {
        Employee employee = new Employee("admin", "sysadmin", "password", SYSTEMADMINISTRATOR);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("op", "opmanager", "password", OPERATIONMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("sales", "salesmanager", "password", SALESMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("guest", "guestrelo", "password", GUESTRELATIONSOFFICER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        
        RoomType roomType = new RoomType("Grand Suite", 1, "Grand Suite", "GRAND", 3, 6, new ArrayList<>());
        RoomRate publishRoomRate = new RoomRate(PUBLISHRATE, 100.00, null, null);
        RoomRate normalRoomRate = new RoomRate(NORMALRATE, 50.00, null, null);
        roomType.addToListOfRoomRate(publishRoomRate);
        roomType.addToListOfRoomRate(normalRoomRate);
        roomTypeSessionBean.createNewRoomType(roomType);
        
    }
}
