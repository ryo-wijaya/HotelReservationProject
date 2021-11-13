/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.BookingExceptionType;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.EntityInstanceMissingInCollectionException;

/**
 *
 * @author Jorda
 */
@Entity
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date checkInDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date checkOutDate;
    @Column(nullable = false)
    private Boolean preBooking;
    @Column(nullable = false)
    private Integer numberOfRooms;
    @Column(nullable = false)
    private Integer numberOfUnallocatedRooms;
    @Column(nullable = false)
    private BookingExceptionType bookingExceptionType;
    @Column(nullable = false)
    private Integer numOfTypeOnes;
    @Column(nullable = false)
    private Integer numOfTypeTwos;
    
    // many to one relationship with customer
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private Partner partner;
    
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    // Many to many relationship with rooms
    @ManyToMany(mappedBy = "bookings", fetch = FetchType.LAZY)
    private List<Room> rooms;

    public Booking() {
        this.rooms = new ArrayList<>();
        preBooking = true;
        bookingExceptionType = BookingExceptionType.NONE;
        this.checkInDate = null;
        this.checkOutDate = null;
        this.numOfTypeOnes = 0;
        this.numOfTypeTwos = 0;
    }

    public Booking(Integer numberOfRooms, Date checkInDate, Date checkOutDate) {
        this();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
        this.numberOfUnallocatedRooms = numberOfRooms;
    }

    public BookingExceptionType getBookingExceptionType() {
        return bookingExceptionType;
    }

    public Integer getNumberOfUnallocatedRooms() {
        return numberOfUnallocatedRooms;
    }

    public void setNumberOfUnallocatedRooms(Integer numberOfUnallocatedRooms) {
        this.numberOfUnallocatedRooms = numberOfUnallocatedRooms;
    }

    public Integer getNumOfTypeOnes() {
        return numOfTypeOnes;
    }

    public void setNumOfTypeOnes(Integer numOfTypeOnes) {
        this.numOfTypeOnes = numOfTypeOnes;
    }

    public Integer getNumOfTypeTwos() {
        return numOfTypeTwos;
    }

    public void setNumOfTypeTwos(Integer numOfTypeTwos) {
        this.numOfTypeTwos = numOfTypeTwos;
    }

    public void setBookingExceptionType(BookingExceptionType bookingExceptionType) {
        this.bookingExceptionType = bookingExceptionType;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingId fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Booking[ id=" + bookingId + " ]";
    }

    /**
     * @return the checkInDate
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
    public void addRoom(Room room) throws EntityInstanceExistsInCollectionException
    {
        if(!this.rooms.contains(room))
        {
            this.getRooms().add(room);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Booking already exist");
        }
    }
    
    public void removeRoom(Room room) throws EntityInstanceMissingInCollectionException
    {
        if(this.getRooms().contains(room))
        {
            this.getRooms().remove(room);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Booking does not exist");
        }
    }

    /**
     * @return the preBooking
     */
    public Boolean getPreBooking() {
        return preBooking;
    }

    /**
     * @param preBooking the preBooking to set
     */
    public void setPreBooking(Boolean preBooking) {
        this.preBooking = preBooking;
    }
    
}
