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
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exceptions.EmployeeNotFoundException;
import static util.enumeration.EmployeeRole.GUESTRELATIONSOFFICER;
import static util.enumeration.EmployeeRole.OPERATIONMANAGER;
import static util.enumeration.EmployeeRole.SALESMANAGER;
import static util.enumeration.EmployeeRole.SYSTEMADMINISTRATOR;
import static util.enumeration.RateType.NORMALRATE;
import static util.enumeration.RateType.PUBLISHRATE;
import util.exceptions.FailedToCreateRoomRateException;
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

    @EJB
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
        
        Employee employee = new Employee("admin", "sysadmin", "password", SYSTEMADMINISTRATOR);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("op", "opmanager", "password", OPERATIONMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("sales", "salesmanager", "password", SALESMANAGER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        employee = new Employee("guest", "guestrelo", "password", GUESTRELATIONSOFFICER);
        employeeSessionBeanLocal.createNewEmployee(employee);
        
        
        
        try{
            RoomType roomType = new RoomType("Grand Suite", "Grand Suite", "GRAND", 3, 6);
            roomType.setNextHigherRoomType("None");
            RoomRate publishRoomRate = new RoomRate(PUBLISHRATE, 100.00);
            RoomRate normalRoomRate = new RoomRate(NORMALRATE, 50.00);
            Long roomTypeId = roomTypeSessionBean.createNewRoomType(roomType);
            roomRateSessionBean.createNewRoomRate(normalRoomRate, roomTypeId);
            roomRateSessionBean.createNewRoomRate(publishRoomRate, roomTypeId);
            System.out.println("roomtypeID: "+ roomTypeId);
            Room room = new Room("0105");
            roomSessionBean.createNewRoom(room, roomTypeId);
            room = new Room("0205");
            roomSessionBean.createNewRoom(room, roomTypeId);
            room = new Room("0305");
            roomSessionBean.createNewRoom(room, roomTypeId);
            room = new Room("0405");
            roomSessionBean.createNewRoom(room, roomTypeId);
            room = new Room("0505");
            roomSessionBean.createNewRoom(room, roomTypeId);
        } catch (FailedToCreateRoomRateException ex) {
            System.out.print("ERROR 2 ");
        } catch (RoomTypeNotFoundException ex) {
            System.out.print("ERROR 3 ");
        }
        
        System.out.println("Successfully deployed");
    
        
    }
}
