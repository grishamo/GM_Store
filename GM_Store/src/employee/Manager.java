package employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Manager extends Employee {

	/**
	 * Employee -> Manager
	 * @param inputName
	 * @param id
	 * @param tel
	 * @param bankAccount
	 * @param empNumber
	 * @throws EmployeeException
	 * @throws IOException
	 */
	public Manager() throws EmployeeException, IOException
	{
		super("","","","",0,0,0);
	}


	public Manager(JSONObject empObj) throws EmployeeException, IOException {
		super( empObj.get("name").toString(), 
			   empObj.get("id").toString(), 
			   empObj.get("tel").toString(), 
			   empObj.get("password").toString(),
			   Integer.parseInt(empObj.get("storeId").toString()),
			   Integer.parseInt(empObj.get("bank").toString()), 
			   Integer.parseInt(empObj.get("empId").toString()));
	}


	@Override
	public String toString() {
		 JSONObject employee = new JSONObject();
		
		employee.put("name", this.name);
		employee.put("id", this.id);
		employee.put("tel", this.tel);
		employee.put("bank", Integer.toString(this.bank));
		employee.put("empId", Integer.toString(this.employeeNumber) );
		employee.put("storeId", Integer.toString(this.storeId));
		employee.put("empType", "manager");
		
		return employee.toString();
	}
	
	/** 
	 * Save Employee to employees list file
	 * @throws IOException
	 * @throws EmployeeException 
	 * @throws ParseException 
	 */
	public String save() throws IOException, EmployeeException, ParseException {
	
		if( !this.isEmployeeExist() ) {
			
			FileWriter fw = new FileWriter(this.employeesFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
				
		    this.employeeNumber = setEmployeeId();
		    
		    out.println(this.toString());
		    out.close();
		    
		    this.saveToAuth();
		    
		}
		else {
			this.updateEmployeeList();
		}
		return "done";
	}
}
