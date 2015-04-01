package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.EMail;

@Entity
@Table(name="user")
public class User extends AbstractEntity implements IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750089196426609433L;
	private String name;
	private String firstName;
	private Date birthdate;
	//	private AdressType address;
	private EMail eMailAddress;

	public User(String name, String firstName, EMail eMailAddress, Date birthdate) {
		this.name = name;
		this.firstName = firstName;
		this.birthdate = birthdate;
		//		this.address = address;
		this.eMailAddress = eMailAddress;
	}

	@Column(name="name", length=30, nullable=false)
	@Override
	public String getName() {
		return name;
	}

	@Column(name="firstname", length=30, nullable=false)
	@Override
	public String getFirstName() {
		return firstName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="birthdate", nullable=false)
	@Override
	public Date getBirthdate() {
		// TODO Auto-generated method stub
		return birthdate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	//	@Override
	//	public AdressType getAddress() {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

	@Column(name="email", nullable=false)
	@Type(type="de.hawai.bicycle_tracking.server.utility.types.EMailType")
	@Override
	public EMail geteMailAddress() {
		return eMailAddress;
	}

	public void seteMailAddress(EMail eMailAddress) {
		this.eMailAddress = eMailAddress;
	}

}
