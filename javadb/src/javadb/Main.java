package javadb;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException
	{ 
		SaltUpdate sU = new SaltUpdate();
		sU.start();
		
		while(true)
		{
		

		
			Scanner sc = new Scanner(System.in);
			String userInputId;
			String userInputPwd;
		
		
			DBC conection = new DBC();
			//System.out.println(CustomSha256hash.GetHash256("User99","asd"));
		
		
			System.out.println("1 로그인 2 회원가입");
			if(sc.nextInt() == 1)
			{
				userInputId = sc.nextLine();
				System.out.println("아이디 입력");
				userInputId = sc.next();
			
				System.out.println("비밀번호 입력");
				userInputPwd = sc.next();
				//mysql db:test table:useraccount 
				System.out.println(conection.UserLogin(userInputId, userInputPwd)); // 로그인
			
			}
			else {
				userInputId = sc.nextLine();
				System.out.println("아이디 입력");
				userInputId = sc.next();
			
				System.out.println("비밀번호 입력");
				userInputPwd = sc.next();
				System.out.println("비밀번호 확인");
				userInputPwd = sc.next();
			
				System.out.println(conection.AddUser(userInputId, userInputPwd)); // 가입
			}
		}
		
		
		
		
		
	
		

	}

}
