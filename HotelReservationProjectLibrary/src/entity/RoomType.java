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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.exceptions.EntityInstanceExistsInCollectionException;
import util.exceptions.EntityInstanceMissingInCollectionException;

/**
 *
 * @author ryo20
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    @Column(nullable = false, length = 16, unique = true)
    @NotNull
    @Size(min = 1, max = 16)
    private String roomName;
    @Column(nullable = false, unique = true)
    @NotNull
    private Integer ranking;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<RoomRate> listOfRoomRates;

    public RoomType() {
        listOfRoomRates = new ArrayList<RoomRate>();
    }

    public RoomType(String roomName) {
        this.roomName = roomName;
    }

    
    public void addToListOfRoomRate(RoomRate roomRate) throws EntityInstanceExistsInCollectionException {
        if (!listOfRoomRates.contains(roomRate)) {
            listOfRoomRates.add(roomRate);
        } else {
            throw new EntityInstanceExistsInCollectionException();
        }
    }
    
    public void deleteFromListOfRoomRate(RoomRate roomRate) throws EntityInstanceMissingInCollectionException {
        if (listOfRoomRates.contains(roomRate)) {
            listOfRoomRates.remove(roomRate);
        } else {
            throw new EntityInstanceMissingInCollectionException();
        }
    }

    
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<RoomRate> getListOfRoomRates() {
        return listOfRoomRates;
    }

    public void setListOfRoomRates(List<RoomRate> listOfRoomRates) {
        this.listOfRoomRates = listOfRoomRates;
    }


    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }

    /**
     * @return the ranking
     */
    public Integer getRanking() {
        return ranking;
    }

    /**
     * @param ranking the ranking to set
     */
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    
}
