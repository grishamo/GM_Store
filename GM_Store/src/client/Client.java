package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {
	public static void main (String [] args) throws ParseException {
		String responseText;
		JSONObject responseObj;
		JSONParser jsonparser = new JSONParser();
		
		try {
			Socket socket= new Socket("localhost", 5000);
			Scanner response = new Scanner(socket.getInputStream());
			PrintStream request = new PrintStream(socket.getOutputStream());
			
			// Initiate SignIn screen
			SignInScreen signin = new SignInScreen(request);
			signin.frame.setVisible(true);
			
			// Waiting for server response on signIn
			responseText = response.nextLine();
			if( responseText == null) {
				System.out.println("Sign in failed");
				signin.showMessage("Sign in failed. Please try again");
			}
			else {
				responseObj = (JSONObject) jsonparser.parse(responseText);
				switch( (String) responseObj.get("empType") ) {
					case "cashier":
						// close sign screen;
						// open cashier screen
						break;
					case "manager":
						// close sign in screen
						// open manager screen
						break;
					default:
						System.out.println("Employee Type Undefined");
						signin.showMessage("Sign in failed. Please try again!");
				}
			}
			
			
			
			
			//TODO: render an employee screen
			
			System.out.println("SignIn response: " + responseText);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
