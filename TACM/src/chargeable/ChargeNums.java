package chargeable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ChargeNums {

	//From textboox section 15.4 Sequential Text Files
	//@XmlElement specifies XML element name for each object in the list
	@XmlElement(name="chargeNum")
	private List<chargeable> chargeNums = new ArrayList<>();
	
	//getter: returns the List<chargeable>
	public List<chargeable> getChargeNum() {return chargeNums;}
	
}
