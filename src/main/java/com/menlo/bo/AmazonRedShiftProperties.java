package com.menlo.bo;

public class AmazonRedShiftProperties {

	private String redshiftDbURL;
	private String redshiftMasterUsername;
	private String redshiftMasterUserPassword;

	public String getRedshiftDbURL() {
		return redshiftDbURL;
	}

	public void setRedshiftDbURL(String redshiftDbURL) {
		this.redshiftDbURL = redshiftDbURL;
	}

	public String getRedshiftMasterUsername() {
		return redshiftMasterUsername;
	}

	public void setRedshiftMasterUsername(String redshiftMasterUsername) {
		this.redshiftMasterUsername = redshiftMasterUsername;
	}

	public String getRedshiftMasterUserPassword() {
		return redshiftMasterUserPassword;
	}

	public void setRedshiftMasterUserPassword(
			String redshiftMasterUserPassword) {
		this.redshiftMasterUserPassword = redshiftMasterUserPassword;
	}

	@Override
	public String toString() {
		return "AmazonRedShiftProperties [redshiftDbURL=" + redshiftDbURL
				+ ", redshiftMasterUsername=" + redshiftMasterUsername
				+ ", redshiftMasterUserPassword=" + redshiftMasterUserPassword
				+ "]";
	}

}
