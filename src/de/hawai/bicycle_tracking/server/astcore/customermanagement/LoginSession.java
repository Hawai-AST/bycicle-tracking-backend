package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "token"))
public class LoginSession implements ILoginSession
{
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private Application application;

	private String token;

	@ManyToOne
	private User user;

	@Override
	public Application getApplication()
	{
		return application;
	}

	public void setApplication(final Application inApplication)
	{
		application = inApplication;
	}

	@Override
	public String getToken()
	{
		return token;
	}

	public void setToken(final String inToken)
	{
		token = inToken;
	}

	@Override
	public User getUser()
	{
		return user;
	}

	public void setUser(final User inUser)
	{
		user = inUser;
	}
}
