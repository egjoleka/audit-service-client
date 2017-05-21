package com.izettle.assignment.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.izettle.assignment.client.ddo.AuditsDisplayEntity;
import com.izettle.assignment.client.ddo.BearerTokenDisplayEntity;
import com.izettle.assignment.client.ddo.UserDisplayEntity;
import com.izettle.assignment.client.utils.ClientError;
import com.izettle.assignment.client.utils.ClientExceptions;
import com.izettle.assignment.client.utils.ErrorResponse;

public class AuditServiceHttpClientImplementation implements AuditServiceHttpClient {
	private static Logger logger = LoggerFactory.getLogger(AuditServiceHttpClientImplementation.class);
	private static final String HTTPS = "https";
	private static final String APPLICATION_X_WWW_FORM_URLENCODED_TYPE = "application/x-www-form-urlencoded";
	private static final String APPLICATION_JSON_TYPE = "application/json";

	private static final Gson gson = new Gson();

	private int readTimeoutInMillis;
	private int connectTimeoutInMillis;
	private CloseableHttpClient httpClient;

	public AuditServiceHttpClientImplementation(final String uriScheme) {
		this(uriScheme, 500, 500);
	}

	public AuditServiceHttpClientImplementation(final String uriScheme, final int readTimeoutInMillis,
			final int connectTimeoutInMillis) {
		this.readTimeoutInMillis = readTimeoutInMillis;
		this.connectTimeoutInMillis = connectTimeoutInMillis;
		httpClient = createHttpClient(uriScheme);
	}

	private CloseableHttpClient createHttpClient(final String uriScheme) {
		if (HTTPS.equalsIgnoreCase(uriScheme)) {
			// if implementation is https add implementation here
		}
		return HttpClients.custom().setRetryHandler(new AuditServiceHttpClientRetryHandler()).build();
	}

	/**
	 * Status API
	 * 
	 * @param destinationUrl
	 * @return
	 */
	public String getStatus(final String destinationUrl, final String logTraceId) {
		CloseableHttpResponse httpResponse = null;
		String response = null;
		try {
			httpResponse = sendGetRequest(httpClient, destinationUrl, logTraceId);
			response = toString(httpResponse);
			isValidResponse(httpResponse);
		} catch (ClientExceptions ex) {
			throw ex;
		} catch (Exception e) {
			throw new ClientExceptions(toServerErrorResponse("IO error while calling status API: " + e.getMessage()),
					e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);
		}
		return response;
	}

	/**
	 * GET Audits REQUEST
	 * 
	 * @param destinationUrl
	 * @param bearer
	 * @return
	 */
	public AuditsDisplayEntity getAudits(final String destinationUrl, final String bearerToken, final Boolean isSuccess,
			final String logTraceId) {
		CloseableHttpResponse httpResponse = null;
		String response = null;
		try {
			httpResponse = sendGetRequest(httpClient, destinationUrl, bearerToken, isSuccess);
			response = toString(httpResponse);
			isValidResponse(httpResponse);
		} catch (ClientExceptions ex) {
			throw ex;
		} catch (Exception e) {
			throw new ClientExceptions(
					toServerErrorResponse("IO error while calling  get user roles: " + e.getMessage()), e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);
		}
		return gson.fromJson(response, AuditsDisplayEntity.class);
	}

	/**
	 * Get verification REQUEST
	 * 
	 * @param password
	 * @param username
	 * @param destinationUrl
	 * @return
	 */
	public BearerTokenDisplayEntity signIn(final String username, final String password, final String destinationUrl) {
		CloseableHttpResponse httpResponse = null;
		String response = null;
		try {
			httpResponse = sendPostRequest(httpClient, destinationUrl, createLoginRequestEntity(username, password));
			response = toString(httpResponse);
			isValidResponse(httpResponse);
		} catch (ClientExceptions ex) {
			throw ex;
		} catch (Exception e) {
			throw new ClientExceptions(
					toServerErrorResponse("IO error while trying login request request: " + e.getMessage()), e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);
		}
		return gson.fromJson(response, BearerTokenDisplayEntity.class);
	}
	
