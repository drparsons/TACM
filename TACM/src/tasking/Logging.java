package tasking;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Formatter;

public class Logging implements Runnable {
		Day logDay = null;
		
		//Constructor
		public Logging(Day logDay) {
			super();
			this.logDay = logDay;
		}
		
		@Override
		public void run() {
			System.out.println("Starting logging...");
			logFile(logDay);
		}
		
		//logFile()
		//INTENT: capture the state of the object in a .txt and .dat
		//Formatter: Create and write to logfile of task information
		//Precondition: Day object has properly created tasks
		//				Day object is correct
		//Postcondtion: Log file created showing the Day's tasks
		//				.dat object data file storing task information
		//Follows Module 2 notes example
		//Object recovery file follows Module 4's example
		//TODO change to robust for loop through Day's tasks (handle ProductionUnit)
		//TODO preface log with more Day object information 
		private void logFile(Day d) {

			Formatter outfile = null;
			boolean flag = true;
			try {
				//open file
				outfile = new Formatter(d.readToday() +" Log File.txt");
				outfile.format("Log File for %s%n", d.readToday());
				outfile.format("%-25s%-15s%-12s%-12s%5s%n", "Description",
						"Charge Code","Start Time","End Time","Tag");
				for (Task task : d.taskArray) {
					outfile.format("%-25s%-15s%-12s%-12s%5s%n", task.getDescription(),
							task.getChargeCode().getChargeCode(),task.formatTime(task.getStartTime()),
							task.formatTime(task.getEndTime()),task.getTag());
				};
			} catch (FileNotFoundException ex) {
				flag = false;
				System.err.println("Error opening file.");
			}
			outfile.close();
			//Create recovery file
		    try (ObjectOutputStream outfileObj = 
		    		new ObjectOutputStream(new FileOutputStream(d.readToday() + " objectfile.dat"));)       
		    	{
		        	 for (Task task : d.taskArray) {
		        		 outfileObj.writeObject(task);
		        		 //debug
		        		 //System.out.println("Logged: " + task);
		        	 }
		        }
		     catch (FileNotFoundException ex)
		     {
		    	 flag = false;
		         System.out.println("FileNotFoundException"); 
		         ex.printStackTrace();   
		     }

		     catch (IOException ex)
		     {
		    	 flag = false;
		         System.out.println("IOException");
		         ex.printStackTrace();    
		     }
		    if (flag) {
		    	System.out.println("Done Logging");
		    } else {
		    	System.out.println("Error in logging.");
		    }
		    

	 }
			
		
}
