package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main (String [] args) {
		String responseText;
		
		try {
			Socket socket= new Socket("localhost", 7000);
			Scanner response = new Scanner(socket.getInputStream());
			PrintStream request = new PrintStream(socket.getOutputStream());
			
			// Initiate SignIn screen
			SignInScreen signin = new SignInScreen(request);
			signin.frame.setVisible(true);
			
			// Waiting for server response on signIn
			responseText = response.nextLine();
			
			System.out.println("SignIn response: " + responseText);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
