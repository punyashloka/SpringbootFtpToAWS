package com.menlo.bo;

public class FtpProperties {

	
	private String url;
	private Integer port;
	private String userName;
	private String passWord;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Override
	public String toString() {
		return "ApplicationProperties [url=" + url + ", port=" + port + ", userName=" + userName + ", passWord="
				+ passWord + "]";
	}
	
	
	
}
