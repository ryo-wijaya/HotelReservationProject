/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.BookingExceptionType;
import util.exceptions.BookingNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.InputDataValidationException;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;
import util.exceptions.SQLIntegrityViolationException;
import util.exceptions.UnknownPersistenceException;

/**
 *
 * @author ryo20
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanLocal, RoomSessionBeanRemote {

    @EJB
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationProject-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewRoom(Room room, Long roomTypeId) throws UnknownPersistenceException, SQLIntegrityViolationException, InputDataValidationException, RoomTypeNotFoundException {
        Set<ConstraintViolation<Room>> constraintViolations = validator.validate(room);
        if (constraintViolations.isEmpty()) {
            try {
                RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);
                if (roomType.getEnabled()) {
                    room.setRoomType(roomType);
                    roomType.setRoomInventory(roomType.getRoomInventory() + 1);
                    em.persist(room);
                    em.flush();
                    return room.getRoomId();
                } else {
                    return null;
                }
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new SQLIntegrityViolationException();
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

    @Override
    public List<Room> retrieveRooms() throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.enabled = :inEnabled");
        query.setParameter("inEnabled", Boolean.TRUE);
        List<Room> listOfRooms = query.getResultList();
        if (!listOfRooms.isEmpty()) {
            for (Room r : listOfRooms) {
                r.getBookings().size();
                r.getRoomType();
            }
            return listOfRooms;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException {
        Room room = em.find(Room.class, id);
        if (room != null && room.isEnabled()) {
            room.getBookings().size();
            room.getRoomType();
            return room;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :inRoomNumber AND r.enabled = :inEnabled");
        query.setParameter("inRoomNumber", roomNumber);
        query.setParameter("inEnabled", Boolean.TRUE);
        Room room = (Room) query.getSingleResult();
        if (room != null) {
            room.getRoomType();
            room.getBookings().size();
            return room;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public void updateRoom(Room room) throws InputDataValidationException, RoomNotFoundException {
        if (room != null && room.getRoomId() != null) {
            
            Set<ConstraintViolation<Room>> constraintViolations = validator.validate(room);

            if (constraintViolations.isEmpty()) {

                Room roomToUpdate = this.getRoomById(room.getRoomId());
                
                roomToUpdate.setRoomNumber(room.getRoomNumber());
                roomToUpdate.setRoomStatus(room.getRoomStatus());
                roomToUpdate.setRoomType(room.getRoomType());
                roomToUpdate.setEnabled(room.isEnabled());
                roomToUpdate.setBookings(room.getBookings());
            } else {
                throw new InputDataValidationException();
            }
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public void updateRoomStatus(long roomId) throws RoomNotFoundException {
        Room room = getRoomById(roomId);
        if (room.getRoomStatus() == false) {
            room.setRoomStatus(Boolean.TRUE);
        } else {
            room.setRoomStatus(Boolean.FALSE);
        }
    }

    //deleting a roomType involves deleting all its associated RoomRates
    @Override
    public void deleteRoomByRoomNumber(String roomNumber) throws RoomNotFoundException, RoomIsTiedToABookingDeletionException {
        try {
            Room roomToDelete = this.getRoomByRoomNumber(roomNumber);
            RoomType roomTypeOfDeletedRoom = roomTypeSessionBeanLocal.getRoomTypeById(roomToDelete.getRoomType().getRoomTypeId());
            roomTypeOfDeletedRoom.setRoomInventory(roomTypeOfDeletedRoom.getRoomInventory() + 1);
            roomToDelete.getBookings().clear();
            roomToDelete.setEnabled(false);
        } catch (RoomTypeNotFoundException ex) {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public boolean checkForRoomTypeUsage(String typeName) throws RoomTypeNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeByName(typeName);
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :inRoomType AND r.enabled = :inEnabled");
        query.setParameter("inRoomType", roomType);
        query.setParameter("inEnabled", Boolean.TRUE);
        List<Room> rooms = query.getResultList();
        return rooms.isEmpty();
    }

    @Override
    public HashMap<Long, Integer> walkInSearchRoom(Date startDate, Date endDate) throws RoomTypeNotFoundException {
        // New and improved method, unlike the shitty monstrocity above
        // Two things, for the given date:
        // 1. Check for any allocated bookings, subtract the booked rooms
        // 2. Check for any unallocated bookings, subtract the booked rooms

        // Mapping from Room Type ID to number of free rooms left
        // Remember to do the check in the clients
        HashMap<Long, Integer> map = new HashMap<>();

        try {
            //pre-processing step
            List<RoomType> roomTypes = roomTypeSessionBeanLocal.retrieveRoomTypes();
            for (RoomType rt : roomTypes) {
                //The map now contains every single Room Type mapped to its current inventory
                map.put(rt.getRoomTypeId(), rt.getRoomInventory());
            }

            List<Booking> bookings = bookingSessionBeanLocal.retrieveBookings();
            for (Booking b : bookings) {
                System.out.println("Booking id: " + b.getBookingId());
                // If the search date interval clashes with the booking in question
                if (startDate.before(b.getCheckOutDate()) && endDate.after(b.getCheckInDate())) {

                    // Updating the number of free rooms for that specific Room Type
                    System.out.println("Booking id: " + b.getBookingId() + " Booking number of rooms: " + b.getNumberOfRooms());
                    Long roomTypeToUpdate = b.getRoomType().getRoomTypeId();
                    map.replace(roomTypeToUpdate, map.get(roomTypeToUpdate) - b.getNumberOfRooms());
                }
            }

            //THIS PART IS ADDED
            //subtracting all rooms where status == true (THIS MEANS THE ROOM STATUS IS NOT FREE)
            List<Room> allRooms = this.retrieveRooms();
            for (Room r : allRooms) {
                Long roomTypeIdOfRoom = r.getRoomType().getRoomTypeId();

                if (r.getRoomStatus()) {
                    if (map.get(roomTypeIdOfRoom) > 0) {
                        map.replace(roomTypeIdOfRoom, map.get(roomTypeIdOfRoom) - 1);
                    }
                }
            }

        } catch (BookingNotFoundException ex) {
            return map;
        } catch (RoomTypeNotFoundException | RoomNotFoundException ex) {
            throw new RoomTypeNotFoundException();
        }

        return map;
    }

    @Override
    public List<Room> retrieveRoomsByRoomType(Long roomTypeId) throws RoomNotFoundException {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.enabled = :inEnabled AND r.roomType.roomTypeId = :inRoomTypeId");
        query.setParameter("inEnabled", true);
        query.setParameter("inRoomTypeId", roomTypeId);
        List<Room> listOfRooms = query.getResultList();
        if (!listOfRooms.isEmpty()) {
            for (Room r : listOfRooms) {
                r.getBookings().size();
                r.getRoomType();
            }
            return listOfRooms;
        } else {
            throw new RoomNotFoundException();
        }
    }

    @Override
    public void findARoomAndAddToIt(Long bookingId) throws RoomNotFoundException, BookingNotFoundException {
        Booking booking;
        booking = bookingSessionBeanLocal.retrieveBookingByBookingId(bookingId);
        booking.setPreBooking(false);
        List<Room> rooms = this.retrieveRoomsByRoomType(booking.getRoomType().getRoomTypeId());
        Date startDate = booking.getCheckInDate();
        Date endDate = booking.getCheckOutDate();
        Boolean thisRoomWillBeFree = true;
        //Integer totalAmountOfRooms = booking.getNumberOfUnallocatedRooms();
        try {
            for (Room r : rooms) {
                //ADDED THE BOTTOM 5 LINES TO CHECK ROOM STATUS
                if (r.getRoomStatus()) {
                    thisRoomWillBeFree = false;
                } else {
                    thisRoomWillBeFree = true;
                }
                for (Booking b : r.getBookings()) {
                    if (startDate.compareTo(b.getCheckOutDate()) < 0) { //if startDate is before a room's booking's checkout date (coz if its after we good to go)
                        if (endDate.compareTo(b.getCheckInDate()) > 0) { //if endDate stop at a room's booking's checkin date
                            thisRoomWillBeFree = false; //THIS MEANS THAT THERES CLASH
                            break;
                        }
                    }
                }
                if (thisRoomWillBeFree) {

                    r.addBookings(booking);
                    booking.addRoom(r);
                    booking.setNumberOfUnallocatedRooms(booking.getNumberOfUnallocatedRooms() - 1);

                }
                if (booking.getNumberOfUnallocatedRooms() == 0) {
                    return;
                }
            }
            if (booking.getNumberOfUnallocatedRooms() > 0) {
                thisRoomWillBeFree = true;
                booking.setBookingExceptionType(BookingExceptionType.ERROR);
                String nextHigherRoomTypeString = booking.getRoomType().getNextHigherRoomType();
                RoomType nextHigherType = roomTypeSessionBeanLocal.getRoomTypeByName(nextHigherRoomTypeString);
                booking.setRoomType(nextHigherType);
                List<Room> rooms2 = this.retrieveRoomsByRoomType(booking.getRoomType().getRoomTypeId());
                for (Room r : rooms2) {
                    //ADDED THE BOTTOM 5 LINES TO CHECK ROOM STATUS
                    if (r.getRoomStatus()) {
                        thisRoomWillBeFree = false;
                    } else {
                        thisRoomWillBeFree = true;
                    }
                    for (Booking b : r.getBookings()) {
                        if (startDate.compareTo(b.getCheckOutDate()) < 0) { //if startDate is before a room's booking's checkout date (coz if its after we good to go)
                            if (endDate.compareTo(b.getCheckInDate()) > 0) { //if endDate stop at a room's booking's checkin date
                                thisRoomWillBeFree = false; //THIS MEANS THAT THERES CLASH
                                break;
                            }
                        }
                    }
                    if (thisRoomWillBeFree) {
                        r.addBookings(booking);
                        booking.addRoom(r);
                        booking.setNumberOfUnallocatedRooms(booking.getNumberOfUnallocatedRooms() - 1);
                        booking.setNumOfTypeOnes(booking.getNumOfTypeOnes() + 1);
                    }
                    if (booking.getNumberOfUnallocatedRooms() == 0) {
                        return;
                    }
                }
            }
            if (booking.getNumberOfUnallocatedRooms() > 0) {
                booking.setNumOfTypeTwos(booking.getNumberOfUnallocatedRooms());
            }
        } catch (EntityInstanceExistsInCollectionException | RoomTypeNotFoundException ex) {
            throw new RoomNotFoundException();
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Room>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
