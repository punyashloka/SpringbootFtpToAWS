/**
 *
 */
package com.menlo.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author
 *
 */
@Component
@Scope("singleton")
public class ConnectionFactory {

	private static Logger LOGGER = Logger.getLogger(ConnectionFactory.class);

	@Value(value = "${Redshift_Driver_Class}")
	private String redShiftDriverClass;

	private String redShiftDbURL;

	private String redShiftUsername;

	private String redShiftPassword;

	private static Connection connection;

	public final String PROPERTIES_FILE_PATH = "src/main/resources" + "/application.properties";
	
	// private constructor
	private ConnectionFactory() {

	}

	/**
	 *
	 * @return Connection
	 *
	 */
	public Connection getConnection() {

		try {
			Properties properties = new Properties();
			FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE_PATH);

			BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);

			properties.load(inputStream);
			redShiftDriverClass = properties.getProperty("Redshift_Driver_Class");
			redShiftDbURL = properties.getProperty("RedShift_DbURL");
			redShiftUsername = properties.getProperty("RedShift_MasterUsername");
			redShiftPassword = properties.getProperty("RedShift_MasterUserPassword");
			
			if (connection == null) {
				Class.forName(redShiftDriverClass);
				connection = DriverManager.getConnection(redShiftDbURL,
						redShiftUsername, redShiftPassword);
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			LOGGER.warn("Unable to Connect to Database." + e);
			e.printStackTrace();
		}
		return connection;
	}

}
