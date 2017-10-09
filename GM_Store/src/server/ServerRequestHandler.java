package server;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONObject;

import configuration.Constants;
import employee.Employee;
import employee.EmployeeException;

public class ServerRequestHandler {
		
		private String responseStr;
		
		// Class Constructor
		public ServerRequestHandler (JSONObject clientData) throws EmployeeException, IOException {
			
			for (Object key : clientData.keySet()) {
		        
		        String keyStr = (String)key;
		        Object keyvalue = clientData.get(keyStr);
		        
		        System.out.println("ServerRequestHandler:KEY: "+ keyStr);
		        
		        switch(keyStr) {
			        case "signin":
				        	this.signInHandler(keyvalue);
				        	break;
		        }   
		    }
			
		}
		
		// Handle sign in request:
		public void signInHandler (Object data) throws EmployeeException, IOException {
			String strData = data.toString();
			
//			Employee grisha = new Employee("Grisha", "317612950", "0548131173", 1234, 1);
//			grisha.save();

			if ( this.isExist(strData, Constants.AUTH_LIST ) ) {
				System.out.println("User Exist!");
				//TODO get employee from employees.txt and return to the client
				this.responseStr = "signInResponse";
			}
			else {
				throw new Error("User doesn't exist!");
			};
			
			//TODO check that data matches some data in auth.txt
			// if not - response to client with error: "Not Authorized"
			// if so - find user name in employee.txt and response with employee data - and render appropriate screen
		}
		
		public boolean isExist(String data, String fileName) {
			Scanner scanner = new Scanner(fileName);
			
			while (scanner.hasNextLine()) {
				   String lineFromFile = scanner.nextLine();
				   if(lineFromFile.contains(data)) { 
				       return true;
				   }
				}
			
			return false;
		}
		
		// Get Response String
		public String response() {
			return "resp";
		}
		
}
