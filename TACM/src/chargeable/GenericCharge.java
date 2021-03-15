package chargeable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.bind.JAXB;

public class GenericCharge<T> extends chargeable {
//Generic class for chargeables 
//This reduces the amount of casting required	
		
	public GenericCharge(chargeable myObj) {
		this.code = myObj.code;
		this.description = myObj.description;
		this.ratio = myObj.ratio;
		this.type=myObj.type;
	}
	
	//INTENT: Take a default parent chargeable object
	//and build a specific child object based off type
	//Precondtions
	//	1. The chargeable object used in the GenericCharge
	//     is properly made so as not to pose runtime errors
	//PostCondion:
	//	1. A chargeable object or chargeable child is returned
	// using the parameters of this GenericCharge object
	public chargeable convert() {
		chargeable child = null;
		switch(this.getType()) {
		case ("Overhead"):
			child = new Overhead(this.getDescription(), this.getChargeCode());
			return(child);
		case("Production"): 
			String[] tempArray = this.getDescription().split(" ");
			if (tempArray.length == 4) {
			child = new ProductionLot(this.getChargeCode(), tempArray[0],
					tempArray[1], tempArray[3], this.getRatio());
				return(child);
			}
			child = new ProductionLot(this.getChargeCode(), this.getDescription(),
						this.getType(), this.getRatio());
			return(child);
		case("Project"):
			String tempDescription = this.getDescription().substring(this.getDescription().indexOf(' ')+1);
			String tempProgram = this.getDescription().substring(0,this.getDescription().indexOf(' '));
			child = new SpecialProject(tempDescription, this.getChargeCode(), tempProgram);
			return(child);
		default:
			return this;
		}//end switch
		
	}
	public static void main(String[] args) {
		
		//Testing the convert method as intended to be used
		//within the Day.java class
		ArrayList<chargeable> ArrayCHGs = new ArrayList<chargeable>();
		try(BufferedReader input = Files.newBufferedReader(Paths.get("numbers.xml"))){
			System.out.println("Importing from numbers.xml...");
			//unmarshal the files contents
			ChargeNums chargeNums = JAXB.unmarshal(input, ChargeNums.class);

			//Fills ArrayCHGs with specific objects
			for (chargeable number : chargeNums.getChargeNum()) {
				//creates a Generic object
				GenericCharge<chargeable> converted = new GenericCharge<chargeable>(number);		
				//calls the convert() method to return the proper chargeable object
				number = converted.convert();
				ArrayCHGs.add(number);
			}//end for loop
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error opening file.");
		}
		System.out.println("List of charge numbers from XML and object instance type:");
		String type = "";
		for (chargeable code : ArrayCHGs) {
			//This test that the objects are now instances of the child class
			//instead of all just being chargeable objects
			System.out.print(code.getDescription());
			if (code instanceof SpecialProject) {
				type = "Special project";
			} else if (code instanceof Overhead) {
				type = "Overhead";
			} else if (code instanceof ProductionLot) {
				type = "ProductionLot";
			} else {
				type = "Non-standard type: " + code.getType();
			}
			System.out.printf(" - %s%n", type);
		}
	}
	
}
