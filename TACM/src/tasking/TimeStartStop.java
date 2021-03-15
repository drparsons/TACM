package tasking;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

//purpose of class is to setup abstract for a start and stop time
//and calculating time between start and stop

public abstract class TimeStartStop implements Serializable{
	protected static ZoneId zone1 = ZoneId.of("America/New_York");
	protected 	LocalTime StartTime;
	protected LocalTime EndTime;
	private static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("hh:mm:ss a");
	
	public double TotalTime(LocalTime Start, LocalTime Stop) {
		Duration timeElapsed = Duration.between(Start, Stop);
		double hours = ((double)timeElapsed.toMinutes()) / 60;
		return round(hours,1);
	}
	
	//Round function copied from:
	//from https://www.baeldung.com/java-round-decimal-number
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	//formatTime()
	//INTENT: return the time in string format
	public String formatTime(LocalTime time) {
		if (time == null ) {
			return "";
		}
		return time.format(myFormatObj);
	}
	
	//Accessors and Modifiers
	public LocalTime getStartTime() {
		//get the LocalTime form
		return StartTime;
	}
	public String readStartTime() {
		//formats in easy to read string
		return StartTime.format(myFormatObj); 
	}
	public void setStartTime() {
		//starts time
		StartTime = LocalTime.now(zone1);
	}
	public void manualStartTime(LocalTime startTime) {
		//manually set the start time (edit)
		StartTime = startTime;
	}
	public LocalTime getEndTime() {
		//get the LocalTime form of EndTime
		return EndTime;
	}
	public String readEndTime() {
		//formats in easy to read string
		return EndTime.format(myFormatObj); 
	}
	public void setEndTime() {
		//sets the EndTime to current Time
		EndTime = LocalTime.now(zone1);
	}
	public void manualEndTime(LocalTime endTime) {
		//manually set the end time (edit)
		EndTime = endTime;
	}
	
}
