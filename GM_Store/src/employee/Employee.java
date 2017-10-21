package employee;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import configuration.Constants;

public class Employee {
	
	protected String name;
	protected String last_name;
	protected String id;
	protected String tel;
	protected int storeId;
	protected String password;
	protected int bank;
	protected int employeeNumber;
	
	protected String authFile;
	protected String employeesFile;
	
	/** ======================================================
	 * Employee Constructor
	 * @param inputName
	 * @param id
	 * @param tel
	 * @param bankAccount
	 * @param empNumber
	 * @throws EmployeeException
	 * @throws IOException
	 ==================================================== */
	
	public Employee(
			String inputName, 
			String id, 
			String tel, 
			String password,
			int storeId,
			int bankAccount, 
			int empNumber) throws EmployeeException, IOException
	{
		this.password = password; 
		this.storeId = storeId;
		this.name = inputName;
		this.id = id;
		this.tel = tel;
		this.bank = bankAccount;
		this.employeeNumber = empNumber;
		
		this.employeesFile = Constants.EMPLOYEE_LIST;
		this.authFile = Constants.AUTH_LIST;
		
	}

	/** ------------------------------------------------------
	 * Print Employee Object to the console
	 * @return String
	 ------------------------------------------------------- */
	
	@Override
	public String toString() {
		JSONObject employee = new JSONObject();
		
		employee.put("name", this.name);
		employee.put("id", this.id);
		employee.put("tel", this.tel);
		employee.put("bank", Integer.toString(this.bank));
		employee.put("empId", Integer.toString(this.employeeNumber) );
		employee.put("storeId", Integer.toString(this.storeId));
		
		return employee.toJSONString();
	}
	
	// GETERS :
	public String getId () { return this.id; }
	public String getTel () { return this.tel; }
	public int getEmployeeNumber () { return this.employeeNumber; }
	public String getName () { return this.name; }
	
	// SETTERS :
	
	/**
	 * Check if the employee exist in the employee list
	 * @return
	 * @throws ParseException */
	
	public boolean isEmployeeExist () throws ParseException {
		File empFile = new File (this.employeesFile);
		JSONObject empJson = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		try {
		    Scanner scan = new Scanner(empFile);

		    while (scan.hasNextLine()) {
		        String line = scan.nextLine();
		        empJson = (JSONObject) jsonparser.parse(line);
		        if( empJson.get("id").equals(this.id) ) {
		        		scan.close();
		        		return true;
		        }
		        
		    }
		    scan.close();
		    return false;
		    
		} catch(FileNotFoundException e) { 
			return false;
		}
	}
	
	/** -------------------------------------------------------- 
	 * Save Employee to employees list file
	 * @throws IOException
	 * @throws EmployeeException 
	 * 	 -------------------------------------------------------- 
	 * @throws ParseException */
	
	public String save() throws IOException, EmployeeException, ParseException {
	
		if( !this.isEmployeeExist() ) {
			
			FileWriter fw = new FileWriter(this.employeesFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
				
		    out.println(this.toString());
		    out.close();
		    
		    return "done";
		}
		else {
			return "";
		}
	}
	
	/**
	 * Save Auth information
	 * @throws IOException
	 */
	public void saveToAuth() throws IOException {
		FileWriter fw = new FileWriter(this.authFile, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
	    JSONObject authDate = new JSONObject();
	    
	    authDate.put("id", this.id);
	    authDate.put("password", this.password);
	    
	    out.println(authDate.toString());
	    out.close();
	}
	
	/**
	 * Set Employee id to the next maximum empId
	 * @return
	 */
	public int setEmployeeId() {
		File empFile = new File (this.employeesFile);
		JSONObject empJson = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		int max = 0;
		
		try {
		    Scanner scan = new Scanner(empFile);

		    while (scan.hasNextLine()) {
		        String line = scan.nextLine();
		        empJson = (JSONObject) jsonparser.parse(line);
		        if( Integer.parseInt((String)empJson.get("empId")) > max) {
		        		max = Integer.parseInt((String)empJson.get("empId"));
		        }
		        
		    }
		    scan.close();
		    return ++max;
		    
		} catch(ParseException | FileNotFoundException e) { 
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Update Employee List
	 * @throws IOException
	 * @throws ParseException
	 */
	public void updateEmployeeList() throws IOException, ParseException {
		
		BufferedReader file = new BufferedReader(new FileReader(Constants.EMPLOYEE_LIST));
        StringBuffer inputBuffer = new StringBuffer();
		
        JSONObject currEmployee = new JSONObject();
		JSONParser jsonparser = new JSONParser();
        
		String line;
		
		while ((line = file.readLine()) != null) {
			currEmployee = (JSONObject) jsonparser.parse(line);
			
	    		// find line in the employee list, update and save to temporary buffer
	    		if ( Integer.parseInt((String)currEmployee.get("empId")) == this.employeeNumber ) {		
				inputBuffer.append(this.toString());
				System.out.println(this.toString());
	    		}
	    		else {
	    			inputBuffer.append(line);
	    		}
	        inputBuffer.append('\n');
		}
		
		// convert buffer to string
        String inputStr = inputBuffer.toString();
		
        // close products list
        file.close();
        
        // open products list for writing, and replace the content with temporary buffer
        FileOutputStream fileOut = new FileOutputStream(Constants.EMPLOYEE_LIST);
        fileOut.write(inputStr.getBytes());
        fileOut.close();

	}
	
	
}
