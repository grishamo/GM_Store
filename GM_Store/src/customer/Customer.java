package customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import configuration.Constants;
import employee.EmployeeException;

public abstract class Customer {
	
	protected String name;
	protected long id, tel;
	protected double sumOfDeals;
	protected int numOfDeals;
	protected String status;
	private String customersFile = Constants.CUSTOMERS_LIST;

	//Constructors:
	public Customer() {
		
	}
	
	public Customer(String firstName, long id, long phoneNumber) {
		setName(firstName);
		setID(id);
		setPhoneNumber(phoneNumber);
		setPurchasesSum();
		setPurchasesCount();
	}
	//============================================================//
	
	//Sets:
	public void setName(String firstName) {
		this.name = firstName;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public void setPhoneNumber(long phoneNumber) {
		this.tel = phoneNumber;
	}
	
	public abstract void setStatus();
	public abstract String toString();
	
	public void setPurchasesSum() {
		this.sumOfDeals = 0;
	};
	
	public void setPurchasesCount() {
		this.numOfDeals = 0;
	};
	//============================================================//
	
	//Gets:
	public String getName() {
		return this.name;
	}
	
	public long getID() {
		return this.id;
	}
	
	public long getPhoneNumber() {
		return this.tel;
	}
	
	public double getPurchasesSum() {
		return this.sumOfDeals;
	}
	
	public int getPurchasesCount() {
		return this.numOfDeals;
	}
	
	//============================================================//
	
	public boolean isCustomerExist () throws ParseException {
		File custFile = new File (this.customersFile);
		JSONObject custJson = new JSONObject();
		JSONParser jsonparser = new JSONParser();
		
		try {
		    Scanner scan = new Scanner(custFile);

		    while (scan.hasNextLine()) {
		        String line = scan.nextLine();
		        custJson = (JSONObject) jsonparser.parse(line);
		        if( custJson.get("id").equals(this.id) ) {
		        		scan.close();
		        		return true;
		        }
		        
		    }
		    scan.close();
		    return false;
		    
		} catch(FileNotFoundException e) { 
			return false;
		}
	}
	
	//============================================================//
	
	public String save() throws IOException, EmployeeException, ParseException {
		
		if( !this.isCustomerExist() ) {
			
			FileWriter fw = new FileWriter(this.customersFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
				
		    out.println(this.toString());
		    out.close();
		    
		    return "done";
		}
		else {
			return "";
		}
	}
};
