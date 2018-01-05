package com.menlo.ftpService.controller;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.menlo.bo.AmazonRedShiftProperties;
import com.menlo.bo.AmazonS3Properties;
import com.menlo.bo.FtpProperties;
import com.menlo.ftpService.FtpToAmazonRedShiftImageService;
import com.menlo.util.SpringBootWarProperty;

@Controller
public class PropertiesControllerDemo {

	private static final Logger LOGGER = Logger
			.getLogger(PropertiesControllerDemo.class);

	@Autowired
	private SpringBootWarProperty springBootWarProperty;

	@RequestMapping("/loadProperties")
	public String loadProperties(ModelAndView modelAndView) {

		return "loadProperties.jsp";
	}

	@RequestMapping("/")
	public String index(ModelAndView modelAndView) {

		return "index.jsp";
	}

	@RequestMapping("/proppage")
	public String getProperties(FtpProperties ftpProperties,
			AmazonS3Properties amazonS3Properties,
			AmazonRedShiftProperties amazonRedShiftProperties)
			throws ConfigurationException, IOException {
		LOGGER.info("starting update the properties file");
		/*LOGGER.info(ftpProperties);
		LOGGER.info(amazonS3Properties);
		LOGGER.info(amazonRedShiftProperties);*/
		springBootWarProperty.setSpringBootWarProperty(ftpProperties,
				amazonS3Properties, amazonRedShiftProperties);
		LOGGER.info("properties file update finish");
		return "index.jsp";

	}

}
