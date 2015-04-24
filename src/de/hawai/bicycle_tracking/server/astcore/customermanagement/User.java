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
	private EMail mailAddress;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private GrantedAuthority authority;

	public User(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password, GrantedAuthority authority) {
		this.name = name;
		this.firstName = firstName;
		this.birthdate = birthdate;
		this.address = address;
		this.mailAddress = eMailAddress;
		this.password = password;
		this.authority = authority;
	}

	private User() {
		super();
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
	public EMail getMailAddress() {
		return mailAddress;
	}

	@Target(HawaiAuthority.class)
	@Column(name = "user_type", nullable = false)
	public GrantedAuthority getAuthority() {
		return this.authority;
	}

	public void setAuthority(GrantedAuthority inAuthority) {
		this.authority = inAuthority;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	private void setMailAddress(EMail eMailAddress) {
		this.mailAddress = eMailAddress;
	}

	private void setAddress(Address address) {
		this.address = address;
	}

	@Column(name = "password", length = 100, nullable = false)
	public String getPassword()
	{
		return password;
	}

	private void setPassword(final String inPassword)
	{
		password = inPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mailAddress == null) ? 0 : mailAddress.hashCode());
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
		if (mailAddress == null) {
			if (other.mailAddress != null)
				return false;
		} else if (!mailAddress.equals(other.mailAddress))
			return false;
		return true;
	}

}
