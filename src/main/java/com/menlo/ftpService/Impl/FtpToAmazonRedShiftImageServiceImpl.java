/**
 *
 */
package com.menlo.ftpService.Impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.menlo.ftpService.FtpToAmazonRedShiftImageService;
import com.menlo.util.ConnectionCheck;
import com.menlo.util.ConnectionFactory;
import com.menlo.util.DeleteDirctory;

/**
 * @author punyam
 *
 */
@Service
public class FtpToAmazonRedShiftImageServiceImpl
		implements FtpToAmazonRedShiftImageService {

	private static Logger LOGGER = Logger
			.getLogger(FtpToAmazonRedShiftImageServiceImpl.class);

	@Autowired
	private ConnectionCheck connectionCheck;

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private DeleteDirctory deleteDirctory;

	@Value(value = "${IMG_GEN_OUTPUT_LOCAL_DIRECTORY}")
	private String localDirectory;

	@Value(value = "${IMG_GEN_OUTPUT_WORKING_DIRECTORY}")
	private String workingDirectory;

	private String bucketName;
	private String amazonBaseUrl;
	private String amazonDomain;
	private String amazonFolderName;
	private String accessKey;
	private String secretKey;
	private String server;
	private Integer port;
	private String username;
	private String password;
	private static AmazonS3 s3client;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String sqlQuery;
	private Boolean status = false;

	private final String PATH_SEPARATOR = "/";
	public final String PROPERTIES_FILE_PATH = "src/main/resources"
			+ "/application.properties";

	/**
	 * get the value from property file in runtime
	 *
	 * @throws IOException
	 */
	@Override
	public void propertyLoad() throws IOException {

		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(
				PROPERTIES_FILE_PATH);

		BufferedInputStream inputStream = new BufferedInputStream(
				fileInputStream);

		properties.load(inputStream);
		server = properties.getProperty("SERVER_BASE_URL");
		port = Integer.parseInt(properties.getProperty("FTP_SERVER_PORT"));
		username = properties.getProperty("USER_NAME");
		password = properties.getProperty("PASSWORD");

		bucketName = properties.getProperty("BUCKET_NAME");
		amazonBaseUrl = properties.getProperty("AMAZON_BASE_URL");
		amazonDomain = properties.getProperty("AMAZON_DOMAIN");
		amazonFolderName = properties.getProperty("AMAZON_FOLDER_NAME");
		accessKey = properties.getProperty("ACCESS_KEY");
		secretKey = properties.getProperty("SECRET_KEY");
	}

	/**
	 * check connection of Ftp
	 *
	 * @return FTPClient
	 * @throws IOException
	 */
	@Override
	public FTPClient checkFtpConnection() throws IOException {

		FTPClient ftpClient = new FTPClient();
		try {
			LOGGER.info("Attempted to connect to server ");
			ftpClient.connect(server, port);
			connectionCheck.showServerReply(ftpClient);
			int replyCode = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				LOGGER.info(
						"Operation failed. Server reply code: " + replyCode);
			}

			LOGGER.info("Connection to server is sucesfull ");
			LOGGER.info("Attempted to login to server ");
			status = ftpClient.login(username, password);
			connectionCheck.showServerReply(ftpClient);

			if (!status) {
				LOGGER.warn("Could not login to the server");
				return null;
			} else {
				LOGGER.info("LOGGED IN SERVER");
			}
		} catch (IOException ex) {
			LOGGER.warn("Exception in login" + ex);
			LOGGER.error(ex);
		}
		return ftpClient;
	}

	/**
	 * downloading image from Ftp
	 *
	 * @return Boolean
	 * @throws IOException
	 */
	@Override
	public Boolean downloadImageFromFtp() throws IOException {
		Boolean flag = false;
		LOGGER.info("connection checking of server ");
		FTPClient ftpClient = checkFtpConnection();

		if (ftpClient == null) {
			LOGGER.info("connection couldn't established ");
			return flag;
		}
		try {
			// setting file type to BINARY_FILE_TYPE
			// and TransferMode to BLOCK_TRANSFER_MODE
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.BLOCK_TRANSFER_MODE);
			// Delete old directory if exists
			deleteDirctory.deleteFile(localDirectory);
			// creating new file with current time in local
			String currentTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
					.format(new Date());
			File file = new File(localDirectory + PATH_SEPARATOR + currentTime);
			Boolean fileStatus = file.exists();
			// if file not exists create it
			if (!file.exists()) {
				fileStatus = file.mkdirs();
				LOGGER.info("new file is created ");
			}

			if (fileStatus) {
				// set connection to LocalPassiveMode
				ftpClient.enterLocalPassiveMode();
				// changing Working Directory
				LOGGER.info("changing Working Directory ");
				ftpClient.changeWorkingDirectory(workingDirectory);

				// getting list of files from working directory
				FTPFile[] listofDirectories = ftpClient.listDirectories();
				LOGGER.info(listofDirectories.length);

				if (listofDirectories != null
						&& listofDirectories.length == 0) {
					FTPFile[] listofFiles = ftpClient.listFiles();
					for (FTPFile fTPFile : listofFiles) {
						flag = downloadSingleFile(ftpClient,
								workingDirectory + fTPFile.getName(),
								file.getAbsolutePath() + PATH_SEPARATOR
										+ fTPFile.getName());
					}
				} else {
					for (FTPFile fTPFile : listofDirectories) {
						LOGGER.info("call for directory");
						flag = downloadDirectory(ftpClient,
								workingDirectory + fTPFile.getName(),
								file.getAbsolutePath() + PATH_SEPARATOR
										+ fTPFile.getName());
						LOGGER.info("Download Directory is " + flag);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.warn("Exception in download" + ex);
			LOGGER.error(ex);
		}
		return flag;
	}

	/**
	 * downloading from directory
	 *
	 * @return Boolean
	 * @param ftpClient
	 * @param parentDir
	 * @param currentDir
	 * @throws IOException
	 */
	private Boolean downloadDirectory(FTPClient ftpClient, String parentDir,
			String currentDir) throws IOException {
		boolean success = false;
		String dirToList = parentDir;

		LOGGER.info("getting subFiles");
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".")
						|| currentFileName.equals("..")) {
					// skip parent directory and the directory itself
					break;
				}
				dirToList = dirToList + PATH_SEPARATOR + currentFileName;
				String filePath = parentDir + PATH_SEPARATOR + currentFileName;
				if (currentDir.equals("")) {
					filePath = currentFileName;
				}
				String newDirPath = currentDir + PATH_SEPARATOR
						+ currentFileName;
				if (currentDir.equals("")) {
					newDirPath = currentFileName;
				}
				if (aFile.isDirectory()) {
					// create the directory in saveDir
					File newDir = new File(newDirPath);
					boolean created = newDir.mkdirs();
					if (created) {
						LOGGER.info("CREATED the directory: " + newDirPath);
					} else {
						LOGGER.info("COULD NOT create the directory: "
								+ newDirPath);
					}

					// download the sub directory
					downloadDirectory(ftpClient, dirToList, newDirPath);
				} else {
					// download the file
					success = downloadSingleFile(ftpClient, filePath,
							newDirPath);
					if (success) {
						LOGGER.info("DOWNLOADED the file to : " + filePath);

					} else {
						LOGGER.info("COULD NOT download the file: " + filePath);
					}
				}
			}
		}
		return success;
	}

	/**
	 * downloading from files
	 *
	 * @param ftpClient
	 * @param remoteFilePath
	 * @param savePath
	 * @return
	 * @throws IOException
	 */
	private boolean downloadSingleFile(FTPClient ftpClient,
			String remoteFilePath, String savePath) throws IOException {
		Boolean flag = false;
		File downloadFile = new File(savePath);

		File parentDir = downloadFile.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}

		OutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(downloadFile));
		InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setBufferSize(1024);

			LOGGER.info(ftpClient.getReplyString());
			byte[] bytesArray = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytesArray)) != -1) {
				outputStream.write(bytesArray, 0, bytesRead);
			}

			flag = ftpClient.completePendingCommand();
			if (flag) {
				LOGGER.info("File " + downloadFile + ""
						+ " has been downloaded successfully.");
			}

			return flag;
		} catch (IOException ex) {
			LOGGER.warn("Oops! Something wrong happened in downloading files "
					+ ex);
			throw ex;
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}

	/***
	 * uploads the given file from the given path to Amazon s3
	 * 
	 */
	@SuppressWarnings("unused")
	@Override
	public void uploadFileToAmazonS3() {

		AWSCredentials credentials = new BasicAWSCredentials(accessKey,
				secretKey);
		if (s3client == null) {
			LOGGER.info("creating new AmazonS3Client ");
			s3client = new AmazonS3Client(credentials);
		}
		LOGGER.info(s3client.doesBucketExist(bucketName));
		if (!s3client.doesBucketExist(bucketName)) {
			throw new IllegalArgumentException(
					"The specified bucket doesn't exist.");
		}

		String url = null;
		try {
			if (!isFolderExists(amazonFolderName)) {
				// create a folder
				createFolder(amazonFolderName);
			}
			// upload file to folder and set it to public
			File file = new File(localDirectory);
			String[] internalFile = file.list();
			File imageFolder = new File(
					localDirectory + PATH_SEPARATOR + internalFile[0]);
			LOGGER.info(imageFolder.listFiles());
			for (File image : imageFolder.listFiles()) {
				if (file.exists()) {
					// this file name will be used as a key to
					// store/retrieve a file
					String fileName = amazonFolderName + PATH_SEPARATOR
							+ image.getName();
					PutObjectResult result = s3client.putObject(
							new PutObjectRequest(bucketName, fileName, image)
									.withCannedAcl(
											CannedAccessControlList.PublicRead));
					if (result != null) {
						LOGGER.info("Image Uploaded into amazon s3 Object"
								+ " repository:" + result);
					} else {
						LOGGER.info(
								"Image is not Uploaded into amazon s3 Object "
										+ "repository:" + fileName);
					}
				}
			}
		} catch (AmazonClientException ace) {
			LOGGER.info("Exception while uploading file:" + localDirectory);
			LOGGER.error(ace);
		}
	}

	/**
	 * creates a folder in amazon s3
	 *
	 * @param folderName
	 */
	private void createFolder(String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + PATH_SEPARATOR, emptyContent, metadata);
		// send request to S3 to create folder
		s3client.putObject(putObjectRequest);
		LOGGER.info("Folder" + folderName + "is created");
	}

	/**
	 * Checks if the given folder exists
	 *
	 * @param folderName
	 * @return
	 */
	private Boolean isFolderExists(String folderName) {
		List<S3ObjectSummary> fileList = s3client
				.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary objectSummary : fileList) {
			if (objectSummary.getKey().equals(folderName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void uploadFileToRedshift() {

		try {
			connection = connectionFactory.getConnection();
			LOGGER.info(connection);
			if (connection == null) {
				return;
			}
			LOGGER.info("Listing system tables...");
			statement = connection.createStatement();
			sqlQuery = "select * from information_schema.tables;";
			/*
			 * sqlQuery = "copy images from 's3://' CREDENTIALS     " +
			 * "'aws_access_key_id=xxxxxxx;aws_secret_access_key=xxxxxxxxx'";
			 */
			LOGGER.info("Sql Query : " + sqlQuery);
			resultSet = statement.executeQuery(sqlQuery);
			/*
			 * Integer affectedRowCount = statement.executeUpdate(sqlQuery);
			 * LOGGER.info( affectedRowCount + "row affected ");
			 */
			// Get the data from the result set.
			while (resultSet.next()) {
				// Retrieve two columns.
				String catalog = resultSet.getString("table_catalog");
				String name = resultSet.getString("table_name");

				// Display values.
				LOGGER.info("Catalog: " + catalog);
				LOGGER.info(", Name: " + name);

				resultSet.close();
				statement.close();
				connection.close();
			}
		} catch (SQLException ex) {
			LOGGER.warn("Exception in uploadFileToRedshift " + ex);
			LOGGER.error(ex);
		}
	}

}
