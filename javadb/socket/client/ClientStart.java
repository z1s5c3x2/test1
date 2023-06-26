package client;

import java.net.Socket;

public class ClientStart {
    private static ClientStart instance;

    public static ClientStart Instance() {
        if (instance == null) {
            instance = new ClientStart();
        }
        return instance;
    }

    int port = 5314;

    public void ClientAwake() {
        try {
            Socket sock = new Socket("localhost", port);
            System.out.println(sock.getInetAddress()+"연결");

            ClientReceiveThread rt = new ClientReceiveThread(sock);
            rt.start();
            ClientSendThread st = new ClientSendThread(sock);
            st.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
