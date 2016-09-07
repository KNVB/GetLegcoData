package hk.legco;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import hk.legco.util.Utility;
import hk.legco.util.QueryLegcoData;
import hk.legco.object.LegcoException;
import hk.legco.object.MemberVoteStat;

public class Main {

	public static void main(String[] args)  
	{
		MemberVoteStat memberVoteStat;
		QueryLegcoData query=new QueryLegcoData();
		HashMap<String, MemberVoteStat> result;
		PrintWriter pw=null;
		try 
		{
			pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream("d:\\output.txt"), "UTF-8"));
			result = query.getVoteStatByTermNo(Utility.getCurrentTermNo());
			for (String memberChiName:result.keySet())
			{
				memberVoteStat=result.get(memberChiName);
				pw.printf("%s\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\n",memberChiName,memberVoteStat.getYesRate(),memberVoteStat.getNoRate(),memberVoteStat.getAbstainRate(),memberVoteStat.getAbsentRate(),memberVoteStat.getAttendance());
			}
			pw.close();
		} 
		catch (LegcoException | UnsupportedEncodingException | FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		pw=null;
		query=null;
	}

}
