package de.hawai.bicycle_tracking.server.rest.exceptions;

public class AlreadyExistsException extends RuntimeException
{
	public AlreadyExistsException(final String message)
	{
		super(message);
	}
}