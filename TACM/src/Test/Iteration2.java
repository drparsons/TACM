package Test;

import consoleGUI.frontGUI;
import tasking.Day;

public class Iteration2 {
	
	/*
	 * NOTE! 
	 * Create a New Day first, then you can create a task
	 * You can not edit or stop a task
	 * You can not end the day
	 * No summary or ability to view/edit charge numbers
	 * 
	 * Needs numbers.xml at the same level as the src folder
	 * generates a log file at the same level as the src folder
	 */
	
	public static void main(String[] args) {
		//frontGUI menu = new frontGUI();
		boolean dayStarted = false;
		Day today = null;
		
		boolean valid = false;
		while(!valid ) {
			if (dayStarted) {
				today.dayStatus();
			}
			switch (frontGUI.userInput()) {
			case 1: //New Day
				if (dayStarted) {
					System.out.println("Day Already Started");
				} else {
					today = new Day();
					dayStarted = true;
				}
				break;
			case 2: //New Task
				if (dayStarted) {
					today.createTask();
				} else {
					System.out.println("Day has not started yet");
				}
				break;
			case 3: //Edit Task
				System.out.println("Not implemented yet");
				//new user input similar to start time in Day.createTask()
				//should call Day.logFile()
				break;
			case 4: //Stop Task
				System.out.println("Not implemented yet");
				//plan is to check if task on day yet
				//if tasks in day check if last task is still open
				//then run function to close it
				//similar to edit task this should call Day.logFile()
				break;
			case 5: //End Day
				if (dayStarted) {
					today.setEndTime();
					today.logFile();
					valid = true;
					break;
				} else {
					System.out.println("Day has not started yet");
				}
				break;
			case 6: //Charge Numbers
				System.out.println("Not implemented yet");
				//not only does this show all charge numbers 
				//gives option to add, remove, or edit
				break;
			case 7: //Summary
				System.out.println("Not implemented yet");
				//will generate summary file and display 
				//calculates the hours and charging 
				break;
			case 8: //Close Application
				valid = true;
				break;
			}
			
		}
	}
}
