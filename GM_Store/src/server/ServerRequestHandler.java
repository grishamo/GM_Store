package server;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import auth.Auth;
import auth.AuthExceptions;
import employee.Cashier;
import employee.Employee;
import reports.Reports;

public class ServerRequestHandler {
		
		private String responseStr;
		private JSONObject reqObj;
		private JSONParser jsonParser;
		private Reports reports;
		
		/**
		 * Constructor
		 * @param clientData
		 * @throws IOException
		 * @throws ParseException
		 * @throws AuthExceptions 
		 */
		public ServerRequestHandler (String clientData) throws IOException, ParseException, AuthExceptions {
			jsonParser = new  JSONParser();
			reports = new Reports();
			reqObj = (JSONObject) jsonParser.parse(clientData);
			
			for (Object key : reqObj.keySet()) {
		        
		        String keyStr = (String)key;
		        JSONObject keyvalue = (JSONObject) reqObj.get(keyStr);
		        
		        System.out.println("ServerRequestHandler:KEY: "+ keyStr);
		        
		        switch(keyStr) {
			        case "signin":
			        		Auth signIn = new  Auth(keyvalue);
			        		this.responseStr = signIn.getResponse();
				        	break;
			        case "setCashier":
//			        		Employee newEmp = new Cashier(keyvalue);
			        		break;
			        case "getAllCustomers":
//			        		this.responseStr = reports.getAllCustomers();
			        		break;
		        }   
		    }
			
		}
			
		// Get Response String
		public String response() {
			return this.responseStr;
		}
		
}
