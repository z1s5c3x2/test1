package javadb;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * 회원제 게시판
   번호   제목   작성자   작성일   조회수 
   1   안녕하   qwe   06-22   33
   2   안녕하   asd   06-22   22
   3   안녕하   zxc   06-22   1
   
   1. 글쓰기 2.글보기  3.내정보

   [1] 제목/내용을 입력받아 현재 로그인된 아이디로 작성
      (db 자동 날짜)
   [2]
      몇 글 보실껀까요? 2   [ 선택받은 게시물은 조회수 증가  ]
      제목      작성자      조회수 
      내용      
      메뉴 : 1.뒤로가기  (작성일경우에만 표시) 2.수정 3.삭제
   [3] 
      1. 내가 쓴글(로그인된 사람) 만 보기
      2. 날짜별 작성된 게시물 수
      3. 로그인 횟수 [ db vs file ]

   *** MVC2 패턴 조사해서 JAVA파일 세분화
 * */
public class Main {

	public static void main(String[] args) throws IOException
	{
		
		SaltUpdate sU = new SaltUpdate();
		sU.getTime = CustomSha256hash.getSaltUTime();
		sU.start();
		Scanner sc = new Scanner(System.in);
		String userInputId ="";
		String userInputPwd ="";
		String userId="";
		DBC conection = new DBC();
		
		ArrayList<Board> boardList = new ArrayList<Board>();
		
		while(true)
		{
			

			//System.out.println(CustomSha256hash.GetHash256("User99","asd"));
		
		
			System.out.println("1 로그인 2 회원가입 3게시판");
			int getNum = sc.nextInt();
			sc.nextLine();
			if(getNum ==1) //로그인
			{
	
				System.out.println("아이디 입력");
				userInputId = sc.nextLine();
			
				System.out.println("비밀번호 입력");
				userInputPwd = sc.nextLine();
				//mysql db:test table:useraccount 
				userId = conection.UserLogin(userInputId, userInputPwd); // 로그인

			}
			else if(getNum ==2) { //회원가입
	
				System.out.println("아이디 입력");
				userInputId = sc.nextLine();
			
				System.out.println("비밀번호 입력");
				userInputPwd = sc.nextLine();
				System.out.println("비밀번호 확인");
				userInputPwd = sc.nextLine();
			
				System.out.println(conection.AddUser(userInputId, userInputPwd)); // 가입
			}
			else //게시판
			{
				if(userId.equals(""))
				{
					System.out.println("로그인 필요");
					continue;
				}
				while(true) {
				
				boardList = conection.ViewAllBoard();
				System.out.println("번호 작성자  제목    내용        날짜");
				
				for(int i=0;i<boardList.size();i++)
				{
					for(int x=0;x<5;x++)
					{
						System.out.printf("%s  ",boardList.get(i).GetBoard()[x]);
					}
					System.out.println();
				}
				System.out.println("1 글쓰기 2 글 보기 3 내정보");
				
				getNum = sc.nextInt();
				sc.nextLine();
				if(getNum == 1) // 글쓰기
				{
					
					System.out.println("제목 입력");
					String Title= sc.nextLine();
					System.out.println("내용 입력");
					String content= sc.nextLine();
					
					conection.WriteBoard(userId, Title, content);
				}
				else if(getNum == 2)  //글 보기
				{
					System.out.println("검색할 게시글의 번호 입력");
					getNum = sc.nextInt();
					sc.nextLine();
					String bUser = conection.SearchIDBoard(getNum);
					if(bUser.equals(userId))
					{
						System.out.println("1뒤로가기 2수정 3삭제");
						
						int selectNum = sc.nextInt();
						
						if(selectNum == 1)
						{
							continue;
						}
						else 
						{
							conection.UpdeateBoard(selectNum,getNum);
						}
					}
					else
					{
						System.out.println("1뒤로가기");
						sc.nextInt();
						continue;
					}
					
			
					
					
					
				}
				else if(getNum ==3) //내정보
				{
					System.out.println("1내가쓴글 2 날짜별 작성 게시물 수 3로그인 횟수");
					
					getNum = sc.nextInt();
					sc.nextLine();
					if(getNum == 1) //내가쓴글
					{
						conection.SearchWriterBoard(userId);
					}
					else if(getNum == 2)  //날짜별 작성된 게시물
					{
						conection.SearchMyBoard(userId);
					}
					else if(getNum == 3) //로그인 횟수
					{
						System.out.println("로그인 횟수 : "+ conection.GetLoginCont(userId));
				
					}
				}
				}
			}
		}
		
		
		
		
		
	
		

	}

}
