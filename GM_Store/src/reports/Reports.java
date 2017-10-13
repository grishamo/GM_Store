package reports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
	
	public String updateProductQuantity(JSONObject product) throws ParseException, IOException {
		
		BufferedReader file = new BufferedReader(new FileReader(Constants.PRODUCTS_LIST));
        StringBuffer inputBuffer = new StringBuffer();
        
        JSONObject currProduct = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		int prodQuantity = Integer.parseInt( (String) product.get("quantity"));
		int currQuantity;
		
        String storeId = (String) product.get("storeId");
        String prodName = (String) product.get("name");
        String line;

        while ((line = file.readLine()) != null) {
        	
        		if ( line.contains(prodName) && line.contains("\"" + storeId + "\"") ) {
       
				currProduct = (JSONObject) jsonparser.parse(line);
				currQuantity = Integer.parseInt((String) currProduct.get("quantity") );
				currProduct.put("quantity", String.valueOf(currQuantity-prodQuantity));
				inputBuffer.append(currProduct.toJSONString());
				System.out.println(currProduct.toJSONString());
        		}
        		else {
        			inputBuffer.append(line);
        		}
        			
            inputBuffer.append('\n');
        }
        String inputStr = inputBuffer.toString();
		
        file.close();
        
        FileOutputStream fileOut = new FileOutputStream(Constants.PRODUCTS_LIST);
        fileOut.write(inputStr.getBytes());
        fileOut.close();
        
        return "done";
 
	}
	
}
