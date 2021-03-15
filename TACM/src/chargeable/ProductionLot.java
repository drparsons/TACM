package chargeable;



public class ProductionLot extends chargeable {
	//Special type of chargeable
	//A car example would be 2021 Ford Mustang
	//2021 would be the LotNum
	//Ford would be the Program
	//Mustang would be the Unit
	//Ratio would 100% because by Feburary they are no longer
	//producing 2020 variant
	protected String LotNum;
	protected String Program;
	protected String Unit;
	
	// default constructor
	public ProductionLot() {
		super();
		LotNum = "";
		Program = "";
		Unit = "";
	}
	//constructor
	public ProductionLot(String code,  String Program, String Unit, String LotNum, int ratio) {
	super(Program + " " + Unit + " Lot " + LotNum, code,"Production", ratio);
	this.LotNum = LotNum;
	this.Program = Program;
	this.Unit = Unit;
	}

	public ProductionLot(String chargeCode, String description, String type, int ratio) {
		super(description, chargeCode, type, ratio);
	}
	//Accessors and Modifiers
	public void setDescription(String Program, String Unit, String LotNum) {
		// The descrition is a combination of Program, Unit, and LotNum
		this.LotNum = LotNum;
		this.Program = Program;
		this.Unit = Unit;
		super.setDescription(Program + Unit + "Lot" + LotNum);
	}
	public String getDescription() {
		return this.description;
	}
	public String getLotNum() {
		return LotNum;
	}
	public void setLotNum(String lotNum) {
		this.LotNum = lotNum;
		//Since lot number changed the description will change
		this.setDescription(this.Program, this.Unit, lotNum);
	}
	public String getProgram() {
		return Program;
	}
	public void setProgram(String program) {
		Program = program;
		//Since program  changed the description will change
		this.setDescription(program, this.Unit, this.LotNum);
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
		//Since unit changed the description will change
		this.setDescription(this.Program, unit, this.LotNum);
	}


	
	

}
