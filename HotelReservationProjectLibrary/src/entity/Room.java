/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author ryo20
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rooomId;
    private String roomNumber;
    private Boolean status;

    public Room() {
    }

    public Room(String roomNumber, Boolean status) {
        this.roomNumber = roomNumber;
        this.status = status;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    

    public Long getRooomId() {
        return rooomId;
    }

    public void setRooomId(Long rooomId) {
        this.rooomId = rooomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rooomId != null ? rooomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rooomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.rooomId == null && other.rooomId != null) || (this.rooomId != null && !this.rooomId.equals(other.rooomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + rooomId + " ]";
    }
    
}
