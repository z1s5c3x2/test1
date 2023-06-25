package View;

import java.util.ArrayList;
import java.util.Scanner;

import Controller.BoardController;
import Controller.CommentController;
import Controller.LoginController;
import Model.Dao.AccountDao;
import Model.Dao.DBDao;
import Model.Dto.BoardDto;
import Model.Dto.CommentDto;
import Service.FileService;


public class MainView {
    private static MainView instance;
	public static MainView Instance()
	{
		if(instance == null)
		{
			instance = new MainView();
		}
		return instance;
	}

    Scanner sc = new Scanner(System.in);

    public void Index() {
        if(!CheckDBServer()){return;} 
        LoginMenu();
        

    }
    boolean CheckDBServer()
    {

        String _msg = DBDao.Instance().DBConnect();
        if(_msg.equals("성공"))
        {
            return true;
        }
        System.out.println(_msg);
        return false;
        
    }



    void LoginMenu()
    {
        while(true)
        {
            System.out.println("비회원 메뉴");
            System.out.println("1 로그인 2 회원가입");
            int select = sc.nextInt();
            sc.nextLine();
            if(select ==1){
                if(Login()){MainMenu();}
            }
            else if(select ==2 ){SignUp();}
        }
        
    }
    void MainMenu()
    {
        System.out.printf("%s %5s %11s %20s %13s %13s", "번호", "제목", "작성자","내용" ,"작성일", "조회수\n");
        // 게시글 불러오기
        
        //메인 게시글
        
        BoardController.Instance().nowPage =0;
        while (!AccountDao.Instance().adt.GetId().equals("")) {
            
            PrintBoard(BoardController.Instance().ViewBoard());
            
            System.out.println("1. 글쓰기 2.글보기 3.다음 페이지 4.이전 페이지 5.내정보 6.검색 7.로그아웃");
            int select = sc.nextInt();
            sc.nextLine();

            if (select == 1) {
                System.out.println("제목 입력");
                String _title = sc.nextLine();
                System.out.println("내용 입력");
                String _content = sc.nextLine();
                BoardController.Instance().WriteBoardInit(_title, _content);
            } else if (select == 2) {//원하는 글 보기
                System.out.println("보고싶은 글의 번호 입력");
                int _num = sc.nextInt();
                sc.nextLine();
                BoardDto Bdt = BoardController.Instance().SearchIdBoard(_num);
                if(!Bdt.getTitle().equals(""))
                {
                    BoardInfo(Bdt);
                }
                else
                {System.out.println("존재하지 않는 게시글");}
                
                
            } else if (select == 3) { //다음
                if(!BoardController.Instance().PageCheck(1))
                {
                    System.out.println("마지막 페이지 입니다");
                    
                    continue;
                }
                BoardController.Instance().nowPage +=1;

            }else if (select == 4) { //이전
                if(!BoardController.Instance().PageCheck(-1))
                {
                    System.out.println("첫번째 페이지 입니다");
                    
                    continue;
                }
                BoardController.Instance().nowPage -=1;
                

            }else if (select == 5) { //내정보
                UserInfo();
            } else if (select == 6) { // 검색
                System.out.println("검색할 제목 입력");
                String _title = sc.nextLine();
                System.out.println("검색할 작성자 입력");
                String _writer = sc.nextLine();
                BoardController.Instance().nowPage = 0;
                ArrayList<BoardDto> btl = BoardController.Instance().SearchDetail(_writer, _title);
                if (btl.size() == 0) {
                    System.out.println("검색 결과 없음");
                }
                else
                {
                    SearchPage(btl,_writer, _title);
                }
                
            }else if(select == 7){//로그아웃
                LoginController.Instance().Logout();
            }

        }
    }
    void SearchPage(ArrayList<BoardDto> btl,String _w,String _t)
    {
        PrintBoard(btl);
        
        while (true) {
            System.out.println("1 다음 페이지 2 이전 페이지 3 뒤로가기");
            int select = sc.nextInt();
            sc.nextLine();
            if (select == 1) {
                if (!BoardController.Instance().PageCheck(1)) {
                    System.out.println("마지막 페이지 입니다");

                    continue;
                }
                BoardController.Instance().nowPage += 1;
            } else if (select == 2) {
                if (!BoardController.Instance().PageCheck(-1)) {
                    System.out.println("첫번째 페이지 입니다");

                    continue;
                }
                BoardController.Instance().nowPage -= 1;
            }else if(select == 3)
            {
                break;
            }
            PrintBoard(BoardController.Instance().SearchDetail(_w, _t));
        }
    }

