package com.izettle.assignment.client.utils;

import com.google.gson.Gson;

public class ClientExceptions extends RuntimeException {

    protected static final Gson gson = new Gson();
    private static final long serialVersionUID = -1778556706865357928L;

    public ClientExceptions(final ErrorResponse errResp) {
        super(gson.toJson(errResp));
    }

    public ClientExceptions(final ErrorResponse errResp, final Exception e) {
        super(gson.toJson(errResp), e);
    }

}
