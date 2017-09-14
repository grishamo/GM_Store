package server;
import employee.Cashier;
import employee.Employee;
import employee.EmployeeException;

import java.util.Date;
import java.net.*;
import java.io.*;

public class Server {
		
	    /**
	     * @param args
	     * @throws IOException
	     */
	    public static void main(String[] args) throws IOException {
	    		final ServerSocket server = new ServerSocket(7000);
	    		Date date = new Date();
	    		
	    		System.out.println(date + ":  Server waits for client\n");
	    		
	    		while(true) {
	    			final Socket socket = server.accept(); // blocking 
	    			System.out.println(date + ": Client connected");
	    			
	    			new Thread(new Runnable() {
	    				public void run() {
	    					String clientAddress = "";
	    					String clientMsg = "";
	    				
	    					
	    					try {
	    						clientAddress = socket.getInetAddress() + ":" + socket.getPort();
	    						System.out.println(date + ": Client connected from: " + clientAddress );
	    		    			
		    		    			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		    		    			PrintStream outputStream = new PrintStream(socket.getOutputStream());
	    						
		    		    			outputStream.print("Welcome to Server!");
		    		    			
//		    		    			Employee grisha = new Cashier("Grisha", "317612950", "0548131173", 44332, 01);
		    		    			clientMsg = inputStream.readLine();
		    		    			
		    		    			while(!clientMsg.equals("goodbye")) {
		    		    				
		    		    				switch(clientMsg) {
		    		    					case "set_employee" : 
		    		    						Employee grisha = new Cashier("Grisha", "317612950", "0548131173", 44332, 01);
		    		    						System.out.println("Set Employee: " + grisha.toString());
		    		    					case "sign_in" :
		    		    						Auth user = new Auth();
		    		    				}
		    		    				
		    		    				clientMsg = inputStream.readLine(); 
//		    		    				System.out.println(date + "->> Recieved line: " + inputStream.toString());
		    		    			};
		    		    			
		    		    			
	    					} catch (IOException e) {
	    		    				System.err.println("IOexception: " + e.getMessage());
		    		    		} catch( Exception e) {
		    		    			System.err.println("Exception: " + e);
		    		    		} finally {
		    		    			try {
		    		    				socket.close();
		    		    			} catch (IOException e) {
		    		    				System.err.println("Socket.close() exception: " + e);
		    		    			}
		    		    		}
	    				}	
    				}).start();
	  
	    		}
		
	    }
}

