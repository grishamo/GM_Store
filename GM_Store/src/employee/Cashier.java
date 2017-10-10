package employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;

public class Cashier extends Employee {

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


	@Override
	public String toString() {
		 JSONObject employee = new JSONObject();
		
		employee.put("Name", this.name);
		employee.put("Id", this.id);
		employee.put("Tel", this.tel);
		employee.put("EmpId", Integer.toString(this.employeeNumber) );
		employee.put("workPlaceId", this.workPlacesId.toString());
		employee.put("empType", "cashier");
		
		return employee.toString();
	}
	
	/** 
	 * Save Employee to employees list file
	 * @throws IOException
	 * @throws EmployeeException 
	 */
	public void save() throws IOException, EmployeeException {
	
		if( !this.isEmployeeExist() ) {
			
			FileWriter fw = new FileWriter(this.employeesFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
				
		    out.println(this.toString());
		    out.close();
		    
		}
		else {
			throw new EmployeeException("Employee allready exist!");
		}
	}
}
