package com.menlo.bo;

public class AmazonS3Properties {

	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String amazonBaseUrl;
	private String amazonDomain;
	private String amazonFolderName;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getAmazonBaseUrl() {
		return amazonBaseUrl;
	}

	public void setAmazonBaseUrl(String amazonBaseUrl) {
		this.amazonBaseUrl = amazonBaseUrl;
	}

	public String getAmazonDomain() {
		return amazonDomain;
	}

	public void setAmazonDomain(String amazonDomain) {
		this.amazonDomain = amazonDomain;
	}

	public String getAmazonFolderName() {
		return amazonFolderName;
	}

	public void setAmazonFolderName(String amazonFolderName) {
		this.amazonFolderName = amazonFolderName;
	}

	@Override
	public String toString() {
		return "AmazonS3Properties [accessKey=" + accessKey + ", secretKey="
				+ secretKey + ", bucketName=" + bucketName + ", amazonBaseUrl="
				+ amazonBaseUrl + ", amazonDomain=" + amazonDomain
				+ ", amazonFolderName=" + amazonFolderName + "]";
	}

}