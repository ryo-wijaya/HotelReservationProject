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
        
        
        
        try{
            
            RoomType roomType = new RoomType("Grand Suite", "Grand Suite", "GRAND", 3, 6);
            roomType.setNextHigherRoomType("None");
            em.persist(roomType);
            em.flush();                    
            
            RoomRate publishRoomRate = new RoomRate(PUBLISHRATE, 500.00);
            em.persist(publishRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(publishRoomRate);
            
            RoomRate normalRoomRate = new RoomRate(NORMALRATE, 250.00);
            em.persist(normalRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(normalRoomRate);
            
            Room room = new Room("0105");
            room.setRoomType(roomType);
            em.persist(room);
            em.flush();   
            //room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            Room room1 = new Room("0205");
            room1.setRoomType(roomType);
            em.persist(room1);
            em.flush();   
            //room1.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            Room room2 = new Room("0305");
            room2.setRoomType(roomType);
            em.persist(room2);
            em.flush();   
            //room2.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            Room room3 = new Room("0405");
            room3.setRoomType(roomType);
            em.persist(room3);
            em.flush();   
            //room3.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            Room room4 = new Room("0505");
            room4.setRoomType(roomType);
            em.persist(room4);
            em.flush();   
            //room4.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            roomType = new RoomType("Junior Suite", "Junior Suite", "JUNIOR", 3, 5);
            roomType.setNextHigherRoomType("Grand Suite");
            em.persist(roomType);
            em.flush();
            
            publishRoomRate = new RoomRate(PUBLISHRATE, 400.00);
            em.persist(publishRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(publishRoomRate);
            
            normalRoomRate = new RoomRate(NORMALRATE, 200.00);
            em.persist(normalRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(normalRoomRate);
            
            room = new Room("0104");
            room.setRoomType(roomType);
            em.persist(room);
            em.flush();   
            //room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room1 = new Room("0204");
            room1.setRoomType(roomType);
            em.persist(room1);
            em.flush();   
            //room1.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room2 = new Room("0304");
            room2.setRoomType(roomType);
            em.persist(room2);
            em.flush();   
            //room2.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room3 = new Room("0404");
            room3.setRoomType(roomType);
            em.persist(room3);
            em.flush();   
            //room3.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room4 = new Room("0504");
            room4.setRoomType(roomType);
            em.persist(room4);
            em.flush();   
            //room4.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            roomType = new RoomType("Family Room", "Family Room", "FAMILY", 3, 4);
            roomType.setNextHigherRoomType("Junior Suite");
            em.persist(roomType);
            em.flush();
            
            publishRoomRate = new RoomRate(PUBLISHRATE, 300.00);
            em.persist(publishRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(publishRoomRate);
            
            normalRoomRate = new RoomRate(NORMALRATE, 150.00);
            em.persist(normalRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(normalRoomRate);
            
            room = new Room("0103");
            room.setRoomType(roomType);
            em.persist(room);
            em.flush();   
            //room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room1 = new Room("0203");
            room1.setRoomType(roomType);
            em.persist(room1);
            em.flush();   
            //room1.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room2 = new Room("0303");
            room2.setRoomType(roomType);
            em.persist(room2);
            em.flush();   
            //room2.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room3 = new Room("0403");
            room3.setRoomType(roomType);
            em.persist(room3);
            em.flush();   
            //room3.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room4 = new Room("0503");
            room4.setRoomType(roomType);
            em.persist(room4);
            em.flush();   
            //room4.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            roomType = new RoomType("Premier Room", "Premier Room", "PREMIER", 2, 4);
            roomType.setNextHigherRoomType("Family Room");
            em.persist(roomType);
            em.flush();
            
            publishRoomRate = new RoomRate(PUBLISHRATE, 200.00);
            em.persist(publishRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(publishRoomRate);
            
            normalRoomRate = new RoomRate(NORMALRATE, 100.00);
            em.persist(normalRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(normalRoomRate);
            
            room = new Room("0102");
            room.setRoomType(roomType);
            em.persist(room);
            em.flush();   
            //room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room1 = new Room("0202");
            room1.setRoomType(roomType);
            em.persist(room1);
            em.flush();   
            //room1.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room2 = new Room("0302");
            room2.setRoomType(roomType);
            em.persist(room2);
            em.flush();   
            //room2.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room3 = new Room("0402");
            room3.setRoomType(roomType);
            em.persist(room3);
            em.flush();   
            //room3.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room4 = new Room("0502");
            room4.setRoomType(roomType);
            em.persist(room4);
            em.flush();   
            //room4.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            roomType = new RoomType("Deluxe Room", "Deluxe Room", "DELUXE", 2, 3);
            roomType.setNextHigherRoomType("Premier Room");
            em.persist(roomType);
            em.flush();
            
            publishRoomRate = new RoomRate(PUBLISHRATE, 100.00);
            em.persist(publishRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(publishRoomRate);
            
            normalRoomRate = new RoomRate(NORMALRATE, 50.00);
            em.persist(normalRoomRate);
            em.flush();   
            roomType.addToListOfRoomRate(normalRoomRate);
            
            room = new Room("0101");
            room.setRoomType(roomType);
            em.persist(room);
            em.flush();   
            //room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room1 = new Room("0201");
            room1.setRoomType(roomType);
            em.persist(room1);
            em.flush();   
            //room1.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room2 = new Room("0301");
            room2.setRoomType(roomType);
            em.persist(room2);
            em.flush();   
            //room2.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room3 = new Room("0401");
            room3.setRoomType(roomType);
            em.persist(room3);
            em.flush();   
            //room3.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
            room4 = new Room("0501");
            room4.setRoomType(roomType);
            em.persist(room4);
            em.flush();   
            //room4.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            
        } catch (EntityInstanceExistsInCollectionException ex) {
            System.out.println("ERROR");
        }
        
        System.out.println("Successfully deployed");
    }
}
