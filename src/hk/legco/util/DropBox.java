package hk.legco.util;
import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;

public class DropBox {
    public static void main(String[] args) throws IOException, DbxException {
        // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = "1drz1450rbvezat";
        final String APP_SECRET = "h46xgodh3l74idy";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        // Have the user sign in and authorize your app.
        webAuth.start();
        String accessToken = "4lub11wllzAAAAAAAAAAJJV1mSpRnZvGgZRLkcEeZ8VYeiHTgHmqoPWZKeuzqn7Z";

        DbxClient client = new DbxClient(config, accessToken);

        System.out.println("Linked account: " + client.getAccountInfo().displayName);

        File inputFile = new File("D:\\SITO3\\Documents\\Xmas-20141224-310.jpg");
        
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/legco/"+inputFile.getName(),
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.name);
        } finally {
            inputStream.close();
        }

        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/legco");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("	" + child.name + " is a " + (child.isFolder()?"Directory":"File"));
        }

        /*FileOutputStream outputStream = new FileOutputStream("magnum-opus.txt");
        try {
            DbxEntry.File downloadedFile = client.getFile("/magnum-opus.txt", null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } finally {
            outputStream.close();
        }*/
    }
}