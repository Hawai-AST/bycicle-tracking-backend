package de.hawai.bicycle_tracking.server.astcore.customermanagement;

public interface ILoginSession {
	IApplication getApplication();

	IUser getUser();

	String getToken();
}
