package hk.legco.object;

import java.util.ArrayList;

public class PoliticalAffiliation 
{
	//政治團體 Political affiliation
	public String name=null;
	private ArrayList<String> members=new ArrayList<String>();
	public void addMember(String memberName)
	{
		members.add(memberName);
	}
	public ArrayList<String> getMemberList()
	{
		return members;
	}
}
