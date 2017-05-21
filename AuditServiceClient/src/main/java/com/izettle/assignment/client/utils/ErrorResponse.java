package com.izettle.assignment.client.utils;

public class ErrorResponse {
	private String error;
    private String error_description;

    public ErrorResponse() {
    }

    public ErrorResponse(final ClientError serverError, final String error_description) {
        this.error = serverError.name();
        this.error_description = error_description;
    }

    public ClientError getError() {
        try {
            return ClientError.valueOf(error);
        } catch (RuntimeException ex) {
            return ClientError.unknown_error;
        }
    }

    public String getError_description() {
        return error_description;
    }

}