    void BoardInfoMenu(BoardDto _bdt)
    {
        
        while(true)
        {
            if(AccountDao.Instance().adt.GetId().equals(_bdt.getWriter()))
            {
                System.out.println("1뒤로가기 2 수정 3 삭제 4댓글 쓰기");
                int select = sc.nextInt();
                sc.nextLine();
                if(select ==1)
                {
                    break;
                }
                else if(select == 2)
                {
                    System.out.println("제목:");
                    _bdt.setTitle(sc.nextLine());
                    System.out.println("내용 :");
                    _bdt.setContent(sc.nextLine());

                    BoardController.Instance().UpdateBoard(_bdt);
                    System.out.printf("제목:%s   작성자:%s     조회수:%d\n내용:%s\n",_bdt.getTitle(),_bdt.getWriter(),_bdt.getViewCount(),_bdt.getContent());
                    continue;
                }
                else if(select ==3)
                {
                    BoardController.Instance().DeleteBoard(_bdt.getId());
                    break;
                }else if(select ==4)
                {
                    System.out.println("댓글 내용 입력");
                    String _c = sc.nextLine();
                    
                    CommentController.Instance().AddComment(AccountDao.Instance().adt.GetId(),_c, _bdt.getId());
                    BoardInfo(_bdt);
                }
            }
            else
            {
                System.out.println("1 뒤로가기 2 댓글 쓰기");
                int select = sc.nextInt();
                sc.nextLine();
                if(select == 2)
                {
                    System.out.println("댓글 내용 입력");
                    String _c = sc.nextLine();
                    
                    CommentController.Instance().AddComment(AccountDao.Instance().adt.GetId(),_c, _bdt.getId());
                    BoardInfo(_bdt);
                }
                
                break;
            }
        }

        
    }


    void UserInfo()
    {
        
        while (true) {
            System.out.println("1 내가 쓴 글 2날짜별 작성 게시글 수 3 로그인 횟수 4뒤로가기");
            int select = sc.nextInt();
            sc.nextLine();
            if (select == 1) {
                UserInfoDetail();
                
            } else if (select == 2) {
                ArrayList<String> printcount = BoardController.Instance().GetCreateCount();
                if(printcount.size() ==0)
                {
                    System.out.println("작성된 글 없음");
                }
                else
                {
                    for(int i =0;i<printcount.size();i++)
                    {
                        System.out.println(printcount.get(i));
                    }
                }
            } else if (select == 3) {
                System.out.println("로그인 횟수: "+FileService.Instance().LoadLoginCount());
            }
            else if(select ==4)
            {
                break;
            }
        }
    }
    
    void UserInfoDetail() {
        BoardController.Instance().nowPage = 0;

        while (true) {
            PrintBoard(BoardController.Instance().MyBoard());
            System.out.println("1다음 페이지 2 이전 페이지 3 뒤로가기");
            int _se = sc.nextInt();
            sc.nextLine();
            if (_se == 1) {
                if (!BoardController.Instance().PageCheck(1)) {
                    System.out.println("마지막 페이지 입니다");

                    continue;
                }
                BoardController.Instance().nowPage += 1;
            } else if (_se == 2) {
                if (!BoardController.Instance().PageCheck(-1)) {
                    System.out.println("첫번째 페이지 입니다");

                    continue;
                }
                BoardController.Instance().nowPage -= 1;
            } else if (_se == 3) {
                break;
            }
        }
    }
    void BoardInfo(BoardDto _bdt)
    {
        System.out.printf("제목:%s   작성자:%s     조회수:%d\n내용:%s\n",_bdt.getTitle(),_bdt.getWriter(),_bdt.getViewCount(),_bdt.getContent());
        ArrayList<CommentDto> _cl = CommentController.Instance().GetCommentList(_bdt.getId());
        if(_cl.size() == 0)
        {System.out.println("☆★작성된 댓글이 없습니다★☆");}
        else
        {
            System.out.println("댓글작성자       내용           날짜");
            for(int i =0;i<_cl.size();i++)
            {
                System.out.printf("%s %20s %17s\n",_cl.get(i).getWriter(),_cl.get(i).getContent(),_cl.get(i).getCreatedate());
            }
        }
        BoardInfoMenu(_bdt);
        
    }
    void PrintBoard(ArrayList<BoardDto> _list)
    {
        System.out.printf("%s %5s %11s %20s %13s %13s", "번호", "제목", "작성자","내용" ,"작성일", "조회수\n");
        for(int i=0;i<_list.size();i++)
        {
            System.out.printf("%d %12s %11s %23s %20s %8d\n",
            _list.get(i).getId(),
            _list.get(i).getTitle(),
            _list.get(i).getWriter(),
            _list.get(i).getContent(),
            _list.get(i).getCreateDate(),
            _list.get(i).getViewCount());
        }
    }

    void SignUp()
    {
        System.out.print("아이디: ");
        String _id = sc.nextLine();
        System.out.print("비밀번호: ");
        String _pwd = sc.nextLine();
        System.out.print("비밀번호 확인: ");
        if(_pwd.equals(sc.nextLine()))
        {
            String _e = LoginController.Instance().SignUp(_id, _pwd);
            if(_e.equals(_id)){
                System.out.println("가입 성공");
            }
            else{System.out.println(_e);}
        }   
        else
        {
            System.out.println("비밀번호 불일치");
        }
        
    }
    boolean Login() 
    {
        // controller로 수정
        System.out.print("아이디: ");
        String _id = sc.nextLine();
        System.out.print("비밀번호: ");
        String _pwd = sc.nextLine();

        String loginResult = LoginController.Instance().Login(_id,_pwd);
        if(_id.equals(loginResult)){
            System.out.println(_id+" 로그인 성공");
            
            return true;
        }
        else{
            System.out.println(loginResult);
            
            return false;    
        }
        
    }
}
