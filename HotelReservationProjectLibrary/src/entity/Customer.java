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
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CustomerId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    //pattern for email
    private String email;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String userName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String password;
    @Column(nullable = false, length = 8, unique = true)
    @NotNull
    @Size(min = 8, max = 8)
    private String phoneNumber;
    @Column(nullable = false, length = 8, unique = true)
    @NotNull
    @Size(min = 8, max = 8)
    private String passportNumber;
    
    //One to many relationship with booking
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public Customer() {
        bookings = new ArrayList<>();
    }

    public Customer(String email, String userName, String password, String phoneNumber, String passportNumber) {
        this();
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }

    
    
    public Long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Long CustomerId) {
        this.CustomerId = CustomerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (CustomerId != null ? CustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the CustomerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.CustomerId == null && other.CustomerId != null) || (this.CustomerId != null && !this.CustomerId.equals(other.CustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + CustomerId + " ]";
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    
    public void addBooking(Booking booking) throws EntityInstanceExistsInCollectionException
    {
        if(!this.bookings.contains(booking))
        {
            this.bookings.add(booking);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Booking already exist");
        }
    }
    
    public void removeBooking(Booking booking) throws EntityInstanceMissingInCollectionException
    {
        if(this.bookings.contains(booking))
        {
            this.bookings.remove(booking);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Booking does not exist");
        }
    }
    
}
