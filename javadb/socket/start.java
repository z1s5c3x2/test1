import client.ClientStart;
import server.Serverstart;
import java.util.Scanner;
public class start {
    public static void main(String[] args) 
    {
        
        if(new Scanner(System.in).nextInt() ==1)
        {
            Serverstart.Instance().ServerAwake();
        }
        else
        {ClientStart.Instance().ClientAwake();}
    }
}
