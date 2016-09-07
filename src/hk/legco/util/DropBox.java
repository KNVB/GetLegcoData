package hk.legco.util;

import java.util.Locale;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxEntry.WithChildren;

public class DropBox 
{
	DbxClient client;
	public DropBox()
	{
		String accessToken = "4lub11wllzAAAAAAAAAAJJV1mSpRnZvGgZRLkcEeZ8VYeiHTgHmqoPWZKeuzqn7Z"; 
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());
		client = new DbxClient(config, accessToken);
	}
	public WithChildren getDirectoryList(String directoryName) throws DbxException
	{
		 WithChildren listing = client.getMetadataWithChildren(directoryName);
		 return listing;
	}
}
