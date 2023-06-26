package server;

import java.io.IOException;
import java.net.Socket;
import DataController.DataController;
import java.io.DataOutputStream;

public class ServerSendThread extends Thread{
    private Socket socket;

    public ServerSendThread(Socket socket)
    {
        this.socket = socket;}

    
    public void run() 
    {
        try 
        {
            DataOutputStream sendWriter = new DataOutputStream(socket.getOutputStream());
            
            while (true) 
            {
                DataController.Instance().Send(sendWriter);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
