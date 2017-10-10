package auth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.simple.JSONObject;

import configuration.Constants;

public class Auth {
	
	//Privates
	private String id;
	private String password;
	private String responseStr;
	
	// Constructor: ----------------------------------------------------------------------
	
	public Auth () {
	}
	
	public boolean isAuth (JSONObject signInData) throws FileNotFoundException, AuthExceptions {
		this.id = signInData.get("id").toString();
		this.password = signInData.get("password").toString();
		
		if ( this.isExistInDB( id, password ) ) {
			System.out.println("User Exist!");
			this.responseStr = this.getEmployeeById(id);
			if( responseStr == null ) {
				throw new AuthExceptions("Employee: " + id + " doesn't exist!");
			}
		}
		else {
			throw new AuthExceptions("User doesn't exist!");
		};
		
		return true;
	}
	
	// Methods: ----------------------------------------------------------------------------
	
	/**
	 * Get Employee from EMPLOYEE_LIST By Id
	 * @param id
	 * @return Employee String
	 * @throws FileNotFoundException
	 */
	public String getEmployeeById(String id) throws FileNotFoundException {
		File empFile = new File(Constants.EMPLOYEE_LIST);
		Scanner scanner = new Scanner(empFile);
		
		while (scanner.hasNextLine()) {
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(id) ) { 
				   scanner.close();
			       return lineFromFile;
			   }
			}
		
		scanner.close();
		return null;
	}
	
	
	public boolean isExistInDB(String id, String pass) throws FileNotFoundException {
		File authFile = new File(Constants.AUTH_LIST);
		Scanner scanner = new Scanner(authFile);
		
		while (scanner.hasNextLine()) {
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(id) && lineFromFile.contains(pass) ) { 
				   scanner.close();
			       return true;
			   }
			}
		
		scanner.close();
		return false;
	}
	
	
	public String getResponse() {
		return this.responseStr;
	}

	
}
