package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import auth.AuthExceptions;

public class ClientHandler implements Runnable{
	
    Socket clientSocket;
    PrintStream response;
    Scanner request;
    String reqData;
    
    public ClientHandler(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.response = new PrintStream(clientSocket.getOutputStream());
        this.request = new Scanner(clientSocket.getInputStream());
    }
    
    @Override
    public void run() {
        while(true){
		    try {
		    		
		    		reqData = request.nextLine();	
		    	
		    		ServerRequestHandler requestHandler = new ServerRequestHandler(reqData);
				response.println( requestHandler.response() );	
				
		    		
		    }
		    catch( AuthExceptions e ) {
			    	System.out.println(e.getMessage());
		    		try {
					clientSocket.close();
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
}  