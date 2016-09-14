package hk.legco;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadData {

	public static void main(String[] args) 
	{
/*		Vector<String> memberDetailLinks=new Vector<String>();
		String detailsClassName="bio-member-detail-1",memberName,strSQL=new String(),term="12-16";
		String theURL = "http://www.legco.gov.hk/general/chinese/members/yr12-16/biographies.htm";
		DbOp dbo=null;
		try 
		{
			Document doc = Jsoup.connect(theURL).get();
			Element parentElement=null,ulElement;
			Elements tempElements,links,details=doc.getElementsByClass(detailsClassName);
			for (Element detail:details)
			{
				links=detail.getElementsByTag("a");
				if (links.size()>0) 
				{
					memberDetailLinks.add(links.get(0).absUrl("abs:href"));
				}
			}
			dbo=new DbOp();
			for (String link : memberDetailLinks)
			{
				doc= Jsoup.connect(link).get();
				tempElements=doc.select("div#container");
				tempElements=tempElements.get(0).getElementsByTag("h2");
				memberName=tempElements.get(0).text();
				memberName=memberName.substring(0,memberName.indexOf("霅啣"));
				tempElements=doc.select("strong:matchesOwn(��惇�踵祥�� \\:)");
				parentElement=tempElements.get(0).parent();
				if(parentElement.childNodeSize()==1)
				{
					ulElement=parentElement.nextElementSibling();
					for (Element liElement : ulElement.getElementsByTag("li"))
					{
						//System.out.println("Member Name="+memberName+",Party="+liElement.text());
						strSQL ="insert into member_to_party (term,member_name,party_name)values";
						strSQL+="('"+term+"','"+memberName+"','"+liElement.text()+"')";
						dbo.executeUpdate(strSQL);
					}
				}
				else
				{
					//System.out.println("Member Name="+memberName+",Party=-");
					strSQL="insert into member_to_party (term,member_name,party_name)values";
					strSQL+="('"+term+"','"+memberName+"','-')";
					dbo.executeUpdate(strSQL);
				}
				
			}
		} 
		catch (IOException | SQLException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if (dbo!=null)
			{	
				try 
				{
					dbo.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbo=null;
			}
			
		}*/
	}

}
