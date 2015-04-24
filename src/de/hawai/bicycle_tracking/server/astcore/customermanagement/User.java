package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.hibernate.annotations.Target;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;

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
	private GrantedAuthority authority;

	public User(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password, GrantedAuthority authority) {
		this.name = name;
		this.firstName = firstName;
		this.birthdate = birthdate;
		this.address = address;
		this.eMailAddress = eMailAddress;
		this.password = password;
		this.authority = authority;
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

	@Target(HawaiAuthority.class)
	@Column(name = "user_type", nullable = false)
	public GrantedAuthority getAuthority() {
		return this.authority;
	}

	public void setAuthority(GrantedAuthority inAuthority) {
		this.authority = inAuthority;
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
