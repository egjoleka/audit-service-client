package com.izettle.assignment.client.ddo;

import java.util.List;


public class AuditsDisplayEntity {

	private List<LoginAudit> loginAudits;

	public AuditsDisplayEntity() {
     //cxf needs it
	}

	public AuditsDisplayEntity(List<LoginAudit> loginAudits) {
		this.loginAudits = loginAudits;
	}

	public List<LoginAudit> getLoginAudits() {
		return loginAudits;
	}

	public void setLoginAudits(List<LoginAudit> loginAudits) {
		this.loginAudits = loginAudits;
	}

	@Override
	public String toString() {
		return "Audits [loginAudits=" + loginAudits + "]";
	}

}
