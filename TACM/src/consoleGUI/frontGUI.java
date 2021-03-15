package consoleGUI;

import java.util.InputMismatchException;
import java.util.Scanner; //import the scanner class

public class frontGUI {

	//Prints out the standard front panel
	public static void printOptions() {
		System.out.println("--TACM: Task and Charging Manager--");
		System.out.println("1. New Day"); //add option to "grey out" day if day started
		System.out.println("2. New Task");
		System.out.println("3. Edit Task");
		System.out.println("4. Stop Task");
		System.out.println("5. End Day");
		System.out.println("6. Charge Numbers");
		System.out.println("7. Summary");
		System.out.println("8. Close Application");
	}

	public static int userInput() {
		Scanner inObj = new Scanner(System.in); //input object
		boolean valid = false;
		int userIn = -1;
		printOptions();
		System.out.println("Enter integer option: ");
		while (!valid) {
			try {
				userIn = inObj.nextInt();
				while ((userIn < 1) || (userIn >8) ) {
					printOptions();
					System.out.println("Invalid value, enter integer between 1 and 8: ");
					userIn = inObj.nextInt();
				}
				valid = true;
			} catch(InputMismatchException ex) {
				printOptions();
				System.out.println("Invalid input, enter an integer option: ");
				inObj.nextLine();
			}
			
		}
		//inObj.close();
		return userIn;
	}
	
	
	public static void main(String[] args) {
		int testInt = userInput();
		System.out.println("You choose: " + testInt);
	}
	
}
