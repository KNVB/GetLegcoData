package hk.legco;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import hk.legco.util.Utility;
import hk.legco.util.QueryLegcoData;
import hk.legco.object.LegcoException;
import hk.legco.object.MemberVoteStat;

public class UpdateVoteStatWebPage {

	public static void main(String[] args)  
	{
		Iterator<String> memberNameList;
		MemberVoteStat memberVoteStat;
		QueryLegcoData query=new QueryLegcoData();
		HashMap<String, MemberVoteStat> result;
		String webFolderPath="D:\\Inetpub\\wwwroot\\legco\\",memberChiName;
		String headerFileName="voteStatHead",outputTempFileName="output.txt",outputFileName="output.html";
		
		PrintWriter pw=null;
		try 
		{
			if (!Files.exists(Paths.get(outputFileName)))
			{	
				result = query.getVoteStatByTermNo(Utility.getCurrentTermNo());
				Files.copy((new File(webFolderPath+headerFileName)).toPath(),(new File(webFolderPath+outputTempFileName)).toPath());
				pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(webFolderPath+outputTempFileName,true), "UTF-8"));
				pw.print("attendance={"+Utility.getCurrentTermNo()+":{");
				memberNameList=result.keySet().iterator();
				while (memberNameList.hasNext())
				{
					memberChiName=memberNameList.next();
					memberVoteStat=result.get(memberChiName);
					//pw.printf("%s\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\n",memberChiName,memberVoteStat.getYesRate(),memberVoteStat.getNoRate(),memberVoteStat.getAbstainRate(),memberVoteStat.getAbsentRate(),memberVoteStat.getAttendance());
					pw.printf("'%s':{'yes':%.3f,'no':%.3f,'abstain':%.3f,'absent':%.3f,'present':%.3f,'attendance':%.3f}", memberChiName,memberVoteStat.getYesRate(),memberVoteStat.getNoRate(),memberVoteStat.getAbstainRate(),memberVoteStat.getAbsentRate(),memberVoteStat.getPresentRate(),memberVoteStat.getAttendance());
					if (memberNameList.hasNext())
						pw.println(",");
					else	
						pw.println("}}");
				}
				pw.println("\t\t</script>");
				pw.println("\t</body>");
				pw.println("</html>");
				pw.close();
			}
		} 
		catch (LegcoException | IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		pw=null;
		
		query=null;
	}

}
