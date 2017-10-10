package reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import configuration.Constants;
import employee.EmployeeException;

public class Reports {

	/**
	 * Constructor
	 */
	public Reports() {}
	
	
	/**
	 * Get Employee by Id from db_files/employees
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws EmployeeException
	 */
	public String getEmployeeById( String id ) throws FileNotFoundException, EmployeeException {
		File empFile = new File(Constants.EMPLOYEE_LIST);
		Scanner scanner = new Scanner(empFile);
		
		while ( scanner.hasNextLine() ) {
			   String lineFromFile = scanner.nextLine();
			   if(lineFromFile.contains(id) ) { 
				   scanner.close();
			       return lineFromFile;
			   }
			}
		
		scanner.close();
		throw new EmployeeException("Employee not found");
	}
	
}
