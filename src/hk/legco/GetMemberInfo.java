package hk.legco;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import hk.legco.util.Utility;
import hk.legco.object.Member;
//政治團體 Political affiliation
public class GetMemberInfo 
{
	String detailsClassName="bio-member-detail-1";
	String memberInfoClassName="bio-member-info",photoClassName="bio-member-photo";
	ArrayList<Member> memberList=new ArrayList<Member>();
	HashMap<String,HashMap<String,String>>paList=new HashMap<String,HashMap<String,String>>();

	private Member getMemberDetail(Element info)
	{
		String temp;
		Elements tempElements;
		Element parentElement,ulElement;
		  
		Member m=new Member();
		m.photoLink=(info.getElementsByClass(photoClassName).select("img").get(0).absUrl("abs:src"));
		try 
		{
			Document doc = Utility.getWebPage(info.getElementsByClass(detailsClassName).select("a").get(0).absUrl("abs:href"));
			tempElements=doc.select("div#container");
			tempElements=tempElements.get(0).getElementsByTag("h2");
			temp=tempElements.get(0).text();
			m.name=temp.substring(0,temp.indexOf("議員"));
			parentElement=doc.select("strong:matchesOwn(所屬政治團體 \\:)").get(0).parent();
			
			if(parentElement.childNodeSize()==1)
			{
				ulElement=parentElement.nextElementSibling();
				for (Element liElement : ulElement.getElementsByTag("li"))
				{
					Utility.addToMap(paList,liElement.text(),m);
				}
			}
			else
			{
				Utility.addToMap(paList,"無派別",m);
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
		String period=Utility.getCurrentTermPeriod().replaceAll("20", "");
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

	public static void main(String[] args) 
	{
		GetMemberInfo gl=new GetMemberInfo();
		gl.go();
		JSONObject paList=new JSONObject(gl.paList);
		System.out.println(paList.toJSONString());
		//System.out.println(gl.memberList.size());
		
		/*for (Member m:gl.memberList)
		{
			System.out.println(m.name);
		}*/
		/*System.out.println(gl.paList.size());
		for (String key:gl.paList.keySet())
		{
			pa=gl.paList.get(key);
			temp="";
			System.out.println(key);
			for (String memberName:pa)
			{
				temp=temp+memberName+",";
			}
			temp=temp.replaceAll(",$","\n");
			System.out.println(temp+"===============================");
		}*/
	}

	
}
