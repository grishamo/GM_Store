package Customer;

public abstract class Customer {
	
	protected String firstName, lastName;
	protected long id, phoneNumber;
	protected double purchasesSum;
	protected int purchasesCount;
	protected String status;

	//Constructors:
	public Customer() {
		
	}
	
	public Customer(String firstName, String lastName, long id, long phoneNumber) {
		setName(firstName, lastName);
		setID(id);
		setPhoneNumber(phoneNumber);
		setPurchasesSum();
		setPurchasesCount();
	}
	//============================================================//
	
	//Sets:
	public void setName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public abstract void setPurchasesSum();
	
	public abstract void setPurchasesCount() ;
	
	public abstract void setStatus();
	//============================================================//
	
	//Gets:
	public String getName() {
		return (this.firstName + this.lastName);
	}
	
	public long getID() {
		return this.id;
	}
	
	public long getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public double getPurchasesSum() {
		return this.purchasesSum;
	}
	
	public int getPurchasesCount() {
		return this.purchasesCount;
	}
	//============================================================//
	
	public String toString() {
		return "New Customer were added!\n";
	}
	
};


