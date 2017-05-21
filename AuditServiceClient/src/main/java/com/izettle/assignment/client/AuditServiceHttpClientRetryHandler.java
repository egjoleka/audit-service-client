package com.izettle.assignment.client;

import java.io.IOException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditServiceHttpClientRetryHandler implements HttpRequestRetryHandler{
	  private static Logger logger = LoggerFactory.getLogger(AuditServiceHttpClientRetryHandler.class);

	    @Override
	    public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
	        logger.info("Received {} from server, retrying {} time", exception.getMessage(), executionCount);
	        if (executionCount > 3) {
	            logger.warn("Maximum tries reached for client http pool ");
	            return false;
	        }
	        if (exception instanceof org.apache.http.NoHttpResponseException) {
	            logger.warn("No response from server on " + executionCount + " call");
	            return true;
	        }
	        return false;
	    }
	}

