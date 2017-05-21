package com.izettle.assignment.client;

import com.izettle.assignment.client.ddo.AuditsDisplayEntity;
import com.izettle.assignment.client.ddo.BearerTokenDisplayEntity;
import com.izettle.assignment.client.ddo.UserDisplayEntity;

public interface AuditServiceHttpClient {

	AuditsDisplayEntity getAudits(final String destinationUrl, final String bearerToken, final Boolean isSuccess, final String logTraceId);

    BearerTokenDisplayEntity signIn(final String username,
            final String password, final String destinationUrl);

    void registerUser(final UserDisplayEntity userDisplayEntity, final String destinationUrl);
	
}
