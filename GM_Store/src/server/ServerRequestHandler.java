package server;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import auth.Auth;
import auth.AuthExceptions;
import employee.Cashier;
import employee.Employee;
import employee.EmployeeException;
import employee.Manager;
import employee.Seller;
import reports.Reports;

public class ServerRequestHandler {
		
		private String responseStr;
		private JSONObject reqObj;
		private JSONObject reqData;
		private JSONObject responseObj;
		private JSONParser jsonParser;
		private Reports reports;
		private String storeId;
		private Employee newEmp;
		
		/**
		 * Constructor
		 * @param clientData
		 * @throws IOException
		 * @throws ParseException
		 * @throws AuthExceptions 
		 * @throws EmployeeException 
		 */
		public ServerRequestHandler (String clientData) throws 
		IOException, ParseException, AuthExceptions, EmployeeException 
		{
			Auth signIn = new  Auth();
			jsonParser = new  JSONParser();
			reports = new Reports();
			responseObj = new JSONObject();
			responseStr = "";
			
			reqObj = ( JSONObject ) jsonParser.parse(clientData);
			
			for (Object key : reqObj.keySet()) {
		        
		        String keyStr = (String) key;
		        System.out.println("ServerRequestHandler:KEY: "+ keyStr);
		        
		        switch( keyStr ) {
		        
			        case "signin":
			        		reqData = (JSONObject) reqObj.get(keyStr);
			        		if ( signIn.isAuth(reqData) ) {
			        			this.responseStr = reports.getEmployeeById( (String) reqData.get("id") );
			        		}	   
			        		else { this.responseStr = "null"; }
				        	break;
				        	
			        case "addEmployee":
			        		reqData = (JSONObject) reqObj.get(keyStr);
			        		switch ( (String) reqData.get("empType") ) {
			        			case "cashier":
				        			newEmp = new Cashier(reqData);
				        			break;
			        			case "manager":
				        			newEmp = new Manager(reqData);
				        			break;
			        			case "seller":
				        			newEmp = new Seller(reqData);
				        			break;
			        		}

		        			this.responseStr = newEmp.save();
			        		break;
			        		
			        case "deleteEmployee":
			        		reqData = (JSONObject) reqObj.get(keyStr);
//		        			this.responseStr = reports.deleteEmployee();
		        			break;		
		        			
			        case "getAllCustomers":
		        			this.responseStr = reports.getAllCustomers();
		        			break;
			        		 
			        case "getProductsByStore":
			        		storeId = (String) reqObj.get(keyStr);
			        		this.responseStr = reports.getProductsByStore(storeId);
		        			break;

			        case "purchaseAction":
			        		reqData = (JSONObject) reqObj.get(keyStr);
			        		
			        		String updateProduct = reports.updateProductQuantity(reqData);
			        		String updateEmployeeSale = reports.updateEmployeeSale(reqData);
			        		String updateSaleList = reports.updateSaleList(reqData);
			        		
			        		this.responseStr = "done";
		        			break;
		        			
			        case "newCustomer":
			        		reqData = (JSONObject) reqObj.get(keyStr);
//			        		Customer newCust = new Customer(name, tel, id, status) 
//			        		this.responseStr = reports.updateProductQuantity(productToUpdate);
			        		break;
			        		
			        case "getAllSalesList":
			        		this.responseStr = reports.getAllSalesList();
			        		break;
			        		
			        case "getSellersByStore":
			        		storeId = (String) reqObj.get(keyStr);
				        	this.responseStr = reports.getSellersByStore(storeId);
			        		break;
			        		
			        case "getAllEmployeesData":
			        		this.responseStr = reports.getAllEmployeesData();
			        		break;
		        }   
		    }
			
		}
			
		// Get Response String
		public String response() {
			return this.responseStr;
		}
		
}
