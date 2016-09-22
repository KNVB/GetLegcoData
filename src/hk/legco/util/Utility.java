package hk.legco.util;

import hk.legco.object.Member;

import java.util.HashMap;
import java.util.Calendar;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility 
{
	static int timeOut=5000;
	public static JSONObject query(Logger logger,String urlString) throws Exception
	{
		JSONObject result=null;
		JSONParser jsonParser=new JSONParser(); 
		result=(JSONObject)jsonParser.parse(Jsoup.connect(urlString).ignoreContentType(true).timeout(timeOut).get().text());
		return result;
	}
	public static Document getWebPage(String urlString) throws IOException
	{
		return Jsoup.connect(urlString).ignoreContentType(true).timeout(timeOut).get();
	}
	public static void addToMap(HashMap<String,HashMap<String,String>> map,String key,Member m) throws JsonProcessingException
	{
		HashMap<String,String> paList;
		ObjectMapper mapper = new ObjectMapper();
		if (map.containsKey(key))
		{
			paList=map.get(key);
			map.remove(key);
		}
		else
		{
			paList=new HashMap<String,String>();
		}
		paList.put(m.name,mapper.writeValueAsString(m));
		map.put(key, paList);			
	}
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
