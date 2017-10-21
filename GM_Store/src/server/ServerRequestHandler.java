package server;

import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import auth.Auth;
import auth.AuthExceptions;
import customer.Customer;
import customer.NewCustomer;
import customer.VipCustomer;
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
		        reports.log(new Date() + ":ServerRequest: " + keyStr);
		        
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
			        		
			        case "deleteEmployeeById":
			        		String reqEmpId = (String) reqObj.get(keyStr);
		        			this.responseStr = reports.deleteEmployeeById(reqEmpId);
		        			break;		
		        			
			        case "getAllCustomers":
		        			this.responseStr = reports.getAllCustomers();
		        			break;
			        
			        case "getVipDiscountInfo":
	        				this.responseStr = reports.getVipDiscountInfo();
	        				break;
	        			
			        case "updateVipDiscountInfo":
			        		reqData = (JSONObject) reqObj.get(keyStr);
        					this.responseStr = reports.updateVipDiscountInfo(reqData);
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
			        		String updateCustomerList = reports.updateCustomerList(reqData);
			        		
			        		this.responseStr = "done";
		        			break;
		        			
			        case "newCustomer":
			        		reqData = (JSONObject) reqObj.get(keyStr);
			        		Customer newCust;
			        		switch ( (String) reqData.get("status") ) {
			        			case "vip":
				        			newCust = new VipCustomer(reqData);
				        			newCust.save();
				        			break;
			        			default:
			        				newCust = new NewCustomer(reqData);
				        			newCust.save();
				        			break;
			        		}
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
