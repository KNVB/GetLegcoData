package hk.legco.object;

public class MemberVoteStat 
{
	private int yesCount=0,noCount=0;
	private int presentCount=0,totalCount=0;
	private int abstainCount=0,absentCount=0;
	public int getYesCount() 
	{
		return yesCount;
	}
	public void setYesCount(int yesCount) 
	{
		this.yesCount = yesCount;
	}
	public int getNoCount() 
	{
		return noCount;
	}
	public void setNoCount(int noCount) 
	{
		this.noCount = noCount;
	}
	public int getPresentCount() 
	{
		return presentCount;
	}
	public void setPresentCount(int presentCount) 
	{
		this.presentCount = presentCount;
	}
	public int getAbstainCount() 
	{
		return abstainCount;
	}
	public void setAbstainCount(int abstainCount) 
	{
		this.abstainCount = abstainCount;
	}
	public int getAbsentCount() 
	{
		return absentCount;
	}
	public void setAbsentCount(int absentCount) 
	{
		this.absentCount = absentCount;
	}
	public float presentRate()
	{
		float result;
		totalCount=this.absentCount + this.abstainCount+this.noCount+this.presentCount+this.yesCount;
		result=(float)(totalCount-this.absentCount)/(float)(totalCount);
		return result;
	}
}
