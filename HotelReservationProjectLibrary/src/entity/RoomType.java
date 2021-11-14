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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String roomName;
    @Column(nullable = false)
    @NotNull
    private String NextHigherRoomType;
    @NotNull
    private Boolean enabled;
    @Size(min = 1, max = 240)
    private String description;
    @Size(min = 1, max = 240)
    private String roomSize;
    private Integer beds;
    private Integer capacity;
    private List<String> amenities;
    @PositiveOrZero
    private Integer roomInventory;
    
    @OneToMany(cascade = {}, fetch = FetchType.LAZY)
    private List<RoomRate> listOfRoomRates;

    public RoomType() {
        this.listOfRoomRates = new ArrayList<>();
        this.amenities = new ArrayList<>();
        this.enabled = true;
        this.roomInventory = 0;
    }
    
    public RoomType(String roomName, Integer roomInventory) {
        this.listOfRoomRates = new ArrayList<>();
        this.amenities = new ArrayList<>();
        this.enabled = true;
        this.roomInventory = roomInventory;
        this.roomName = roomName;
    }

    public RoomType(String roomName, String description, String roomSize, Integer beds, Integer capacity) {
        this();
        this.roomName = roomName;
        this.description = description;
        this.roomSize = roomSize;
        this.beds = beds;
        this.capacity = capacity;
    }

    public Integer getRoomInventory() {
        return roomInventory;
    }

    public void setRoomInventory(Integer roomInventory) {
        this.roomInventory = roomInventory;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the roomSize
     */
    public String getRoomSize() {
        return roomSize;
    }

    /**
     * @param roomSize the roomSize to set
     */
    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    /**
     * @return the beds
     */
    public Integer getBeds() {
        return beds;
    }

    /**
     * @param beds the beds to set
     */
    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    /**
     * @return the capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public List<String> getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the NextHigherRoomType
     */
    public String getNextHigherRoomType() {
        return NextHigherRoomType;
    }

    /**
     * @param NextHigherRoomType the NextHigherRoomType to set
     */
    public void setNextHigherRoomType(String NextHigherRoomType) {
        this.NextHigherRoomType = NextHigherRoomType;
    }

    /**
     * @param roomName the roomName to set
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
