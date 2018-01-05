package com.menlo.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.menlo.bo.AmazonRedShiftProperties;
import com.menlo.bo.AmazonS3Properties;
import com.menlo.bo.FtpProperties;

@Component
public class SpringBootWarProperty {

	private static Logger LOGGER = Logger
			.getLogger(SpringBootWarProperty.class);

	private final String PROPERTIES_FILE_PATH = "src/main/resources"
			+ "/application.properties";

	public void setSpringBootWarProperty(FtpProperties ftpProperties,
			AmazonS3Properties amazonS3Properties,
			AmazonRedShiftProperties amazonRedShiftProperties)
			throws ConfigurationException {

		PropertiesConfiguration props = new PropertiesConfiguration(
				PROPERTIES_FILE_PATH);

		// setting Ftp property
		props.setProperty("SERVER_BASE_URL", ftpProperties.getUrl());
		props.setProperty("FTP_SERVER_PORT",
				ftpProperties.getPort().toString());
		props.setProperty("USER_NAME", ftpProperties.getUserName().trim());
		props.setProperty("PASSWORD", ftpProperties.getPassWord().trim());

		// amazon S3 property
		LOGGER.info("amazone" + amazonS3Properties);
		props.setProperty("ACCESS_KEY",
				amazonS3Properties.getAccessKey().trim());
		props.setProperty("SECRET_KEY",
				amazonS3Properties.getSecretKey().trim());
		props.setProperty("BUCKET_NAME",
				amazonS3Properties.getBucketName().trim());
		props.setProperty("AMAZON_BASE_URL",
				amazonS3Properties.getAmazonBaseUrl().trim());
		props.setProperty("AMAZON_DOMAIN",
				amazonS3Properties.getAmazonDomain().trim());
		props.setProperty("AMAZON_FOLDER_NAME",
				amazonS3Properties.getAmazonFolderName().trim());

		// amazon redshift property setting
		LOGGER.info("amazoneRedshift" + amazonRedShiftProperties);
		props.setProperty("RedShift_DbURL",
				amazonRedShiftProperties.getRedshiftDbURL().trim());
		props.setProperty("RedShift_MasterUsername",
				amazonRedShiftProperties.getRedshiftMasterUsername().trim());
		props.setProperty("RedShift_MasterUserPassword",
				amazonRedShiftProperties.getRedshiftMasterUserPassword()
						.trim());

		PropertiesConfigurationLayout layout = props.getLayout();
		layout.setLineSeparator("\n");
		//save in default layout
		props.setLayout(layout);

		//saving properties file
		props.save();
	}

}
