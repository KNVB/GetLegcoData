package hk.legco.util;

import hk.legco.object.LegcoException;
import hk.legco.object.Motion;
import hk.legco.object.MemberVoteStat;

import java.util.HashMap;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;

public class QueryLegcoData 
{
	int maxQueryRetryCount=10;
	Logger logger=null;
	String votingResultURL="http://app.legco.gov.hk/vrdb/odata/vVotingResult?";
	public QueryLegcoData(Logger logger)
	{
		this.logger=logger;
	}
	public HashMap <String,MemberVoteStat>getVoteStatByTermNo(int termNo) throws LegcoException
	{
		int i,retryCount=0,totalRecordCount=0,currentRecordCount=0;
		boolean finish=false;
		String filterString,memberChiName;
		JSONArray tempObj=new JSONArray();
		JSONObject resultObj=null,memberVoteStatObj;
		
		MemberVoteStat memberVoteStat=new MemberVoteStat();
		HashMap <String,MemberVoteStat>result=new HashMap <String,MemberVoteStat>();
		try
		{
			if ((termNo>0) && (termNo<=Utility.getCurrentTermNo()))
			{
				filterString="$select=name_ch,vote&$orderby=vote_time+desc&$orderby=type,motion_ch&$inlinecount=allpages&$filter=";
				filterString+=URLEncoder.encode("term_no eq "+termNo,"UTF-8");
				filterString=votingResultURL+filterString;
				while (!finish)
				{
					while ((tempObj.size()==0) && (retryCount<maxQueryRetryCount))
					{
						retryCount++;
						resultObj=(JSONObject) Utility.query(logger,filterString);
						tempObj=(JSONArray)resultObj.get("value");
					}
					currentRecordCount+=tempObj.size();
					totalRecordCount=Integer.valueOf((String) resultObj.get("odata.count"));
					for (i=0;i<tempObj.size();i++)
					{
						memberVoteStatObj=(JSONObject)tempObj.get(i);
						memberChiName=(String)memberVoteStatObj.get("name_ch");
						if (result.containsKey(memberChiName))
						{
							memberVoteStat=result.get(memberChiName);
						}
						else
						{
							memberVoteStat=new MemberVoteStat();
						}
						switch ((String)memberVoteStatObj.get("vote"))
						{
							case "Yes":memberVoteStat.setYesCount(memberVoteStat.getYesCount()+1);
										break;
							case "No":memberVoteStat.setNoCount(memberVoteStat.getNoCount()+1);
										break;
							case "Abstain":memberVoteStat.setAbstainCount(memberVoteStat.getAbstainCount()+1);
											break;
							case "Absent":memberVoteStat.setAbsentCount(memberVoteStat.getAbsentCount()+1);
											break;
							case "Present": memberVoteStat.setPresentCount(memberVoteStat.getPresentCount()+1);
											break;
						}					
						result.put(memberChiName, memberVoteStat);
					}
					if (resultObj.keySet().contains("odata.nextLink"))
					{	
						filterString=(String)resultObj.get("odata.nextLink");
						retryCount=0;
						tempObj=new JSONArray();
					}
					else
					{	
						finish=true;
					}
				}
				tempObj=null;
				memberVoteStatObj=null;
				resultObj=null;
				if ((totalRecordCount>0) && (totalRecordCount==currentRecordCount))
				{
					for (String key:result.keySet())
					{
						memberVoteStat=result.get(key);
						memberVoteStat.calTotal();
						result.put(key,memberVoteStat);
					}
				}
				else
				{	
					result.clear();
					result=null;
					throw (new LegcoException("Cannot fetch all vote record."));
				}
			}
			else
				throw (new LegcoException("Invalid Term No"));
		}
		catch (IOException| ParseException e)
		{
			throw (new LegcoException(e.getMessage()));
		}
		return result;
	}
	public ArrayList<Motion> getMotionListByTermNo(int termNo) throws UnsupportedEncodingException, MalformedURLException, IOException, ParseException, java.text.ParseException 
	{
		Motion motion;
		int i,retryCount=0;
		boolean finish=false;
		
		JSONArray tempObj=new JSONArray();
		JSONObject resultObj=null,motionObj;
		String motionName,preMotionName="",filterString="";
		ArrayList<Motion> result=new ArrayList<Motion>();
		if ((termNo>0) && (termNo<=Utility.getCurrentTermNo()))
		{	
			filterString="$select=type,motion_ch,vote_date,vote_time&$orderby=vote_time+desc&$orderby=type,motion_ch&$inlinecount=allpages&$filter=";
			filterString+=URLEncoder.encode("term_no eq "+termNo,"UTF-8");
			filterString=votingResultURL+filterString;
			while (!finish)
			{
				while ((tempObj.size()==0) && (retryCount<maxQueryRetryCount))
				{
					retryCount++;
					resultObj=Utility.query(logger,filterString);
					tempObj=(JSONArray) resultObj.get("value");
				}
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
	/*public static void main(String[] args) 
	{
		QueryLegcoData q=new QueryLegcoData();
		try 
		{
			q.getMotionListByTermNo(Utility.getCurrentTermNo());
			q.getVoteStatByTermNo(Utility.getCurrentTermNo());
		} 
		catch (IOException | LegcoException|ParseException | java.text.ParseException e) 
		{
			e.printStackTrace();
		} 
	}*/
}
