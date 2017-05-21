package com.izettle.assignment.client.ddo;

import java.sql.Timestamp;
import java.util.UUID;

public class LoginAudit {

    private String userName;
    private Boolean isSucess;
    private UUID reqTimeUuid;
    private Timestamp requestTimestamp;
    private String status;
    private String reason;
    private String clientIpAddress;
    private String clientBrowserInfo;
    private String clientOperatingSystem;
    private String clientDeviceType;
    private String bearer;

    public LoginAudit() {
        // TODO Auto-generated constructor stub
    }

    
    public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Boolean getIsSucess() {
		return isSucess;
	}


	public void setIsSucess(Boolean isSucess) {
		this.isSucess = isSucess;
	}


	public UUID getReqTimeUuid() {
		return reqTimeUuid;
	}


	public void setReqTimeUuid(UUID reqTimeUuid) {
		this.reqTimeUuid = reqTimeUuid;
	}


	public Timestamp getRequestTimestamp() {
		return requestTimestamp;
	}


	public void setRequestTimestamp(Timestamp requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getClientIpAddress() {
		return clientIpAddress;
	}


	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}


	public String getClientBrowserInfo() {
		return clientBrowserInfo;
	}


	public void setClientBrowserInfo(String clientBrowserInfo) {
		this.clientBrowserInfo = clientBrowserInfo;
	}


	public String getClientOperatingSystem() {
		return clientOperatingSystem;
	}


	public void setClientOperatingSystem(String clientOperatingSystem) {
		this.clientOperatingSystem = clientOperatingSystem;
	}


	public String getClientDeviceType() {
		return clientDeviceType;
	}


	public void setClientDeviceType(String clientDeviceType) {
		this.clientDeviceType = clientDeviceType;
	}


	public String getBearer() {
		return bearer;
	}


	public void setBearer(String bearer) {
		this.bearer = bearer;
	}


}
