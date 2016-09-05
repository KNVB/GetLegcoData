package hk.legco.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Motion 
{
	private String type,motionChiName;
	private Calendar voteDate=new GregorianCalendar(),voteTime=new GregorianCalendar();
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMotionChiName() {
		return motionChiName;
	}
	public void setMotionChiName(String motionChiName) 
	{
		this.motionChiName = motionChiName;
	}
	public Calendar getVoteDate() 
	{
		return voteDate;
	}
	public void setVoteDate(String voteDate) throws ParseException 
	{												//2014-12-05T15:27:24
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		this.voteDate.setTime(sdf.parse(voteDate));// all done
	}
	public Calendar getVoteTime() 
	{
		return voteTime;
	}
	public void setVoteTime(String voteTime) throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		this.voteTime.setTime(sdf.parse(voteTime));
	}
}
