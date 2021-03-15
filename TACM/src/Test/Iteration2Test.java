package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import chargeable.ProductionLot;
import tasking.Day;
import tasking.Task;
import tasking.TimeStartStop;

class Iteration2Test {

	
	@Test
	void Tasktest() {
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("hh:mm a");
		LocalTime time1 = LocalTime.parse("08:00 PM", inputFormat);
		LocalTime time2 = LocalTime.parse("08:30 PM", inputFormat);
		TimeStartStop tester = new Task();
		//Test TotalTime() using the Task class
		assertEquals(tester.TotalTime(time1, time2),0.5);
		//Test more involved getters/setters
		tester.manualEndTime(time2);
		assertEquals(time2,tester.getEndTime());
		tester.manualStartTime(time1);
		assertEquals(time1,tester.getStartTime());
	}
	
	@Test
	void Daytest() {
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("hh:mm a");
		LocalTime time1 = LocalTime.parse("08:00 PM", inputFormat);
		LocalTime time2 = LocalTime.parse("08:30 PM", inputFormat);
		TimeStartStop tester = new Day();
		//Test TotalTime() using the Day class
		assertEquals(tester.TotalTime(time1, time2),0.5);
		//Test more involved getters/setters
		tester.manualEndTime(time2);
		assertEquals(time2,tester.getEndTime());
		tester.manualStartTime(time1);
		assertEquals(time1,tester.getStartTime());
	}
	@Test
	void ProductionLotTest() {
		ProductionLot tester = new ProductionLot();
		String program = "program";
		String unit = "unit";
		String lotNum = "lotNum";
		String combined = program + unit + "Lot" + lotNum;
		//Testing more involved getter/setter
		tester.setDescription(program, unit, lotNum);
		assertEquals(combined,tester.getDescription());
	}

	
	
}
