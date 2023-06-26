package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerReceiveThread extends Thread {
    private final Socket socket;

    public ServerReceiveThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            DataInputStream tmpbuf = new DataInputStream(socket.getInputStream());
            String receiveString;
            while (true) {
                receiveString = tmpbuf.readUTF();
                System.out.println("받은 메세지 : " + receiveString);
            }
        } catch (SocketException e1) {
            System.out.println("상대방 연결이 종료되었습니다.");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

}