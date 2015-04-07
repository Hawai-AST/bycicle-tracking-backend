package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.List;
import javax.persistence.*;

@Entity
public class Application implements IApplication
{
	@Id
	@GeneratedValue
	private int id;

	@Column(name = "clientid")
	private String clientID;

	@OneToMany(mappedBy = "application")
	private List<LoginSession> sessions;

	public int getId()
	{
		return id;
	}

	public void setId(final int inId)
	{
		id = inId;
	}

	@Override
	public String getClientID()
	{
		return clientID;
	}

	public void setClientID(final String inClientID)
	{
		clientID = inClientID;
	}
}
