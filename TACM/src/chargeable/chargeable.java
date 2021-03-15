package chargeable;

import java.io.Serializable;

//parent class for Chargeable information
//not abstract to allow polymorphism on chargeable objects 

public class chargeable implements Serializable{	
	//Description will be name of the charge code
	//example: Overhead Meetings
	protected String description = "";
	
	//Code will be the actual charge code
	//Example: CHG01EN
	protected String code = "";
	
	//Type will be the type of code, in SW it identifies the child class
	//Example: Overhead
	protected String type = "";
	
	//This will be the ratio of the charge code. Default is 100% or 1
	//Example: ProductionUnit is 80%(.8) lot 1 and 20%(.2) lot 2
	protected int ratio = 1;
	
	//constructor
	public chargeable(String description, String code, String type) {
		this.description = description;
		this.code = code;
		this.type = type;
	}
	//constructor
	public chargeable(String description, String code, String type, int Ratio) {
		this.description = description;
		this.code = code;
		this.type = type;
		this.ratio = Ratio;
	}
	//default constructor
	public chargeable() {
		this.description = "No Description Given";
		this.code = "No Code Set";
		this.type = "No Type Set";
	}
	
	
	//accessors and mutators
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChargeCode() {
		return code;
	}
	public void setChargeCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}	
	
	
}




