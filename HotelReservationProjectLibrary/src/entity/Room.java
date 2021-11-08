/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    //@NotNull
    //@Size(min = 4, max = 4)
    private String roomNumber;
    private Boolean roomStatus;
    private Boolean enabled;
    
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
    private RoomType roomType;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    private List<Booking> bookings;
    
    public Room() {
        bookings = new ArrayList<>();
        enabled = true;
        roomStatus = false;
    }
    
    public Room(String roomNumber) {
        this();
        this.roomNumber = roomNumber;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Boolean roomStatus) {
        this.roomStatus = roomStatus;
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
