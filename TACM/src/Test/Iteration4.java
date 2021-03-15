package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chargeable.Overhead;
import chargeable.ProductionLot;
import chargeable.ProductionUnit;
import chargeable.SpecialProject;
import chargeable.chargeable;
import tasking.Day;
import tasking.Task;

public class Iteration4 {
	static interface TwoArgOperation {
		boolean operation(String arg0, File arg1);
	}
	static interface OneArgOperation {
		String operation(File arg0);
	}
	
	public static void main(String[] args) {
		//create charge codes
		//Overhead(String description, String code)
		chargeable training = new Overhead("Training","CHGTRN");
		chargeable meetings = new Overhead("Meetings","CHGMTG");
		//ProductionLot(String code,  String Program, String Unit, String LotNum, int ratio)
		chargeable must20 = new ProductionLot("CHGMS20", "Ford", "Mustang", "20", 2);
		chargeable must21 = new ProductionLot("CHGMS21", "Ford", "Mustang", "21", 8);
		chargeable tooling = new SpecialProject("Tooling", "CHGTOOL", "Ford");
		chargeable mustang = new ProductionUnit();
		
		//Manually creating Day object vs using frontGUI.java
		Day dayObj = new Day();
		//clears out any tasks inserted by recovery
		dayObj.clearDay();
		System.out.println("Day cleared of any auto-recovered tasks for demonstration.");
		
		//Manual Task Creation and get example
		//Task(String Description, chargeable Code, String tag)
		System.out.println("Creating example tasks for testing object writing");
		Task task1 = new Task("Morning Stand up", meetings, "");
		Task task2 = new Task("Production Floor", must20, "Floor"); //mustang, "Floor");
		task1.setStartTime();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
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
		

		//Add our tasks to our Day Object dayObj
		dayObj.addTask(task1);
		dayObj.addTask(task2);
		
		//Call logFile
		//Creates a text file for user readability
		//and new for iteration 4 creates a binary object output stream file 
		//for ease in software recovery instead of trying to parse
		//the user text file
		dayObj.logFile();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("Log files created. Attempting to read it back and add to current day.");
		System.out.println("(Expecting 2 repeating entries as recovery will add recovered task to today)");
		//Recovery Method - Prototype
		//Intent: Recovery of Tasks from objectdata file
		//Precondtion:
		//			1. If objectdata file exists for today's date it
		//			contains proper Task object information
		//			2. ObjectReadTest(String) inside Day.java exists
		//Postcondtion:
		//			1. If no objectdata file exists for today's date
		//			nothing is imported
		//			2. If objectdata file exists, then Task object
		//			information is read to the user
		//TODO: Make formal method inside Day.java
		/*
		 * Called inside Day.java when logFile()
		 * Because of that and the two tasks are already added
		 * we expect to see 4 tasks total
		 */
		//Checking what files are in the current local directory (Dir)
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();
		
		//Stream files to get list of all the objectfiles.dat
		Stream<File> st1 = Stream.of(listOfFiles);
		List<File> objectFiles = new ArrayList<File>();
		objectFiles = st1.filter(s -> s.getName().matches("\\d{2}-\\d{2}-\\d{4} objectfile.dat"))
				.sorted()
				.collect(Collectors.toList());
		//Debug Stream
		//System.out.println("Debug Stream showing sorted objectfile in local Dir:");
		/*st1.filter(s -> s.getName().matches("\\d{2}-\\d{2}-\\d{4} objectfile.dat"))
			.sorted()
			.forEach(s -> System.out.printf("%s%n",s.getName()));
		*/
		
		//Initialize the String
		String ObjectFileName = "DNE";
		
		//Get today's file using Lambda
		OneArgOperation stripDate = (x) -> x.getName().split(" ")[0];
		TwoArgOperation DateCheck = (x ,y) -> x.equals(stripDate.operation(y));
		for (File file : objectFiles ) {
			if(DateCheck.operation(dayObj.readToday(),file)) {
				ObjectFileName = file.getName();
			};
		}
		
		System.out.println("File name: " + ObjectFileName);
		System.out.println();
		//Read object
		//TODO: modify dayObj.ObjectReadTest to add the tasks to the Day instance
		System.out.println("Object Serialize Read: ");
		if (ObjectFileName.equals("DNE")) {
			//Object file does not exist (DNE)
			System.out.println("No object file for today.");
		} else {
			dayObj.ObjectReadTest(ObjectFileName);
			dayObj.dayStatus();
		}
		
		
	}//end main

}
