package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

	@ManyToOne(targetEntity = User.class) 
	private IUser user;

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
	public IUser getUser()
	{
		return user;
	}

	public void setUser(final IUser inUser)
	{
		user = inUser;
	}
}
