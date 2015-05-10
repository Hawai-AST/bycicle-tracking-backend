package de.hawai.bicycle_tracking.server.rest;

import java.text.ParseException;

import de.hawai.bicycle_tracking.server.rest.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {
	@ExceptionHandler(NotAuthorizedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorMessage onNotAuthorized(NotAuthorizedException inNotAuthorized) {
		return new ErrorMessage(401, "Not Authorized", inNotAuthorized.getMessage());
	}

	@ExceptionHandler(InternalError.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage onInternalError(InternalError inInternalError) {
		return new ErrorMessage(500, "Server Error", inInternalError.getMessage());
	}

	@ExceptionHandler(InvalidClientException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage onInvalidClient(InvalidClientException inInvalidClient) {
		return new ErrorMessage(400, "Client ID Error", inInvalidClient.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage onNotFound(NotFoundException inNotFound) {
		return new ErrorMessage(404, "Resource not Found", inNotFound.getMessage());
	}

	@ExceptionHandler(AlreadyExistsException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorMessage onAlreadyExists(AlreadyExistsException inAlreadyExists) {
		return new ErrorMessage(409, "Already exists", inAlreadyExists.getMessage());
	}

	@ExceptionHandler(MalformedRequestException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage onMalformedRequestException(MalformedRequestException inParseException) {
		return new ErrorMessage(400, "Invalid Input", inParseException.getMessage());
	}

	@ExceptionHandler(ParseException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage onParseException(ParseException inParseException) {
		return new ErrorMessage(400, "Invalid Input", inParseException.getMessage());
	}

	@ExceptionHandler(InvalidAccessException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorMessage onParseException(InvalidAccessException inInvalidAccessException) {
		return new ErrorMessage(403, "Forbidden", inInvalidAccessException.getMessage());
	}
}
