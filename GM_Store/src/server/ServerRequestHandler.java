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
import reports.Reports;

public class ServerRequestHandler {
		
		private String responseStr;
		private JSONObject reqObj;
		private JSONObject responseObj;
		private JSONParser jsonParser;
		private Reports reports;
		
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
			        	
			        		JSONObject signInValue = (JSONObject) reqObj.get(keyStr);
			        		if ( signIn.isAuth(signInValue) ) {
			        			this.responseStr = reports.getEmployeeById( (String) signInValue.get("id") );
			        		}	   
			        		else { this.responseStr = "null"; }
				        	break;
				        	
			        case "setNewEmpoyee":
			        	
//			        		Employee newEmp = new Cashier(keyvalue);
			        		break;
			        		
			        case "getAllCustomers":
			        	
		        			this.responseStr = reports.getAllCustomers();
		        			break;
			        		
			        case "getProductsByStore":
			        		String storeId = (String) reqObj.get(keyStr);
			        		this.responseStr = reports.getProductsByStore(storeId);
		        			break;

			        case "purchaseAction":
			        		JSONObject productToUpdate = (JSONObject) reqObj.get(keyStr);
			        		
			        		String updateProduct = reports.updateProductQuantity(productToUpdate);
			        		String updateEmployee = reports.updateEmployeeInfo(productToUpdate);
			        		String updateSaleList = reports.updateSaleList(productToUpdate);
			        		
			        		this.responseStr = "done";
		        			break;
		        			
			        case "newCustomer":
			        		JSONObject newCustomer = (JSONObject) reqObj.get(keyStr);
//			        		Customer newCust = new Customer(name, tel, id, status) 
//			        		this.responseStr = reports.updateProductQuantity(productToUpdate);
		        }   
		    }
			
		}
			
		// Get Response String
		public String response() {
			return this.responseStr;
		}
		
}
