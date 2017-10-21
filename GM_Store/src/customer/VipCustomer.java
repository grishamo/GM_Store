package customer;

import org.json.simple.JSONObject;

public class VipCustomer extends Customer {
	
	//Constructors:
	public VipCustomer() {}
	
	public VipCustomer(String firstName, long id, long phoneNumber ) {
		super(firstName, id, phoneNumber);
		setStatus();
	}
	
	public VipCustomer(JSONObject newcust ) {
		this( (String)newcust.get("name"), 
			  Long.parseLong((String)newcust.get("id")), 
			  Long.parseLong((String)newcust.get("tel")));
	}
	
	//============================================================//
	
	public void setStatus() {
		this.status = "vip";
	}
	
	
	//Gets:
	public String getStatus() {
		return this.status;
	}
	
	//============================================================//
	@Override
	public String toString() {
		JSONObject employee = new JSONObject();
		
		employee.put("name", this.name);
		employee.put("id", Long.toString(this.id));
		employee.put("tel", Long.toString(this.tel));
		employee.put("sumOfDeals", Double.toString(this.sumOfDeals));
		employee.put("numOfDeals", Integer.toString(this.numOfDeals));
		employee.put("status", this.status);
		
		return employee.toJSONString();
	}
};
