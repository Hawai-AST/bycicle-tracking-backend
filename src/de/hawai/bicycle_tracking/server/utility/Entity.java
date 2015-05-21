package de.hawai.bicycle_tracking.server.utility;

import java.util.UUID;

public interface Entity {

	public UUID getId();
	public void setId(UUID id);
	public Integer getVersion();
	public void setVersion(Integer version);

}
