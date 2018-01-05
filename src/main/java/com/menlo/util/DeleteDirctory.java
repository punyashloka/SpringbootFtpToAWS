package com.menlo.util;

import java.io.File;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DeleteDirctory {

	private static Logger LOGGER = Logger.getLogger(DeleteDirctory.class);	
	
	
	public boolean deleteFile(String filePath) {
		
		File path= new File(filePath);
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteFile(files[i].toString());
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    LOGGER.info("path is deleted");
	    return( path.delete() );
	  }
	
}
