package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
	public static void main(String [] args) {
		Socket socket = null;
		DataInputStream fromNetInput;
		DataInputStream consoleInput;
		PrintStream toNetOutputStream;
		
		String line = "fromClient";
		
		try {
			socket = new Socket("localhost", 7000);
			
			fromNetInput = new DataInputStream(socket.getInputStream());
			consoleInput = new DataInputStream(System.in);
			toNetOutputStream = new PrintStream(socket.getOutputStream());
			
			SignInScreen signIn = new SignInScreen();
			signIn.setVisible(true);
		
			System.out.println("->> Recieved from server " + fromNetInput.readLine());
			
			
			
			toNetOutputStream.println(line);
			
			while(!line.equals("endserver")) {
				
				toNetOutputStream.println(line);
				
			}

			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		
		
	}
}
