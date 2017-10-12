
package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Server {

    static public void main(String str[]){
    	 	ArrayList<ObjectOutputStream> clientstreams = new ArrayList<ObjectOutputStream>();
    	
    		try {
            ServerSocket mySocket = new ServerSocket(8000);
            while(true){
                try {
                    Socket client = mySocket.accept();
//                    clientstreams.add(new ObjectOutputStream(client.getOutputStream()));
                    System.out.println(client.getInetAddress().getHostAddress() + " connected to the Server");
                    Thread t = new Thread(new ClientHandler(client));
                    t.start();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to start.");
            e.printStackTrace();
        }
    }
 
}