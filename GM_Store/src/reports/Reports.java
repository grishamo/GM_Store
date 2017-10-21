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
	public String updateEmployeeSale(JSONObject product) throws ParseException, NumberFormatException, IOException {
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
	
	/**
	 * Get all sales
	 * @return
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
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
	
	/**
	 * Get all sellers by store id
	 * @param storeId
	 * @return
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public String getSellersByStore(String storeId) throws FileNotFoundException, ParseException {
		File empFile = new File(Constants.EMPLOYEE_LIST);
		Scanner scanner = new Scanner(empFile);
		JSONObject returnObj = new JSONObject();
		JSONObject seller = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
			   String lineFromFile = scanner.nextLine();
			   seller = (JSONObject) jsonparser.parse(lineFromFile);
			   
			   if( seller.get("storeId").equals(storeId) && seller.get("empType").equals("seller")) {
				   returnObj.put( (String) seller.get("id"), seller);
			   }
			   
			}
		
		scanner.close();
		return returnObj.toString();
	}
	
	/**
	 * Get all employees data
	 * @return
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public String getAllEmployeesData() throws FileNotFoundException, ParseException {
		File empFile = new File(Constants.EMPLOYEE_LIST);
		Scanner scanner = new Scanner(empFile);
		JSONObject returnObj = new JSONObject();
		JSONObject seller = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		while ( scanner.hasNextLine() ) {
		   String lineFromFile = scanner.nextLine();
		   seller = (JSONObject) jsonparser.parse(lineFromFile);
	   
		   returnObj.put( (String) seller.get("empId"), seller);
   
		}
		
		scanner.close();
		return returnObj.toString();
	}
	
	/**
	 * Delete Employee by Id
	 * @param id
	 * @return
	 */
	public String deleteEmployeeById(String id) {
		try {
			deleteEmployeeInFile(Constants.EMPLOYEE_LIST, id);
			deleteEmployeeInFile(Constants.AUTH_LIST, id);
			return "done";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Delete employee by file and id
	 * @param srcFile
	 * @param id
	 * @throws IOException
	 * @throws ParseException
	 */
	public void deleteEmployeeInFile(String srcFile, String id) throws IOException, ParseException {
		BufferedReader file = new BufferedReader(new FileReader(srcFile));
        StringBuffer inputBuffer = new StringBuffer();
		
        JSONObject currEmployee = new JSONObject();
		JSONParser jsonparser = new JSONParser();
        
		String line;
		
		while ((line = file.readLine()) != null) {
			currEmployee = (JSONObject) jsonparser.parse(line);
	    		// find line in the employee list, update and save to temporary buffer
	    		if ( !currEmployee.get("id").equals(id)  ) {
	    			inputBuffer.append(line);
	    			inputBuffer.append('\n');
	    		}
	        
		}
		
		// convert buffer to string
        String inputStr = inputBuffer.toString();
		
        // close products list
        file.close();
        
        // open products list for writing, and replace the content with temporary buffer
        FileOutputStream fileOut = new FileOutputStream(srcFile);
        fileOut.write(inputStr.getBytes());
        fileOut.close();
        
	}
	
	/**
	 * Get VIP discount information
	 * @return
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public String getVipDiscountInfo() throws FileNotFoundException, ParseException {
		File empFile = new File(Constants.DISCOUNT_INFO);
		Scanner scanner = new Scanner(empFile);
		String returnStr = "";
		
		while ( scanner.hasNextLine() ) {
		   String lineFromFile = scanner.nextLine();
		   returnStr = lineFromFile;
		}
		
		scanner.close();
		return returnStr;
	}
	
	/**
	 * Update VIP Discount information
	 */
	public String updateVipDiscountInfo(JSONObject reqObj) throws IOException {
		
		PrintWriter cleaner = new PrintWriter(Constants.DISCOUNT_INFO);
		cleaner.print("");
		cleaner.close();
		
		FileWriter fw = new FileWriter(Constants.DISCOUNT_INFO, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
	 
		out.println(reqObj.toJSONString());
		out.close();
		return "done";
	}
	
	/**
	 * Update customers
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public String updateCustomerList (JSONObject obj) throws IOException, ParseException {
		JSONObject customer = (JSONObject) obj.get("customer");
		JSONParser jsonparser = new JSONParser();
		double prodCost = Double.parseDouble((String) obj.get("prodCost"));
		String customerId = (String)customer.get("id");
		
		//return if customer id is undefined
		if( customerId.equals("") ) return "done";
		
		String discountInfo = getVipDiscountInfo();
		JSONObject disountObj = (JSONObject) jsonparser.parse(discountInfo);
		int sumOfpurchase = Integer.parseInt((String)disountObj.get("sumOfPurchases"));
		
		BufferedReader file = new BufferedReader(new FileReader(Constants.CUSTOMERS_LIST));
        StringBuffer inputBuffer = new StringBuffer();
		
        JSONObject currCustomer = new JSONObject();
        
		String line;
		int currNumOfDeals;
		double currSumOfDeals;
		
		while ((line = file.readLine()) != null) {
			currCustomer = (JSONObject) jsonparser.parse(line);
			
	    		// find line in the employee list, update and save to temporary buffer
	    		if ( currCustomer.get("id").equals(customerId) ) {
	   
				currNumOfDeals = Integer.parseInt( (String) currCustomer.get("numOfDeals") );
				currSumOfDeals = Double.parseDouble( (String) currCustomer.get("sumOfDeals") );
				
				currCustomer.put("numOfDeals", String.valueOf(++currNumOfDeals));
				currCustomer.put("sumOfDeals", String.valueOf(currSumOfDeals + prodCost));
				
				if( (currSumOfDeals + prodCost) > sumOfpurchase) {
					currCustomer.put("status", "vip");
				}
				
				inputBuffer.append(currCustomer.toJSONString());
				System.out.println(currCustomer.toJSONString());
	    		}
	    		else {
	    			inputBuffer.append(line);
	    		}
	        inputBuffer.append('\n');
		}
		
		// convert buffer to string
        String inputStr = inputBuffer.toString();
		
        // close customers list
        file.close();
        
        // open customers list for writing, and replace the content with temporary buffer
        FileOutputStream fileOut = new FileOutputStream(Constants.CUSTOMERS_LIST);
        fileOut.write(inputStr.getBytes());
        fileOut.close();
        
		return "done";
	}
	
	/**
	 * Save log to the file
	 * @param logTxt
	 */
	public void log(String logTxt) {
		FileWriter fw;
		
		try {
			fw = new FileWriter(Constants.LOGS_LIST, true);
			BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
		    
		    out.println(logTxt);
		    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
}