	/**
	 * @param UserDisplayEntity
	 * @param destinationUrl
	 */

	public void registerUser(final UserDisplayEntity userDisplayEntity, final String destinationUrl) {
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = sendPostRequestForJson(httpClient, userDisplayEntity, destinationUrl);
		} catch (ClientExceptions ex) {
			throw ex;
		} catch (Exception e) {
			throw new ClientExceptions(
					toServerErrorResponse("IO error while trying login request request: " + e.getMessage()), e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);
		}
	}

	private static UrlEncodedFormEntity createLoginRequestEntity(final String userName, final String password)
			throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user", userName));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		return new UrlEncodedFormEntity(nameValuePairs);
	}

	private CloseableHttpResponse sendPostRequest(final CloseableHttpClient httpClient, final String destinationUrl,
			final UrlEncodedFormEntity entity) throws IOException {
		HttpPost httpPost = new HttpPost(destinationUrl);
		configureHttpRequest(httpPost, APPLICATION_X_WWW_FORM_URLENCODED_TYPE);
		httpPost.setEntity(entity);
		return executePostRequest(httpPost);
	}

	private CloseableHttpResponse sendPostRequestForJson(final CloseableHttpClient httpClient,
			final UserDisplayEntity entity, final String destinationUrl) throws IOException {
		HttpPost httpPost = new HttpPost(destinationUrl);
		configureHttpRequest(httpPost, APPLICATION_JSON_TYPE);
		StringEntity jsonEntity = new StringEntity(gson.toJson(entity));
		httpPost.setEntity(jsonEntity);
		return executePostRequest(httpPost);
	}

	CloseableHttpResponse executePostRequest(final HttpPost httpPost) throws IOException {
		return httpClient.execute(httpPost);
	}

	private void configureHttpRequest(final HttpRequestBase httpRequest, final String contentType) {
		httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		httpRequest.addHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_TYPE);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutInMillis)
				.setConnectTimeout(connectTimeoutInMillis).build();
		httpRequest.setConfig(requestConfig);
	}

	private CloseableHttpResponse sendGetRequest(final CloseableHttpClient httpClient, final String destinationUrl,
			final String logTraceId) throws IOException {
		HttpGet httpGet = new HttpGet(destinationUrl);
		configureHttpRequest(httpGet, APPLICATION_JSON_TYPE);
		return executeGetRequest(httpGet);
	}

	CloseableHttpResponse executeGetRequest(final HttpGet httpGet) throws IOException {
		return httpClient.execute(httpGet);
	}

	private CloseableHttpResponse sendGetRequest(final CloseableHttpClient httpClient, final String destinationUrl,
			final String bearerToken, final Boolean isSuccess) throws IOException, URISyntaxException {
		URI uri = new URIBuilder().setPath(destinationUrl).setParameter("isSuccess", isSuccess.toString())
				.setParameter("bearer", bearerToken).build();
		HttpGet httpGet = new HttpGet(uri);
		configureHttpRequest(httpGet, APPLICATION_JSON_TYPE);
		return executeGetRequest(httpGet);
	}

	private String toString(final CloseableHttpResponse httpResponse) throws IOException {
		StringBuilder httpStrResponse = new StringBuilder();
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			InputStream inputStream = entity.getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				httpStrResponse.append(line);
			}
			bufferedReader.close();
		}
		return httpStrResponse.toString();
	}

	private ErrorResponse toServerErrorResponse(final String value) {
		return new ErrorResponse(ClientError.server_error, value);
	}

	private boolean isValidResponse(final CloseableHttpResponse httpResponse) {
		if (httpResponse == null) {
			throw new ClientExceptions(toServerErrorResponse("Invalid HttpResponse"));
		}
		final int statusCode = httpResponse.getStatusLine().getStatusCode();

		return statusCode >= 200 && statusCode <= 201;
	}

}
