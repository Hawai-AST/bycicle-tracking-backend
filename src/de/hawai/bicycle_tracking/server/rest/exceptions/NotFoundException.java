package de.hawai.bicycle_tracking.server.rest.exceptions;

public class NotFoundException extends RuntimeException {
	public NotFoundException(final String message) {
		super(message);
	}
}
