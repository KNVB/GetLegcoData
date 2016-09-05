package hk.legco.util;

import hk.legco.object.Motion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.net.MalformedURLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Query 
{
	int maxQueryRetryCount=10;
	Logger logger=null;
	String rootURL="http://app.legco.gov.hk/vrdb/odata/vVotingResult?";
	public Query()
	{
		logger = LogManager.getLogger(this.getClass()); 
		logger.debug("Log4j2 is ready.");
	}
	public ArrayList<Motion> getMotionListByTermNo(int termNo) throws UnsupportedEncodingException, MalformedURLException, IOException, ParseException, java.text.ParseException 
	{
		Motion motion;
		int i,retryCount=0;
		boolean finish=false;
		JSONObject resultObj=null,motionObj;
		JSONArray tempObj=new JSONArray();
		String motionName,preMotionName="",filterString="";
		ArrayList<Motion> result=new ArrayList<Motion>();
		if ((termNo>0) && (termNo<=Utility.getCurrentTermNo()))
		{	
			filterString="$select=type,motion_ch,vote_date,vote_time&$orderby=vote_time+desc&$orderby=type,motion_ch&$inlinecount=allpages&$filter=";
			filterString+=URLEncoder.encode("term_no eq "+termNo,"UTF-8");
			filterString=rootURL+filterString;
			while (!finish)
			{
				while ((tempObj.size()==0) && (retryCount<maxQueryRetryCount))
				{
					retryCount++;
					resultObj=Utility.query(logger,filterString);
					tempObj=(JSONArray) resultObj.get("value");
				}
				i=0;
				for (i=0;i<tempObj.size();i++)
				{
					motionObj=(JSONObject) tempObj.get(i);			
					motionName=(String)motionObj.get("motion_ch");
					if (!preMotionName.equals(motionName))
					{
						//result.add(motionObj);
						preMotionName=motionName;
						motion=new Motion();
						
						/*logger.debug((String)motionObj.get("type"));
						logger.debug((String)motionObj.get("motion_ch"));
						logger.debug((String)motionObj.get("vote_date"));
						logger.debug((String)motionObj.get("vote_time"));*/
						
						motion.setType((String)motionObj.get("type"));
						motion.setMotionChiName((String)motionObj.get("motion_ch"));
						motion.setVoteDate((String)motionObj.get("vote_date"));
						motion.setVoteTime((String)motionObj.get("vote_time"));
						result.add(motion);
					}
				}
				if (resultObj.keySet().contains("odata.nextLink"))
				{	
					filterString=(String)resultObj.get("odata.nextLink");
					retryCount=0;
					tempObj=new JSONArray();
				}
				else
					finish=true;
			}
			tempObj=null;
			motionObj=null;
			resultObj=null;
		}
		return result;
	}
	public static void main(String[] args) 
	{
		Query q=new Query();
		try 
		{
			q.getMotionListByTermNo(Utility.getCurrentTermNo());
		} 
		catch (IOException | ParseException e) 
		{
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
