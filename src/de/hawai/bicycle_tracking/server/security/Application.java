package de.hawai.bicycle_tracking.server.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Application implements IApplication {
	@Id
	@GeneratedValue
	private int id;

	@Column(name = "client_id")
	private String clientID;

	@Column(name = "client_secret")
	private String secret;

	@ManyToMany(targetEntity = HawaiAuthority.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(foreignKey = @ForeignKey(name = "authority_name"))
	private Collection<GrantedAuthority> authorities;

	private String scopes;

	public Application(String clientID, String secret, Collection<GrantedAuthority> authorities, String scopes) {
		this.clientID = clientID;
		this.secret = secret;
		this.authorities = authorities;
		this.scopes = scopes;
	}

	protected Application() {
	}

	public int getId() {
		return id;
	}

	@Override
	public String getClientID() {
		return clientID;
	}

	@Override
	public String getSecret() {
		return this.secret;
	}

	@Override
	public Set<String> getScopes() {
		return new HashSet<>(Arrays.asList(this.scopes.split(";")));
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	private void setId(final int inId) {
		id = inId;
	}

	private void setClientID(final String inClientID) {
		clientID = inClientID;
	}

	private void setSecret(String secret) {
		this.secret = secret;
	}

	private void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	private void setScopes(String scopes) {
		this.scopes = scopes;
	}
}
