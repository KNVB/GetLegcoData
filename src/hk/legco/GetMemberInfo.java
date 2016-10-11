package hk.legco;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import hk.legco.util.Utility;
import hk.legco.object.MemberDetail;
//政治團體 Political affiliation
public class GetMemberInfo 
{
	String detailsClassName="bio-member-detail-1";
	String memberInfoClassName="bio-member-info",photoClassName="bio-member-photo";
	ArrayList<MemberDetail> memberList=new ArrayList<MemberDetail>();
	HashMap<String,HashMap<String,String>>paList=new HashMap<String,HashMap<String,String>>();

	private MemberDetail getMemberDetail(Element info)
	{
		String temp;
		Elements tempElements;
		Element parentElement,ulElement;
		  
		MemberDetail m=new MemberDetail();
		m.photoLink=(info.getElementsByClass(photoClassName).select("img").get(0).absUrl("abs:src"));
		try 
		{
			Document doc = Utility.getWebPage(info.getElementsByClass(detailsClassName).select("a").get(0).absUrl("abs:href"));
			tempElements=doc.select("div#container");
			tempElements=tempElements.get(0).getElementsByTag("h2");
			temp=tempElements.get(0).text();
			temp=temp.substring(0,temp.indexOf("議員"));
			parentElement=doc.select("strong:matchesOwn(所屬政治團體 \\:)").get(0).parent();
			
			if(parentElement.childNodeSize()==1)
			{
				ulElement=parentElement.nextElementSibling();
				for (Element liElement : ulElement.getElementsByTag("li"))
				{
					Utility.addToMap(paList,liElement.text(),m,temp);
				}
			}
			else
			{
				Utility.addToMap(paList,"無派別",m,temp);
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	private void go() 
	{
		String period,temp;
		String[] periods=Utility.getCurrentTermPeriod().split("-");
		temp=periods[0];
		temp=temp.replaceFirst("20", "");
		period=temp+"-";
		temp=periods[1];
		temp=temp.replaceFirst("20", "");
		period=period+temp;
		String memberBiographiesURL = "http://www.legco.gov.hk/general/chinese/members/yr"+period+"/biographies.htm";
		try 
		{
			Document doc = Utility.getWebPage(memberBiographiesURL);
			Elements infoElements=doc.getElementsByClass(memberInfoClassName);
			for (Element infoElement:infoElements)
			{
				if (infoElement.getElementsByClass(detailsClassName).select("a").size()>0)
				{
					memberList.add(getMemberDetail(infoElement));
				}
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getResultInJSONFormat()
	{
		this.go();
		HashMap<String,String> memberList;
		String temp;
		StringBuilder resultBuilder=new StringBuilder(),tempBuilder;
		resultBuilder.append("{");
		for (String paName:this.paList.keySet())
		{
			resultBuilder.append("\""+paName+"\":[");
			memberList=this.paList.get(paName);
			tempBuilder=new StringBuilder();
			for (String memberName:memberList.keySet())
			{
				tempBuilder.append("\""+memberName+"\":"+memberList.get(memberName)+",");
			}
			temp=tempBuilder.toString();
			temp=temp.substring(0,temp.length()-1);
			temp=temp.replaceAll(",", ",\n");
			resultBuilder.append(temp);
			resultBuilder.append("],");
		}
		temp=resultBuilder.toString();
		temp=temp.substring(0,temp.length()-1);
		temp=temp.replaceAll("],","},\n");
		temp=temp.replaceAll(":\\[",":{");
		temp=temp.replaceAll("]","}");
		temp+="}";
		return temp;
	}
	public String getResultInCSVFormat()
	{
		this.go();
		HashMap<String,String> memberList;
		StringBuilder resultBuilder=new StringBuilder(),tempBuilder;
		for (String paName:this.paList.keySet())
		{
			resultBuilder.append(paName);
			memberList=this.paList.get(paName);
			tempBuilder=new StringBuilder();
			for (String memberName:memberList.keySet())
			{
				tempBuilder.append(","+memberName+"\n");
			}
			resultBuilder.append(tempBuilder.toString());
		}
		return resultBuilder.toString();
	}
	public static void main(String[] args) 
	{
		GetMemberInfo gl=new GetMemberInfo();
		String resultInCSVFormat=gl.getResultInCSVFormat();
		System.out.println(resultInCSVFormat);
		//String resultInJSONFormat=gl.getResultInJSONFormat();
		//System.out.println(resultInJSONFormat);

	}

	
}
