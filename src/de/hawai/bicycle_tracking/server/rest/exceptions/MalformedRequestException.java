package de.hawai.bicycle_tracking.server.rest.exceptions;

public class MalformedRequestException extends RuntimeException
{
	public MalformedRequestException(final String message)
	{
		super(message);
	}
}
