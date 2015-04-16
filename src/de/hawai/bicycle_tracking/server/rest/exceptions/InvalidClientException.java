package de.hawai.bicycle_tracking.server.rest.exceptions;

public class InvalidClientException extends RuntimeException {
	public InvalidClientException(final String message) {
		super(message);
	}
}
