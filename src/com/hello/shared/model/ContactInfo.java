package com.hello.shared.model;

import java.io.Serializable;
import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.hello.shared.enums.Address;
import com.hello.shared.formatter.ContactInfoFormatter;
import com.hello.shared.enums.Gender;

@Entity
public class ContactInfo implements Serializable, IsSerializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Index
	private String firstName;
	
    private String lastName;
    @Index
	private String fullName;
    
    private Gender gender;
    @Id
    private String phoneNumber;

    private Address address;
    public ContactInfo() {} // GWT RPC cần constructor rỗng

	//create new contact info 
	public ContactInfo(
			String firstName, 
			String lastName, 
			Gender gender,
			String phoneNumber, 
			Address address) {
		this.firstName = ContactInfoFormatter.formatName(firstName);
		this.lastName = ContactInfoFormatter.formatName(lastName);
		this.fullName = ContactInfoFormatter.formatName(firstName + " " + lastName) ;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = ContactInfoFormatter.formatPhoneNumber(phoneNumber) ;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ContactInfo that = (ContactInfo) obj;
		return firstName.equals(that.firstName) &&
			   lastName.equals(that.lastName) &&
			   gender.equals(that.gender) &&
			   phoneNumber.equals(that.phoneNumber) &&
			   address.equals(that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName,gender, phoneNumber, address);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}

	public Gender getGender() {
		return gender;
	}
	
	public String getGenderStr() {
		return gender.toString();
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Address getAddress() {
		return address;
	}
	
	public String getAddressStr() {
		return address.toString();
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
