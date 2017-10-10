package employee;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import configuration.Constants;

public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String last_name;
	protected String id;
	protected String tel;
	protected int bankAccount;
	protected int employeeNumber;
	protected Set<Integer> workPlacesId = new LinkedHashSet<Integer>();
	
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
			int bankAccount, 
			int empNumber) throws EmployeeException, IOException
	{
		this.name = inputName;
		this.id = id;
		this.tel = tel;
		this.bankAccount = bankAccount;
		this.employeeNumber = empNumber;
		this.employeesFile = Constants.EMPLOYEE_LIST;
		
	}

	 public Employee(JSONObject keyvalue) {
		// TODO Auto-generated constructor stub
	}

	/** ------------------------------------------------------
	 * Print Employee Object to the console
	 * @return String
	 ------------------------------------------------------- */
	
	@Override
	public String toString() {
		JSONObject employee = new JSONObject();
		
		employee.put("Name", this.name);
		employee.put("Id", this.id);
		employee.put("Tel", this.tel);
		employee.put("EmpId", Integer.toString(this.employeeNumber) );
		employee.put("workPlaceId", this.workPlacesId.toString());
		
		return employee.toJSONString();
	}
	
	// GETERS :
	public String getId () { return this.id; }
	public String getTel () { return this.tel; }
	public int getEmployeeNumber () { return this.employeeNumber; }
	public String getName () { return this.name; }
	public Set<Integer> getAllWorkPlaces () { return this.workPlacesId; }
	
	// SETTERS :
	/** ------------------------------------------------------
	 * Set work place id of employee
	 * @param workId
	 * @throws EmployeeException
	 ------------------------------------------------------ */
	public void setWorkPlaceId(int workId) throws EmployeeException {
		if ( this.workPlacesId.add(workId) ) {}
		else {
			throw new EmployeeException("Failed to add work place to " + this.name);
		};
	}
	
	/** ------------------------------------------------------
	 * Check if the employee exist in the employee list
	 * @return
	 ------------------------------------------------------ */
	
	public boolean isEmployeeExist () {
		File empFile = new File (this.employeesFile);
		
		try {
		    Scanner scan = new Scanner(empFile);

		    while (scan.hasNextLine()) {
		        String line = scan.nextLine();
		        if( line.indexOf(this.id) > -1 ) {
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
	 * 	 -------------------------------------------------------- */
	
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

	/** -------------------------------------------------------- 
	 * Serialize object 
	 * @throws IOException
	 --------------------------------------------------------*/
	public void serialize() throws IOException {
		String fileName;
		FileWriter fw = new FileWriter(this.employeesFile);
		fw.close();
		
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {
			fileName = this.employeesFile.replace(".txt", ".ser");
			fout = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(this);

			System.out.println("Employee Saved");

		} catch (Exception ex) {
			
			ex.printStackTrace();
			
		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	/** ------------------------------------------------------
	 * Identify Employee object by id + randomNumber + name
	 ------------------------------------------------------ */
	@Override
	public int hashCode() {
		Random rnd = new Random();
		int result;
		
		result = rnd.nextInt() + Integer.parseInt(this.id);
		return result; 
	}
	
}
