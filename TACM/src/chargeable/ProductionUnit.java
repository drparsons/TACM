

package chargeable;

import java.util.ArrayList;
import chargeable.ProductionLot;


public class ProductionUnit extends ProductionLot{
	//A production of a unit can be of different lots
	//Keeping with Ford Mustang example, imagine its 3rd quarter 2020
	//the production line will be switching from 2020 models to 2021 models
	//this is where ProductionLot's ratio is also important
	public ArrayList<ProductionLot> ArrayLots = new ArrayList<ProductionLot>();
	public String chargeArray[][];
	
	public ProductionUnit() {
		// Default constructor does nothing
	}
	
	public void AddLot(ProductionLot lot) {
			ArrayLots.add(lot);
	}
	
	//Needs work
	
	//TODO
	//insert to array list
	//description OVERRIDE (like production lot but without lot)
	//get charge number w/ratio OVERRIDE (report array w/ ratio)
	//calculate hours? Input is hours, output is charge code with hours per code?
	
	
	
}
