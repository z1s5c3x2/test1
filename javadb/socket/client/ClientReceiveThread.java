package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiveThread extends Thread {
    private final Socket socket;
    private String receiveString;

    public ClientReceiveThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            DataInputStream tmpbuf = new DataInputStream(socket.getInputStream());
            while (true) {
                receiveString = tmpbuf.readUTF();
                if (receiveString == null) {
                    System.out.println("상대방 연결이 종료되었습니다.");
                } else {
                    System.out.println("상대방 : " + receiveString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
