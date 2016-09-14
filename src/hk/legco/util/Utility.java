package hk.legco.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;

public class Utility 
{
	public static JSONObject query(Logger logger,String urlString) throws Exception
	{
		JSONObject result=null;
		JSONParser jsonParser=new JSONParser(); 
		result=(JSONObject)jsonParser.parse(Jsoup.connect(urlString).ignoreContentType(true).timeout(5000).get().text());
		return result;
	}
	/*public static JSONObject query(Logger logger,String urlString) throws MalformedURLException,IOException, ParseException
	{
		JSONObject result=null;
		JSONParser jsonParser=new JSONParser(); 
		CloseableHttpClient httpclient = HttpClients.createDefault();
	    try 
	    {
	        HttpGet httpget = new HttpGet(urlString);

	        logger.debug("Executing request " + httpget.getRequestLine());
	        CloseableHttpResponse response = httpclient.execute(httpget);
			try 
			{
				//System.out.println("----------------------------------------");
				//System.out.println(response.getStatusLine());
				
				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				
				// If the response does not enclose an entity, there is no need
				// to bother about connection release
				if (entity != null) 
				{
					result=(JSONObject)jsonParser.parse(new InputStreamReader(entity.getContent(),"UTF-8"));
				} 
			}
			finally 
			{
			  response.close();
			  response=null;
			  httpget = null;
			}
		} 
	    finally 
	    {
	    	httpclient.close();
	    	httpclient=null;
		}		
		return result;
	}*/
	public static int getCurrentTermNo()
	{
		int result=0;
		int theYear=2000,thisMonth,thisYear;
		Calendar calendar = new GregorianCalendar();		
		thisYear= calendar.get(Calendar.YEAR);
		thisMonth= calendar.get(Calendar.MONTH)+1;
		if (thisYear>1997)
		{
			if (thisYear<2001)
				result++;
			else
			{
				result++;
				while (theYear<thisYear)
				{
					theYear+=4;
					result++;
				}
			}
			if ((theYear==thisYear)&& (thisMonth>9))
			{
				result++;
			}
		}		
		return result;
	}
	public static String getCurrentTermPeriod()
	{
		String result=new String();
		int theYear=2000,thisMonth,thisYear;
		Calendar calendar = new GregorianCalendar();		
		thisYear= calendar.get(Calendar.YEAR);
		thisMonth= calendar.get(Calendar.MONTH)+1;
		while (theYear<thisYear)
		{
			theYear+=4;
		}
		if ((theYear==thisYear)&& (thisMonth>9))
			result=theYear+"-"+(theYear+4);
		else
			result=(theYear-4)+"-"+theYear;
		return result;
	}
	public static String getTermPeriod(int termNo)
	{
		int i,thisYear=2000;
		String result=null;
		if (termNo>0)
		{
			if (termNo==1)
			{
				result="1998-2000";
			}
			else
			{
				for (i=2;i<=termNo;i++)
				{
					thisYear+=4;
				}
				result=(thisYear-4)+"-"+thisYear;
			}
		}
		return result;
	}
	public static void main(String[] args) throws Exception 
	{
		Logger logger = LogManager.getLogger(Utility.class); 
		logger.debug("Log4j2 is ready.");
		Utility.query(logger,"http://www.cm");
		System.out.println(Utility.getTermPeriod(6));
		System.out.println(Utility.getCurrentTermNo());
		System.out.println(Utility.getCurrentTermPeriod());
	}
}
