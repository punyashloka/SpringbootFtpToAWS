package com.menlo.ftpService;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

@Service
public interface FtpToAmazonRedShiftImageService {

	public FTPClient checkFtpConnection() throws IOException;

	public Boolean downloadImageFromFtp() throws IOException;

	public void uploadFileToAmazonS3();

	public void uploadFileToRedshift();

	void propertyLoad() throws FileNotFoundException, IOException;
}
