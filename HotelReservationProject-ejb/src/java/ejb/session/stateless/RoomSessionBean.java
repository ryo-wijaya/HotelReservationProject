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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.BookingExceptionType;
import util.exceptions.BookingNotFoundException;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.RoomIsTiedToABookingDeletionException;
import util.exceptions.RoomNotFoundException;
import util.exceptions.RoomTypeNotFoundException;

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

    @Override
    public Long createNewRoom(Room room, Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = roomTypeSessionBeanLocal.getRoomTypeById(roomTypeId);
        if (roomType.getEnabled()) {
            room.setRoomType(roomType);
            roomType.setRoomInventory(roomType.getRoomInventory() + 1);
            em.persist(room);
            em.flush();
            return room.getRoomId();
        } else {
            throw new RoomTypeNotFoundException();
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
    public void updateRoom(Room room) throws RoomNotFoundException {
        //add bean validators
        em.merge(room);
    }
    
    public void updateRoomStatus(long roomId) throws RoomNotFoundException {
        Room room = getRoomById(roomId);
        if(room.getRoomStatus() == false) {
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

    /*
    @Override
    public List<RoomType> walkInSearchRoom(Date startDate, Date endDate) throws RoomNotFoundException {

        //The way to make this method alot simpler is to make RoomType - Room bidirectional - NOTE IN CASE THIS DOESNT WORK
        List<Room> rooms = this.retrieveRooms();
        List<RoomType> freeRoomTypes = new ArrayList<>();
        HashSet<String> set = new HashSet<String>();

        try {

            for (Room r : rooms) {
                Boolean thisRoomWillBeFree = true;
                for (Booking b : r.getBookings()) {
                    if (startDate.before(b.getCheckOutDate()) && endDate.after(b.getCheckInDate())) { //THIS MEANS THAT THERES CLASH
                        thisRoomWillBeFree = false;
                        break;

                    }
                }
                if (thisRoomWillBeFree) {
                    set.add(r.getRoomType().getRoomName()); //these are the room types that are free
                }
            }

            for (String s : set) {
                //Making a copy of the Room Type but with an UPDATED inventory (total inventory - dates its booked)
                //set current inventory to current inventory
                freeRoomTypes.add(new RoomType(s, roomTypeSessionBeanLocal.getRoomTypeByName(s).getRoomInventory()));

            }

            for (Room r : rooms) {
                for (Booking b : r.getBookings()) {
                    if (startDate.before(b.getCheckOutDate()) && endDate.after(b.getCheckInDate())) { //THIS MEANS THAT THERES CLASH
                        for (RoomType rt : freeRoomTypes) { //if that room is of the room type, decremenet its inventory by 1
                            if (rt.getRoomName().equals(r.getRoomType().getRoomName())) {
                                rt.setRoomInventory(rt.getRoomInventory() - 1);
                            }
                        }
                        break;
                    }
                }
            }

            //CHECKING ALL PRE BOOKINGS FOR NUMBER OF ROOMS BOOKED ON THAT DATE AND MINUSING IT FROM INVENTORY OF THE FAKEROOMTYPE RETURNED
            for (RoomType rt : freeRoomTypes) {

                Query query = em.createQuery("SELECT b FROM Booking b WHERE b.preBooking = :inPreBooking AND b.roomType.roomTypeId = :inRoomTypeId");
                query.setParameter("inPreBooking", Boolean.TRUE);
                query.setParameter("inRoomTypeId", rt.getRoomTypeId());
                List<Booking> bookings = query.getResultList(); //list of bookings of the same room type

                for (Booking b : bookings) {
                    if (startDate.before(b.getCheckOutDate()) && endDate.after(b.getCheckInDate())) { //THIS MEANS THAT THERES CLASH
                        rt.setRoomInventory(rt.getRoomInventory() - b.getNumberOfRooms());
                        break;
                    }
                }
            }
            return freeRoomTypes;
        } catch (RoomTypeNotFoundException ex) {
            throw new RoomNotFoundException();
        }
    }
    
     */
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
                System.out.println("Booking id: "+ b.getBookingId());
                // If the search date interval clashes with the booking in question
                if (startDate.before(b.getCheckOutDate()) && endDate.after(b.getCheckInDate())) {
                    // Updating the number of free rooms for that specific Room Type
                    System.out.println("Booking id: "+ b.getBookingId() + " Booking number of rooms: " + b.getNumberOfRooms());
                    Long roomTypeToUpdate = b.getRoomType().getRoomTypeId();
                    map.replace(roomTypeToUpdate, map.get(roomTypeToUpdate) - b.getNumberOfRooms());
                }
            }
        } catch (BookingNotFoundException ex) {
            return map;
        }
        catch (RoomTypeNotFoundException ex) {
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
        List<Room> rooms = this.retrieveRoomsByRoomType(booking.getRoomType().getRoomTypeId());
        Date startDate = booking.getCheckInDate();
        Date endDate = booking.getCheckOutDate();
        Boolean thisRoomWillBeFree = true;

        booking.setPreBooking(false);

        try {
            for (Room r : rooms) {
                thisRoomWillBeFree = true;
                for (Booking b : r.getBookings()) {
                    if (startDate.compareTo(b.getCheckOutDate()) < 0) { //if startDate is before a room's booking's checkout date (coz if its after we good to go)
                        if (endDate.compareTo(b.getCheckInDate()) > 0) { //if endDate stop at a room's booking's checkin date
                            thisRoomWillBeFree = false; //THIS MEANS THAT THERES CLASH
                        }
                    }
                }
                if (thisRoomWillBeFree) {
                    r.addBookings(booking);
                    booking.addRoom(r);
                    booking.setNumberOfRooms(booking.getNumberOfRooms() - 1);
                    break;
                }
            } //thisRoomWillBeFree will end up TRUE if a booking was made, and FALSE if no available rooms exist

            if (!thisRoomWillBeFree) { //no available rooms
                String nextHigherRoomTypeString = booking.getRoomType().getNextHigherRoomType();

                if (!nextHigherRoomTypeString.equals("None")) {

                    if (booking.getBookingExceptionType() == BookingExceptionType.TYPE1) {
                        booking.setBookingExceptionType(BookingExceptionType.TYPE2);
                        return;
                    }

                    RoomType nextHigherType = roomTypeSessionBeanLocal.getRoomTypeByName(nextHigherRoomTypeString);
                    booking.setRoomType(nextHigherType);
                    booking.setBookingExceptionType(BookingExceptionType.TYPE1);
                    this.findARoomAndAddToIt(bookingId);

                } else { //no available rooms and no available higher room types
                    booking.setBookingExceptionType(BookingExceptionType.TYPE2);
                }
            }

            if (booking.getNumberOfRooms() != 0 && booking.getBookingExceptionType() == BookingExceptionType.NONE) {
                this.findARoomAndAddToIt(bookingId);
            }

        } catch (EntityInstanceExistsInCollectionException | RoomTypeNotFoundException ex) {
            throw new RoomNotFoundException();
        }
    }
}
