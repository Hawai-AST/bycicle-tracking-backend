package de.hawai.bicycle_tracking.server.rest.exceptions;

public class NotAuthorizedException extends RuntimeException {
	public NotAuthorizedException(final String message) {
		super(message);
	}
}
