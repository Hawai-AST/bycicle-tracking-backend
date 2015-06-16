package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import de.hawai.bicycle_tracking.server.utility.value.Gender;
import org.hibernate.annotations.Target;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite.UserDeserializerSuite;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email_address" }) })
@SuppressWarnings("unused")
@JsonDeserialize(using = UserDeserializerSuite.class)
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
	private String password;
	private Gender gender;
	@JsonIgnore
	private GrantedAuthority authority;

	public User(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password, Gender gender,
				GrantedAuthority authority) {
		this.name = name;
		this.firstName = firstName;
		this.birthdate = birthdate;
		this.address = address;
		this.mailAddress = eMailAddress;
		this.password = password;
		this.authority = authority;
		this.gender = gender;
	}
	
	public User(String name, String firstName, EMail eMailAddress, Address address,
			Date birthdate, String password, Gender gender, GrantedAuthority authority, UUID id) {
		this(name, firstName, eMailAddress, address, birthdate, password, gender, authority);
		this.setId(id);
	}

	private User() {
		super();
	}

	@Column(name = "name", length = 30, nullable = false)
	@Override
	public String getName() {
		return name;
	}

	@Column(name = "firstname", length = 30, nullable = false)
	@Override
	public String getFirstName() {
		return firstName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birthdate", nullable = true)
	@Override
	public Date getBirthdate() {
		return birthdate;
	}

	@Embedded
	@Override
	public Address getAddress() {
		return address;
	}

	@Embedded
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

	protected void setName(String name) {
		this.name = name;
	}

	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	protected void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	protected void setMailAddress(EMail eMailAddress) {
		this.mailAddress = eMailAddress;
	}

	protected void setAddress(Address address) {
		this.address = address;
	}

	@Override
	@Column(name = "password", length = 100, nullable = false)
	public String getPassword() {
		return password;
	}

	protected void setPassword(final String inPassword) {
		password = inPassword;
	}

	@Override
	@Column(name = "gender", nullable = true)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mailAddress == null) ? 0 : mailAddress.hashCode());
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

	@Override
	public String toString() {
		return "User [name=" + name + ", firstName=" + firstName + ", birthdate=" + birthdate + ", address=" + address +
				", mailAddress=" + mailAddress + ", password=" + password + ", gender=" + gender + ", id=" + getId() +
				", authority=" + authority + "]";
	}
	
	
}
