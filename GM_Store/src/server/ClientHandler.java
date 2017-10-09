package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClientHandler implements Runnable{
	
    Socket clientSocket;
    PrintStream response;
    Scanner request;
    String reqData;
    JSONObject jsonData;
    JSONParser jsonParser;
    
    public ClientHandler(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        this.response = new PrintStream(clientSocket.getOutputStream());
        this.request = new Scanner(clientSocket.getInputStream());
        this.jsonParser = new  JSONParser();
    }
    
    @Override
    public void run() {
        while(true){
		    try {
		    		
		    		reqData = request.nextLine();
		    		jsonData = (JSONObject) jsonParser.parse(reqData);
		    		
		    		ServerRequestHandler requestHandler = new ServerRequestHandler(jsonData);
				response.println("server responce: " + requestHandler.response());	
		    		
		    } catch (Exception e) {
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