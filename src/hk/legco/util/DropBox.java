package hk.legco.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.logging.log4j.Logger;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.DbxEntry.WithChildren;

public class DropBox 
{
	DbxClient client;
	Logger logger=null;
	public DropBox(Logger logger)
	{
		this.logger=logger;
		String accessToken = "4lub11wllzAAAAAAAAAAJJV1mSpRnZvGgZRLkcEeZ8VYeiHTgHmqoPWZKeuzqn7Z"; 
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());
		client = new DbxClient(config, accessToken);
	}
	public void upLoadFile(String srcFileName,String destPath,String destFileName) throws DbxException, IOException
	{
		File inputFile = new File(srcFileName);
        
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile(destPath+destFileName,
                DbxWriteMode.add(), inputFile.length(), inputStream);
            logger.debug("Uploaded: " + uploadedFile.name);
        } finally {
            inputStream.close();
            inputStream=null;
            inputFile=null;
        }
	}
	public WithChildren getDirectoryList(String directoryName) throws DbxException
	{
		 WithChildren listing = client.getMetadataWithChildren(directoryName);
		 return listing;
	}
}
