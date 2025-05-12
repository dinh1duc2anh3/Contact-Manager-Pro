package koolsoft.server;

import java.io.Serializable;
import java.util.Objects;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ContactInfo implements  Serializable, IsSerializable {

	@Id
	private Long id;
	private static final long serialVersionUID = 1L;
	//	private final int id;
	private String firstName = "";
	private String lastName = "";
	private String phoneNumber = "";
	private String address = "";
	
	
	//create new contact info 
	public ContactInfo() {
		}
	
	//create new contact info 
	public ContactInfo(String firstName, String lastName, String phoneNumber, String address
			) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
//		this.id = nextId;
//		nextId++;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ContactInfo that = (ContactInfo) obj;
		return firstName.equals(that.firstName) &&
			   lastName.equals(that.lastName) &&
			   phoneNumber.equals(that.phoneNumber) &&
			   address.equals(that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, phoneNumber, address);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
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
