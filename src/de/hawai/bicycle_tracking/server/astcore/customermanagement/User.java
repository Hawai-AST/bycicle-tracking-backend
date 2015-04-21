package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Entity
@Table(name="user", uniqueConstraints = { @UniqueConstraint(columnNames = {"email_address"})})
public class User extends AbstractEntity implements IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750089196426609433L;
	private String name;
	private String firstName;
	private Date birthdate;
	private Address address;
	private EMail eMailAddress;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private List<LoginSession> loginSessions = new ArrayList<LoginSession>();

	public User(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password) {
		this.name = name;
		this.firstName = firstName;
		this.birthdate = birthdate;
		this.address = address;
		this.eMailAddress = eMailAddress;
		this.password = password;
	}

	public User(){
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
		return birthdate;
	}

	@Embedded
	@Override
	public Address getAddress() {
		return address;
	}

	@Embedded()
	@Override
	public EMail geteMailAddress() {
		return eMailAddress;
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

	public void seteMailAddress(EMail eMailAddress) {
		this.eMailAddress = eMailAddress;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(name = "password", length = 100, nullable = false)
	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String inPassword)
	{
		password = inPassword;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	public List<LoginSession> getLoginSessions()
	{
		return loginSessions;
	}

	public void setLoginSessions(final List<LoginSession> inLoginSessions)
	{
		loginSessions = inLoginSessions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eMailAddress == null) ? 0 : eMailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (eMailAddress == null) {
			if (other.eMailAddress != null)
				return false;
		} else if (!eMailAddress.equals(other.eMailAddress))
			return false;
		return true;
	}

}
