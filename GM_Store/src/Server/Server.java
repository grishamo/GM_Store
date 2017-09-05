package Server;
import java.util.Date;
import java.net.*;
import java.io.*;

public class Server {
		
	    public static void main(String[] args) throws IOException {
	    		final ServerSocket server = new ServerSocket(7000);
	    		Date date = new Date();
	    		
	    		System.out.println(date + "->> Server waits for client");
	    		
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
		    		    			while(!line.equals("goodbye")) {
		    		    				line = inputStream.readLine();
//		    		    				outputStream.println(line);
		    		    				System.out.println(date + "->> Recieved line: " + line);
		    		    			};
		    		    			
	    					} catch(IOException e) {
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

