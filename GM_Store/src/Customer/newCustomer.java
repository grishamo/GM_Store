package Customer;


public class newCustomer extends Customer {
	
	//Constructors:
	public newCustomer() {
		
	}
	
	public newCustomer(String firstName, String lastName, long id, long phoneNumber) {
		super(firstName, lastName, id, phoneNumber);
		setStatus();
	}
	
	//============================================================//
	
	//Sets:
	public void setPurchasesSum() {
		this.purchasesSum = 0;
	}
	
	public void setPurchasesCount() {
		this.purchasesCount = 0;
	}
	
	public void setStatus() {
		this.status = "New Customer";
	}
	//============================================================//
	
	//Gets:
	public String getStatus() {
		return this.status;
	}
	//============================================================//
	public String toString() {
		return super.toString() + "Customer status: " + getStatus();
	}
};
