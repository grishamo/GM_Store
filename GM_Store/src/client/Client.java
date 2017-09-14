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
		
		String clientMsg = "set_employee";
		
		try {
			socket = new Socket("localhost", 7000) ;
			
			fromNetInput = new DataInputStream(socket.getInputStream());
			consoleInput = new DataInputStream(System.in);
			toNetOutputStream = new PrintStream(socket.getOutputStream());
			
			try {
				SignIn signInwindow = new SignIn();
				signInwindow.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			toNetOutputStream.print(clientMsg);
//			System.out.print("from server: " + fromNetInput.readLine() );
			
			
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Socket.close() exception: " + e.getMessage());
				System.err.println(e.getMessage());
			}
		}
		
		
	}
}
