package server;
import java.net.Socket;
import java.net.ServerSocket;

public class Serverstart {
    private static Serverstart instance;
	public static Serverstart Instance() 
	{
		if(instance == null)
		{
			instance = new Serverstart();
		}
		return instance;
	}
    public void ServerAwake()
    {
     
        
        try {
            int port = 5314;
            ServerSocket sSocket = new ServerSocket(port);
            
            System.out.println("연결중 ?!☆★※㉾○");
            while(true)
            {
                Socket sock = sSocket.accept();
                System.out.println(sock.getInetAddress()+"와 연결");  
                ServerReceiveThread rt = new ServerReceiveThread(sock);
                rt.start();  
                ServerSendThread st = new ServerSendThread(sock);
                st.start();
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
