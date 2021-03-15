package tasking;

import tasking.Task;
import chargeable.ChargeNums;
import chargeable.GenericCharge;
import chargeable.Overhead;
import chargeable.ProductionLot;
import chargeable.ProductionUnit;
import chargeable.SpecialProject;
import chargeable.chargeable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.List;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXB;

//A day object will be created for every day the software is run
//A arrayList of tasks keeps track of what tasks were accomplished
//Uses TimeStartStop for tracking time past of the day

public class Day extends TimeStartStop {
	protected ArrayList<Task> taskArray = new ArrayList<Task>();
	protected ArrayList<chargeable> ArrayCHGs = new ArrayList<chargeable>();
	protected LocalDate today;
	static DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	
	//Lambda Methods
	static interface TwoArgOperation {
		boolean operation(String arg0, File arg1);
	}
	static interface OneArgOperation {
		String operation(File arg0);
	}
		
	//constructor
	public Day() {
		setToday();
	}
	
	//Accessors and Modifiers
	public LocalDate getToday() {
		return today;
	}
	public String readToday() {
		return today.format(formatObj);
	}

	public void manualSetToday(LocalDate today, LocalTime startTime) {
		this.today = today;
		this.StartTime = startTime;
	}
	
	//Methods
	
	//addTask method
	//INTENT: Add a task to the taskArray
	//Precondtion: 
	//			newTask needs to be a proper task
	//Postcondtion:
	//			newTask is added to taskArray
	public void addTask(Task newTask) {
		taskArray.add(newTask);
	}
	
