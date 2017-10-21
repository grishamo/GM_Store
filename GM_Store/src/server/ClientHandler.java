package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import auth.AuthExceptions;
import employee.Cashier;
import employee.Employee;

public class ClientHandler implements Runnable{
	
    Socket clientSocket;
    PrintStream response;
    DataInputStream request;
    String reqData;
    
    public ClientHandler(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.response = new PrintStream(clientSocket.getOutputStream());
        this.request = new DataInputStream(clientSocket.getInputStream());
    }
    
    @Override
    public void run() {
        		
		    try {
		    		response.println("Welcome");
		    		
		    		while(true) {
		    			reqData = request.readLine();	
		    			
			    		if( reqData.length() > 0 ) {
			    			ServerRequestHandler requestHandler = new ServerRequestHandler(reqData);
			    			String respStr = requestHandler.response();
			    			
			    			response.println(respStr);
			    			
			    		}
		    		}
		    		
		    }
		    catch (Exception e) {
		    		System.out.println(e.getMessage());
		    		try {
					clientSocket.close();
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		
    }
}  