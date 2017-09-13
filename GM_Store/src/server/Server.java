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
	    		
	    		System.out.println(date + "->> Server waits for client <<-\n");
	    		
	    		while(true) {
	    			final Socket socket = server.accept(); // blocking 
	    			
	    			new Thread(new Runnable() {
	    				public void run() {
	    					String clientAddress = "";
	    					String line = "";
	    					
	    					try {
	    						clientAddress = socket.getInetAddress() + ":" + socket.getPort();
	    						System.out.println(date + ": Client connected from" + clientAddress );
	    		    			
		    		    			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		    		    			PrintStream outputStream = new PrintStream(socket.getOutputStream());
	    						
		    		    			outputStream.print("Welcome to Server!");
		    		    			System.out.println(date + "->> Recieved line: " + inputStream.toString() + "\n");
		    		    			
//		    		    			Employee grisha = new Cashier("Grisha", "317612950", "0548131173", 44332, 01);
		    		    			line = inputStream.readLine();
		    		    			
		    		    			while(!line.equals("goodbye")) {
//		    		    				line = inputStream.readLine(); 
		    		    				outputStream.println(line);
//		    		    				System.out.println(date + "->> Recieved line: " + inputStream.toString());
		    		    			};
		    		    			
		    		    			
	    					} catch (IOException e) {
	    		    				System.err.println(e);
		    		    		} catch( Exception e) {
		    		    			System.err.println(e);
		    		    		} finally {
		    		    			try {
		    		    				socket.close();
		    		    				server.close();
		    		    			} catch (IOException e) {
		    		    				System.err.println(e);
		    		    			}
		    		    		}
	    				}	
    				}).start();
	  
	    		}
		
	    }
}

