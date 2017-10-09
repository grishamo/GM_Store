package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MessageStreamerTpl {
    ObjectOutputStream out;
    ObjectInputStream in;

    public MessageStreamerTpl(String ip, int port) throws UnknownHostException, IOException{
        connect(ip, port);
    }

    public MessageStreamerTpl(){
    }

    public void send(Object message) throws IOException {
        if(out == null) throw new IOException();
        out.writeObject(message);
        out.flush();
    }

    public Object receive() throws IOException{
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException();
        }
    }

    public void connect(String ip, int port) throws UnknownHostException, IOException {
        Socket socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        InputStream is = socket.getInputStream();
        in = new ObjectInputStream(is);
    }

}