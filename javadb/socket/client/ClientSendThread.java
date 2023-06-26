package client;
import DataController.DataController;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendThread extends Thread{
    private final Socket socket;
    Scanner scanner = new Scanner(System.in);

    public ClientSendThread(Socket socket){this.socket = socket;}


    public void run() {

        try {
            DataOutputStream sendWriter = new DataOutputStream(socket.getOutputStream());
            
            while (true) {
                DataController.Instance().Send(sendWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
