package reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	/**
	 * Get All Customers
	 * @return
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public String getAllCustomers() throws ParseException, FileNotFoundException {
		File empFile = new File(Constants.CUSTOMERS_LIST);
		Scanner scanner = new Scanner(empFile);
		JSONObject returnObj = new JSONObject();
		JSONObject customer = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
			   String lineFromFile = scanner.nextLine();
			   customer = (JSONObject) jsonparser.parse(lineFromFile);
			   returnObj.put( (String) customer.get("id"), customer);
			}
		
		scanner.close();
		return returnObj.toString();
	}
	
	/**
	 * Get All Products by Store Id
	 * @param storeId
	 * @return
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public String getProductsByStore(String storeId) throws FileNotFoundException, ParseException {
		File empFile = new File(Constants.PRODUCTS_LIST);
		Scanner scanner = new Scanner(empFile);
		JSONObject returnObj = new JSONObject();
		JSONObject product = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
		   String lineFromFile = scanner.nextLine();
		   product = (JSONObject) jsonparser.parse(lineFromFile);
		   String currStore = (String) product.get("store");
		   String quantity = (String) product.get("quantity");
		   
		   if ( currStore.equals(storeId) && !quantity.equals("0") ) {
			   returnObj.put( (String) product.get("name"), product);
		   }  
		}
		
		scanner.close();
		return returnObj.toString();
	}
	
}
