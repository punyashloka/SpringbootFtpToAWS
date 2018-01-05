package com.menlo.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author punyam
 *
 */
@Component
public class ConnectionCheck {

	private static Logger LOGGER = Logger.getLogger(ConnectionCheck.class);

	/**
	 * showing reply of FTP server
	 * 
	 * @param ftpClient
	 */
	public void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				LOGGER.info("Server reply is : " + aReply);
			}
		}
	}
}
