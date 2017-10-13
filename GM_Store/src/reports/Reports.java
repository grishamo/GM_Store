package reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
	 * @throws ParseException 
	 */
	public String getEmployeeById( String id ) throws FileNotFoundException, EmployeeException, ParseException {
		File empFile = new File(Constants.EMPLOYEE_LIST);
		Scanner scanner = new Scanner(empFile);
		JSONObject employee = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
			   String lineFromFile = scanner.nextLine();
			   employee = (JSONObject) jsonparser.parse(lineFromFile);
			   
			   if( employee.get("id").equals(id) ) { 
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
		   int quantity = Integer.parseInt((String) product.get("quantity"));
		   
		   if ( currStore.equals(storeId) && quantity > 0 ) {
			   returnObj.put( (String) product.get("name"), product);
		   }  
		}
		
		scanner.close();
		return returnObj.toString();
	}
	
	/**
	 * Update product quantity inside products list
	 * @param product
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String updateProductQuantity(JSONObject product) throws ParseException, IOException {
		
		// Open product file and temporary buffer
		BufferedReader file = new BufferedReader(new FileReader(Constants.PRODUCTS_LIST));
        StringBuffer inputBuffer = new StringBuffer();
        
        JSONObject currProduct = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		// Get Product Data from function argument
		int prodQuantity = Integer.parseInt( (String) product.get("quantity"));
        String storeId = (String) product.get("storeId");
        String prodName = (String) product.get("name");
        
        String line;
        int currQuantity;

        while ((line = file.readLine()) != null) {
        		
        		// find line in the products list, update and save to temporary buffer
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
        // convert buffer to string
        String inputStr = inputBuffer.toString();
		
        // close products list
        file.close();
        
        // open products list for writing, and replace the content with temporary buffer
        FileOutputStream fileOut = new FileOutputStream(Constants.PRODUCTS_LIST);
        fileOut.write(inputStr.getBytes());
        fileOut.close();
        
        return "done";
 
	}
	
	
	/**
	 * Update Employee sale
	 * @param product
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public String updateEmployeeInfo(JSONObject product) throws ParseException, NumberFormatException, IOException {
		String empId = (String) product.get("empId");
		double prodCost = Double.parseDouble((String) product.get("prodCost"));
		
		//return if employee id is undefined
		if( empId.equals("0") || empId.equals("") ) return "done";
			
		BufferedReader file = new BufferedReader(new FileReader(Constants.EMPLOYEE_LIST));
        StringBuffer inputBuffer = new StringBuffer();
		
        JSONObject currEmployee = new JSONObject();
		JSONParser jsonparser = new JSONParser();
        
		String line;
		int currNumOfSales;
		double currSumOfSales;
		
		while ((line = file.readLine()) != null) {
			currEmployee = (JSONObject) jsonparser.parse(line);
			
	    		// find line in the employee list, update and save to temporary buffer
	    		if ( currEmployee.get("empId").equals(empId) && currEmployee.get("empType").equals("seller") ) {
	   
				currNumOfSales = Integer.parseInt( (String) currEmployee.get("numOfSales") );
				currSumOfSales = Double.parseDouble( (String) currEmployee.get("sumOfSales") );
				
				currEmployee.put("numOfSales", String.valueOf(++currNumOfSales));
				currEmployee.put("sumOfSales", String.valueOf(currSumOfSales + prodCost));
				
				inputBuffer.append(currEmployee.toJSONString());
				System.out.println(currEmployee.toJSONString());
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
        
		return "done";
	}
	
	/**
	 * Update sale List
	 * @param product
	 * @return
	 * @throws IOException
	 */
	public String updateSaleList(JSONObject product) throws IOException {
		FileWriter fw = new FileWriter(Constants.SALES_LIST, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
		JSONObject newSale = new JSONObject();
		
		newSale.put("date", new Date().toString());
		newSale.put("product", (String) product.get("name"));
		newSale.put("storeId", (String) product.get("storeId"));
		newSale.put("sumOfSale", (String) product.get("prodCost"));
		newSale.put("empId", (String) product.get("empId"));
	    
	    
	    out.println(newSale.toString());
	    out.close();
	    
		return "done";
	}
	
	public String getAllSalesList() throws ParseException, FileNotFoundException {
		File salesFile = new File(Constants.SALES_LIST);
		Scanner scanner = new Scanner(salesFile);
		
		JSONObject returnObj = new JSONObject();
		JSONObject sale = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
		   String lineFromFile = scanner.nextLine();
		   sale = (JSONObject) jsonparser.parse(lineFromFile);
		   
		   String currDate = (String) sale.get("date");
		   returnObj.put( currDate, sale);
		  
		}
		
		scanner.close();
		return returnObj.toString();
	}
	
	
}
