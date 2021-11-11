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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exceptions.EmployeeNotFoundException;
import static util.enumeration.EmployeeRole.GUESTRELATIONSOFFICER;
import static util.enumeration.EmployeeRole.OPERATIONMANAGER;
import static util.enumeration.EmployeeRole.SALESMANAGER;
import static util.enumeration.EmployeeRole.SYSTEMADMINISTRATOR;
import static util.enumeration.RateType.NORMALRATE;
import static util.enumeration.RateType.PUBLISHRATE;
import util.exceptions.EntityInstanceExistsInCollectionException;
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
    
    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

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
        
        
        /*
        try{
            
            RoomType roomType = new RoomType("Grand Suite", "Grand Suite", "GRAND", 3, 6);
            roomType.setNextHigherRoomType("None");
            em.persist(roomType);
            
            RoomRate publishRoomRate = new RoomRate(PUBLISHRATE, 100.00);
            em.persist(publishRoomRate);
            roomType.addToListOfRoomRate(publishRoomRate);
            
            RoomRate normalRoomRate = new RoomRate(NORMALRATE, 50.00);
            em.persist(normalRoomRate);
            roomType.addToListOfRoomRate(normalRoomRate);
            
            Room room = new Room("0105");
            em.persist(room);
            room.setRoomType(roomType);
            
            Room room1 = new Room("0205");
            em.persist(room1);
            room1.setRoomType(roomType);
            
            Room room2 = new Room("0305");
            em.persist(room2);
            room2.setRoomType(roomType);
            
            Room room3 = new Room("0405");
            em.persist(room3);
            room3.setRoomType(roomType);
            
            Room room4 = new Room("0505");
            em.persist(room4);
            room4.setRoomType(roomType);
        } catch (EntityInstanceExistsInCollectionException ex) {
            System.out.println("ERROR");
        }
        
        System.out.println("Successfully deployed");
*/
    }
}