	//setToday()
	//INTENT: sets the date and time to today and now
	//		Also imports change numbers through importCHGs
	//		(see importCHGs for pre/post condtions)
	//Precondtion:
	//Postcondtion:
	//		today and StartTime are filled
	//		importCHGs completes or throws IO exception
	public void setToday() {
		//"Starts" the day, attempts to import chargeNums
		this.today = LocalDate.now(this.zone1);
		this.StartTime = getStartTime();
		try {
			importCHGs();
		} catch (IOException e) {
			System.out.println("Error importing charge numbers, none added.");
		}
		recovery();
		
	}
	//chooseTag()
	//INTENT: User chooses a tag from existing tags
	//Process:Loops through all taskArray tags
	//	creates uniqueTag, an array of unique tags
	//	Always an N/A option on uniqueTag
	//	validates input
	//	returns a tag to be used in creation of a new Task
	//PreCondtion:
	//		user interacts properly following prompts
	//		no expected exceptions other than InputMismatchException
	//		System.in is available for a scanner object
	//Postcondtion:
	//		a string is returned of the user chosen tag
	//		scanner is left open to allow other methods to access
	//			System.in
	protected String chooseTag() {
		System.out.println("Enter tag (enter integer option): ");
		ArrayList<String> uniqueTag = new ArrayList<String>();
		uniqueTag.add("N/A");
		for (Task task : taskArray) {
			if (!uniqueTag.contains(task.tag) && task.tag.replaceAll("\\s", "") != "") {
				uniqueTag.add(task.tag);
				System.out.println("Task: " + task.tag);
			}
		}
		for (int i = 0; i < uniqueTag.size(); i++ ) {
			System.out.println((i + 1) + ". " + uniqueTag.get(i));
		}
		
		Scanner inObj = new Scanner(System.in); //input object
		boolean valid = false;
		int userIn = -1;
		while (!valid) {
			try {
				userIn = inObj.nextInt();
				while ((userIn < 1) || (userIn >(uniqueTag.size())) ) {
					System.out.println("Invalid value, enter integer between 1 and " +
							(uniqueTag.size()) +": ");
					userIn = inObj.nextInt();
				}
				valid = true;
			} catch(InputMismatchException ex) {
				System.out.println("Invalid input, enter an integer option: ");
				inObj.nextLine();
			}
			
		}
		//inObj.close();
		return uniqueTag.get(userIn-1);
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
	//Follows Deitel Java textbook section 15.5 XML serialization
	public void importCHGs() throws IOException  {
		try(BufferedReader input = Files.newBufferedReader(Paths.get("numbers.xml"))){
		//unmarshal the files contents
			ChargeNums chargeNums = JAXB.unmarshal(input, ChargeNums.class);
			String tempArray[];
		
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
	}
	
	//chooseCharge()
	//INTENT: User picks the charge number for available list
	//Loops through all ArrayCHGs codes
	//validates input
	//returns a chargeable to be used in creation of a new Task
	//Precondtion:
	//		User enters proper input
	//		System.in is available for a scanner object
	//Postcondtion:
	//		a chargeable object is returned
	//		scanner is left open to allow other methods to access
	//			System.in	
	public chargeable chooseCharge() {
		System.out.println("Enter Charge Code (enter integer option): ");
		for (int i = 0; i < ArrayCHGs.size(); i++ ) {
			System.out.println((i + 1) + ". " + ArrayCHGs.get(i).getDescription());
		}
		
		Scanner inObj = new Scanner(System.in); //input object
		boolean valid = false;
		int userIn = -1;
		while (!valid) {
			try {
				userIn = inObj.nextInt();
				while ((userIn < 1) || (userIn >(ArrayCHGs.size())) ) {
					System.out.println("Invalid value, enter integer between 1 and " +
							(ArrayCHGs.size()) +": ");
					userIn = inObj.nextInt();
				}
				valid = true;
			} catch(InputMismatchException ex) {
				System.out.println("Invalid input, enter an integer option: ");
				inObj.nextLine();
			}
			
		}
		//inObj.close();
		return ArrayCHGs.get(userIn-1);
	}
	
	//userStartTime()
	//INTENT: Asks user to confirm using current time or manually
	//		input a task start time
	//	Throws invalidTask if it is determined that chosen time
	//	conflicts with an existing task
	//Precondtion:
	//		System.in is available for a scanner object
	//Postcondtion:
	//		LocalTime object returned
	public LocalTime userStartTime() throws invalidTask {
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("hh:mm a");
		Scanner inObj = new Scanner(System.in);
		LocalTime userInputTime = null;
		System.out.printf("Hit Enter to set start time of %s%n", 
				LocalTime.now(zone1).format(inputFormat));
		System.out.println("Otherwise enter start time (hh:mm AM/PM): ");
		String rawInput = inObj.nextLine();
		if (rawInput.equalsIgnoreCase("")) {
			return LocalTime.now(zone1);
		} 
		while (true) {
			try {
				userInputTime = LocalTime.parse(rawInput, inputFormat);
				break;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect input follow the format hh:mm AM/PM");
				//inObj.nextLine();
			}
			rawInput = inObj.nextLine();
		}
		//System.out.print(userInputTime.format(myFormatObj));
		if(!taskArray.isEmpty()) {
			for (Task task : taskArray) {
				if (userInputTime.compareTo(task.getEndTime()) < 0) {
					//userInputTime is less than a task's EndTime
					//Means user is trying to create a task during
					//time already taken up
					throw new invalidTask();
				}
			}
		}
		return userInputTime;
		
	}
	
	//INTENT: This creates a new Task item and adds to current Day's
	//array of tasks using input from the user
	//
	//Precondition:
	//				1.Tag is consistent with format of other tags
	//				if tag is "Floor" then it is not entered "floor"
	//				2. Charging description is entered correctly for 
	//				for getChargeable
	//				System.in is available for a scanner object
	//Postcondtion:
	//				A task is successfully created and added to taskArray
	public void createTask() {
		//TODO
		//check latest task in stack
		//ask user: "Task ___ is not over. End Task (Y/N)"
		//If N then "can't create new task until current task end"
		//If Y the currentTask.endTask();
		Scanner scn = new Scanner(System.in);
		boolean valid = false;
		String userIn = "";
		String Description = "";
		chargeable chargeNum = null;
		String  tag = "";
		while (!valid) {
			System.out.println("Enter Task Description: ");
			Description = scn.nextLine();		
			tag = chooseTag();
			System.out.println("Charging: ");
			chargeNum =  chooseCharge();
			System.out.println("Create the following task? (Y/N):");
			System.out.printf("Description: %s%nCharge Number: %s%n"
					+ "Tag: %s%n", Description, chargeNum.getDescription(), tag);
			try {
				userIn = scn.next();
				while (!(userIn.equalsIgnoreCase("y")) && (!(userIn.equalsIgnoreCase("n")))) {
					System.out.println("Invalid value, enter Y or N" );
					userIn = scn.next();
				}
				if (userIn.equalsIgnoreCase("y")) {
					valid = true;
				} else {
					scn.nextLine();
					return;
				}
			} catch(InputMismatchException ex) {
				System.out.println("Invalid input, enter an viable String option: ");
				scn.nextLine();
			}
			
		}
		Task newTask = new Task(Description, chargeNum, tag);
		while(true) {
			try {
				newTask.manualStartTime(userStartTime());
				break;
			} catch (invalidTask e) {
				System.out.println("Invalid Task Time, pick a different time");
			}
		}
		addTask(newTask);
		//scn.close();
		logFile();
	}
	
	//logFile()
	//INTENT: Start thread for Logging that captures the state of the object in a .txt
	//Precondition: Day object has properly created tasks
	//				Day object is correct
	//Postcondtion: Log file created showing the Day's tasks
	//				Object.dat file created storing task information
	public void logFile() {
		
		Logging log = new Logging(this);
		Thread logThread = new Thread(log);
		logThread.start();
 }
		
	//INTENT: Read and store task objects in the Day
	//to be utilized with recovery()
	//precondition
	//		1. FileName exists and is in the local directory
	//		2. If FileName is not in the local directory then
	//		it contains file path information
	//		3. FileName is in the correct format that can be read
	//Postcondtion:
	//		1. Task objects are added to the day
	//Based on Module 4's example
	//TODO: remove debug prints
	//TODO: add the tasks to the day
	public void ObjectReadTest(String FileName) {
        try (ObjectInputStream infile = new ObjectInputStream(new 
                FileInputStream(FileName));)  
        {
        	while (true)
        	{
        		//Debug print
        		//System.out.printf("%s%n", (Task) (infile.readObject()));
        		this.addTask((Task)(infile.readObject()));
			} 
        }  
        catch (EOFException ex) 
        {
	        //Debug print 
        	//System.out.println("EOF reached in objectfile.dat");    
	     } 
        catch (ClassNotFoundException ex)
	     {
	         System.out.println("ClassNotFoundException");
	         ex.printStackTrace();    
	     } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//INTENT: Output current information for day
	 public void dayStatus() {
			System.out.printf("Status for %s%n", readToday());
			System.out.printf("%-25s%-15s%-12s%-12s%5s%n", "Description",
					"Charge Code","Start Time","End Time","Tag");
			for (Task task : taskArray) {
				System.out.printf("%-25s%-15s%-12s%-12s%5s%n", task.getDescription(),
						task.getChargeCode().getChargeCode(),task.formatTime(task.getStartTime()),
						task.formatTime(task.getEndTime()),task.getTag());
			};
	 }

		//Recovery Method
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
	 public void recovery() {
		//Checking what files are in the current local directory (Dir)
			File folder = new File(".");
			File[] listOfFiles = folder.listFiles();
			
			//Stream files to get list of all the objectfiles.dat
			Stream<File> st1 = Stream.of(listOfFiles);
			List<File> objectFiles = new ArrayList<File>();
			objectFiles = st1.filter(s -> s.getName().matches("\\d{2}-\\d{2}-\\d{4} objectfile.dat"))
					.sorted()
					.collect(Collectors.toList());

			//Initialize the String
			String ObjectFileName = "DNE";
			
			//Get today's file using Lambda
			OneArgOperation stripDate = (x) -> x.getName().split(" ")[0];
			TwoArgOperation DateCheck = (x ,y) -> x.equals(stripDate.operation(y));
			for (File file : objectFiles ) {
				if(DateCheck.operation(this.readToday(),file)) {
					ObjectFileName = file.getName();
				};
			}
			
			//System.out.println("File name: " + ObjectFileName);
			System.out.println();
			//Read object
			//System.out.println("Object Serialize Read: ");
			if (ObjectFileName.equals("DNE")) {
				//Object file does not exist (DNE)
				//System.out.println("No object file for today.");
			} else {
				System.out.println("Recovery file found, automatically adding tasks.");
				this.ObjectReadTest(ObjectFileName);
			}
			
	 }
	 
	 //INTENT: Debug method that clears all tasks from the day
	 public void clearDay() {
		 this.taskArray.clear();
	 }
	 
	 public void editTask() {
		 //list all the task (in taskArray)
		 //choose task to edit based off number or return
		 //once number selected create copy in temporary item
		 //ask:
		 //current description:__ Change (Y/N)
		 //current tag: ___ Change (y/n) //use chooseTag is picking new
		 //current charge: task.getChargeCode.(get charge descr), change Y/N
		 //start time: ___ change(Y/N) //similar to set start time
		 // if start time goes before start time of previous task then invalidTask exception
		 //end time: ___ change(Y/N)
		 //if end time is later than current time then invalid task exception
		 //present user temporary item if they are happy (Y/N)
		 //if N then exit (they'll have to come back in)
		 //if Y then replace the task in the task array
		 //if edits to either start and end time adjust adjacent task times accordingly
		 //run logFile() 
	 }
	 
	 public void editCharges() {
		 //list all the charges (in ArrayCHGs)
		 //choose charge number based off number or return to main
		 //once number selected create GenericCharge copy/temp of object choose
		 //current type:__, change(Y/N)
		 //if Y then change type of generic and run convert()
		 //current Descrpition: ___, change(Y/N)
		 //Y: ask questions matching type and use classes modifieries appropriately
		 //current code: __, change(Y/N)
		 //type specific questions 
	 }
	 
	//TODO
	//future iterations will add the additional methods
	//editTask - prompts user to edit a specific task with new inputs
	//report - generates a report of all the completed tasks
	//endTask - ends a specific task
	//logFile - after a task has been ended or edited: modify log file
	
	
	
}
