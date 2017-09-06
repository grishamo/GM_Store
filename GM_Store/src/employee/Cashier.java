package employee;

public class Cashier extends Employee {
	
	public Cashier(
			String inputName, 
			long id, 
			long tel, 
			int bankAccount, 
			int empNumber) throws EmployeeException
	{
		super(inputName, id, tel, bankAccount, empNumber);
	}
}
