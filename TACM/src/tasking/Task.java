package tasking;


import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import chargeable.chargeable;

//Task is what the user will create multiple times a day
//It contains a chargeable item as well as a description
//A tag is used for sorting tasks by host main program
//TimeStartStop is used for calculating the time of task.

public class Task extends TimeStartStop implements Serializable {
	String Description;
	chargeable ChargeCode;
	String tag;
	
	//constructor
	public Task(String Description, chargeable Code, String tag) {
		this.Description = Description;
		this.ChargeCode = Code;
		this.tag = tag;
	}
	
	public Task() {
	}
	
	@Override
	public String toString() {
		return String.format("%-25s%-15s%-12s%-12s%5s%n", this.getDescription(),
				this.getChargeCode().getChargeCode(),this.formatTime(this.getStartTime()),
				this.formatTime(this.getEndTime()),this.getTag());
	}

	//accessors and modifiers
	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public chargeable getChargeCode() {
		return ChargeCode;
	}

	public void setChargeCode(chargeable chargeCode) {
		ChargeCode = chargeCode;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	//method
	

	
	
	
	
}
