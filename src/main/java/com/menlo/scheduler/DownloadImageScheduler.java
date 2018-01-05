package com.menlo.scheduler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.menlo.ftpService.FtpToAmazonRedShiftImageService;

/**
 * @author punyam
 *
 */
@EnableScheduling
@Service
public class DownloadImageScheduler {

	private static Logger LOGGER = Logger.getLogger(DownloadImageScheduler.class);

	@Autowired
	private FtpToAmazonRedShiftImageService ftpToAmazonRedShiftImageService;

	@Scheduled(cron = "${downloadImages.cronExpr}")
	public  void FtpToAmazonRedShiftImageProcess() throws IOException {

		// calling downloadImage service
		LOGGER.info("calling FtpToAmazonRedShiftImageProcessor to process  .");
		ftpToAmazonRedShiftImageService.propertyLoad();
		ftpToAmazonRedShiftImageService.downloadImageFromFtp();
		//ftpToAmazonRedShiftImageService.uploadFileToAmazonS3();
		/*ftpToAmazonRedShiftImageService.uploadFileToRedshift();*/
		LOGGER.info("Image processing service completed");
	}
}