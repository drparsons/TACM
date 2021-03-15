package chargeable;

public class SpecialProject extends chargeable {
	//Adds more definition to chargeable parent class
	protected String Program;

	//Constructors
	public SpecialProject() {
		super();
	}
	public SpecialProject(String description, String code, String program) {
		super(description, code,"Project");
		this.Program = program;
	}
	
	//Accessors and Modifiers
	public String getProgram() {
		return Program;
	}
	public void setProgram(String program) {
		Program = program;
	}
	public String getDescription() {
		return this.Program + " " + this.description;
	}
	
	
}
