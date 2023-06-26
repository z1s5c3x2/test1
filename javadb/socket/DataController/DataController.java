package DataController;
import java.io.DataOutputStream;
import java.util.Scanner;

public class DataController {
    private static DataController instance;
	public static DataController Instance() 
	{
		if(instance == null)
		{
			instance = new DataController();
		}
		return instance;
	}
    Scanner sc = new Scanner(System.in);

    public void Send(DataOutputStream _DS)
    {
        try {
            String msg = sc.nextLine();
            _DS.writeUTF(msg);
            _DS.flush();
            System.out.println("보낸 메세지 : " + msg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}
