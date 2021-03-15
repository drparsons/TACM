package chargeableTest;
//David Parsons 

/*
 * 
 * 
 * Disregard this is playground
 * used for development and testing functions
 * 
 * 
 * 
 * 
 * 
 */

//import classes
import chargeable.chargeable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

import chargeable.Overhead;
import chargeable.ProductionLot;
import chargeable.ProductionUnit;
import chargeable.SpecialProject;
import tasking.TimeStartStop;
import tasking.Day;
import tasking.Task;
import chargeable.ChargeNums;
import javax.xml.bind.JAXB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Iteration1Test {
	//create charge codes
	//Overhead(String description, String code)
	public static chargeable training = new Overhead("Training","CHGTRN");
	public static chargeable meetings = new Overhead("Meetings","CHGMTG");
	//ProductionLot(String code,  String Program, String Unit, String LotNum, int ratio)
	public static chargeable must20 = new ProductionLot("CHGMS20", "Ford", "Mustang", "20", 2);
	public static chargeable must21 = new ProductionLot("CHGMS21", "Ford", "Mustang", "21", 8);
	public static chargeable tooling = new SpecialProject("Tooling", "CHGTOOL", "Ford");
	public static chargeable mustang = new ProductionUnit();
	
	
	public static void main(String[] args) {

		
		// manually adding ProductionLots for this example
		//Having to downcast to be able to add correctly
		((ProductionUnit)mustang).ArrayLots.add((ProductionLot) must20);
		((ProductionUnit)mustang).ArrayLots.add((ProductionLot) must21);
		((ProductionUnit)mustang).setDescription("Mustang");
		//creating ArrayList of above objects for looping
		ArrayList<chargeable> ArrayCHGs = new ArrayList<chargeable>();
		ArrayCHGs.add(training);
		ArrayCHGs.add(meetings);
		//ArrayCHGs.add(must20); //contained in mustang ProductionUnit
		//ArrayCHGs.add(must21); //contained in mustant ProductionUnit
		ArrayCHGs.add(mustang);
		ArrayCHGs.add(tooling);
		
		//Polymorphism of getDescription()
		System.out.println("Description of Charge Codes");
		for (chargeable code : ArrayCHGs) {
			System.out.println(code.getDescription());
		}
		
		//Downcasting for ProductionUnit
		System.out.println("\nCharge Codes of objects");
		for (chargeable code : ArrayCHGs) {
			if (code instanceof ProductionUnit) {
				for (ProductionLot loopLots :  ((ProductionUnit)code).ArrayLots) {
					System.out.println(loopLots.getChargeCode());
					System.out.println("split!!");
					for (String bit : loopLots.getDescription().split(" ")) {
						System.out.println(bit);
					}
					
				}
			} else {
				System.out.println(code.getChargeCode());
			}//end if
			if (code instanceof SpecialProject) {
				System.out.println("Special!");
				//Description
				System.out.println(code.getDescription().substring(code.getDescription().indexOf(' ')+1));
				//Program
				System.out.println(code.getDescription().substring(0,code.getDescription().indexOf(' ')));
				
			}
		}//end for
		
		
		//Task Creation and get example
		//Task(String Description, chargeable Code, String tag)
		Task task1 = new Task("Morning Stand up", meetings, "");
		Task task2 = new Task("Production Floor", must20, "Floor"); //mustang, "Floor");
		task1.setStartTime();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task1.setEndTime();
		task2.setStartTime();
		System.out.println("\nTask1 - " + task1.getTag());
		System.out.println(task1.getDescription() + " " + task1.getChargeCode().getDescription() +
				" " + task1.getStartTime() + " " + task1.getEndTime());
		System.out.println("\nTask2 - " + task2.getTag());
		System.out.println(task2.getDescription() + " " + task2.getChargeCode().getDescription() +
				" " + task2.getStartTime() + " " + task2.getEndTime());
		
		//XML Writer of chargeable objects.
		//Precondition: ArrayCHGs is an ArrayList filled with chargeable objects
		//Postcondtion:
		//  1. numbers.xml is created and stores the contents of ArrayCHGs
		//		Note: ProductionUnit is broken into ProductionLot level to
		//			properly store information
		//	2. numbers.xml is open and unable to write thus IOException is generated
		//			"Error Opening File." is printed.
		//TODO write to file "ratio" parameter for ProductionLot chargeable objects
		//Follows Deitel Java textbook section 15.5 XML serialization
		try(BufferedWriter output = Files.newBufferedWriter(Paths.get("numbers.xml"))) {
			ChargeNums chargeNums = new ChargeNums(); 
			for (chargeable code : ArrayCHGs) {
				if (code instanceof ProductionUnit) {
					for (ProductionLot loopLots :  ((ProductionUnit)code).ArrayLots) {
						chargeNums.getChargeNum().add(loopLots);
					}
				} else {
						chargeNums.getChargeNum().add(code);
				}//end if
				
			}		
			//write ChargeNums's XML to output
			JAXB.marshal(chargeNums, output);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Error Opening File.");
		}

		//XML Reader of chargeable objects.
		//Precondition: 
		//	1. "numbers.xml" exists and is properly filled with chargeable
		//		object information
		//	2. "numbers.xml" is stored next to project src level
		//Postcondtion:
		//  1. Contents of "numbers.xml" is stored in chargeable objects 
		//		and printed out on the console 
		//	2. If preconditions not met, IOException thrown and "Error
		//		opening file." is printed on console
		//TODO based off type of chargeable in XML then store in more
		//appropriate object and build ProductionUnit code
		//Follows Deitel Java textbook section 15.5 XML serialization
		try(BufferedReader input = Files.newBufferedReader(Paths.get("numbers.xml"))){
			//unmarshal the files contents
			ChargeNums chargeNums = JAXB.unmarshal(input, ChargeNums.class);
			
			for (chargeable number : chargeNums.getChargeNum()) {
				System.out.printf("%s%n%s%n%s%n%n", number.getDescription(),
						number.getChargeCode(), number.getType());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error opening file.");
		}
	
		//Formatter: Create and write to logfile of task information
		//Precondition: Day object has properly created tasks
		//Postcondtion: Log file created showing the Day's tasks
		//Follows Module 2 notes example
		//TODO change to robust for loop through Day's tasks (handle ProductionUnit)
		//TODO preface log with Day object information 
		//TODO get today's date from Day object instead of writing
		Formatter outfile = null;
		try {
			//Gets and set the date in the correct format
			ZoneId zone1 = ZoneId.of("America/New_York");
			DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy");
			LocalDate today = LocalDate.now(zone1);
			//open file
			outfile = new Formatter(today.format(formatObj) +" Log File.txt");
			outfile.format("Log File for %s%n", today.format(formatObj));
			outfile.format("%-25s%-15s%-12s%-12s%5s%n", "Description",
					"Charge Code","Start Time","End Time","Tag");
			outfile.format("%-25s%-15s%-12s%-12s%5s%n", task1.getDescription(),
					task1.getChargeCode().getChargeCode(),task1.formatTime(task1.getStartTime()),
					task1.formatTime(task1.getEndTime()),task1.getTag());
			outfile.format("%-25s%-15s%-12s%-12s%5s%n", task2.getDescription(),
					task2.getChargeCode().getChargeCode(),task2.formatTime(task2.getStartTime()),
					task2.formatTime(task2.getEndTime()),task2.getTag());
		} catch (FileNotFoundException ex) {
			System.err.println("Error opening file.");
		}
		outfile.close();
		
		
	}//end main

}//end class
