package employee;

import java.io.IOException;

public class Cashier extends Employee {

	private static final long serialVersionUID = 1L;

	/**
	 * Employee -> Cashier
	 * @param inputName
	 * @param id
	 * @param tel
	 * @param bankAccount
	 * @param empNumber
	 * @throws EmployeeException
	 * @throws IOException
	 */
	public Cashier(
			String inputName, 
			String id, 
			String tel, 
			int bankAccount, 
			int empNumber) throws EmployeeException, IOException
	{
		super(inputName, "317612950", "0548131174", bankAccount, empNumber);
	}
}
