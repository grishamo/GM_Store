package Customer;

public class vipCustomer extends Customer {
	private double discountPercent;
	
	//Constructors:
	public vipCustomer() {
		
	}
	
	public vipCustomer(String firstName, String lastName, long id, long phoneNumber, double discount) {
		super(firstName, lastName, id, phoneNumber);
		setDiscount(discount);
		setStatus();
	}
	
	//Sets:
	public void setPurchasesSum() {
		this.purchasesSum = 0;
	}
	public void setPurchasesCount() {
		this.purchasesCount = 0;
	}
	
	public void setDiscount(double discount) {
		this.discountPercent = discount ;
	}
	
	public void setStatus() {
		this.status = "VIP Customer";
	}
	//============================================================//
	
	//Gets:
	public String getStatus() {
		return this.status;
	}
	
	public double getDiscount() {
		return this.discountPercent;
	}
	
	//============================================================//
	public String toString() {
		return super.toString() + "Customer Status: " + getStatus() + "\nCustomer Discount: " + getDiscount();
	}
};
