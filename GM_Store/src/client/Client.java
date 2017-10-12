package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {
	public static void main (String [] args) throws ParseException {
		Socket socket = null;
		String responseText = "";
		JSONObject responseObj;
		JSONParser jsonparser = new JSONParser();
		
		try {
			socket= new Socket("localhost", 8000);
			DataInputStream response = new DataInputStream(socket.getInputStream());
			PrintStream request = new PrintStream(socket.getOutputStream());
			
			System.out.println(new Date() + " --> Recieved from Server: " + response.readLine() );
			
			// Initiate SignIn screen
			SignInScreen signin = new SignInScreen(request);
			signin.frame.setVisible(true);
		
			
			while( !responseText.contains("empType") ) {
				responseText = response.readLine();
				
				if( responseText.length() >= 1 && responseText.contains("empType") ) {
					signin.frame.setVisible(false);
					
					// Parse sign in response ( Employee data ) to JSON
					responseObj = (JSONObject) jsonparser.parse(responseText);
					String empScreen = (String) responseObj.get("empType");
					
					switch( empScreen ) {
						case "cashier":
							
							CashierScreen cashierScn = new CashierScreen(responseObj, response, request);
							cashierScn.frame.setVisible(true);
							
							// close sign screen;
							// open cashier screen
							break;
						case "manager":
							// close sign in screen
							// open manager screen
							break;
					}
				}
				else {
					System.out.println(new Date() + " --> Sign in failed");
					signin.showMessage("Sign in failed. Please try again!");
				}
			}
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch ( IOException e ) {}
		}
		
	}
}
