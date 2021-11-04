/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.EntityInstanceMissingInCollectionException;

/**
 *
 * @author ryo20
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @Column(nullable = false, length = 4, unique = true)
    @NotNull
    @Size(min = 4, max = 4)
    private String roomNumber;
    
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private RoomType roomType;

    @OneToMany()
    private List<Booking> preBookings;
    
    @ManyToMany()
    private List<Booking> bookings;

    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }
    
    public Room() {
    }

    public void addPreBookings(Booking booking) throws EntityInstanceExistsInCollectionException
    {
        if(!this.preBookings.contains(booking))
        {
            this.getPreBookings().add(booking);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("PreBooking already exist");
        }
    }
    
    public void removePreBookings(Booking booking) throws EntityInstanceMissingInCollectionException
    {
        if(this.getPreBookings().contains(booking))
        {
            this.getPreBookings().remove(booking);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("PreBooking does not exist");
        }
    }
    
    public void addBookings(Booking booking) throws EntityInstanceExistsInCollectionException
    {
        if(!this.bookings.contains(booking))
        {
            this.getBookings().add(booking);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Booking already exist");
        }
    }
    
    public void removeBookings(Booking booking) throws EntityInstanceMissingInCollectionException
    {
        if(this.getBookings().contains(booking))
        {
            this.getBookings().remove(booking);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Booking does not exist");
        }
    }
    
    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }    

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long rooomId) {
        this.roomId = rooomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rooomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }

    /**
     * @return the preBookings
     */
    public List<Booking> getPreBookings() {
        return preBookings;
    }

    /**
     * @param preBookings the preBookings to set
     */
    public void setPreBookings(List<Booking> preBookings) {
        this.preBookings = preBookings;
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * @param bookings the bookings to set
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    
}
