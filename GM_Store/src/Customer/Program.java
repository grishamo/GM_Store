package Customer;

public class Program {
	
	public static void main (String[] args) {
		
		newCustomer c = new newCustomer("masha", "nasha" , 123, 111);
		vipCustomer v = new vipCustomer("jojo", "neli", 333, 777, 25.7);
		
		System.out.println("Congrads!!! \n" + c.toString());
		System.out.println("Congrads!!! \n" + v.toString());
	}
}
