package javadb;
import java.util.Scanner;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DBC {
	private Connection con;
	Scanner sc = new Scanner(System.in);
	private ResultSet rs;
	private PreparedStatement psmt;
	public DBC()
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");

		} catch (Exception e) {
			System.out.println("연결오류"+e.getMessage());
		}
	}
	
	
	//org.apache.commons.codec.digest.DigestUtils.sha256Hex("asdf")
	public String AddUser(String _id ,String _pwd)
	{
		try {
			String sql = "insert into useraccount (UserId,UserPwd) values (?,?)";
			psmt = con.prepareStatement(sql);
			psmt.setString(1,_id); 
			psmt.setString(2,CustomSha256hash.GetHash256(_id, _pwd));

			psmt.execute();


		}
		catch(Exception e)
		{
			return "가입 에러\n"+e.getMessage();
		}
		return "성공";
		
	}
	public String UserLogin(String _id ,String _pwd)
	{
		try {
			//String sql = "SELECT * FROM UserAccount WHERE UserId= '" + _id + "'and UserPwd='" + _pwd+"'";
			
			String sql = "select UserPwd from useraccount where UserId=?";
			

			psmt = con.prepareStatement(sql);
			
			psmt.setString(1,_id); 
			//System.out.println(psmt);
			
			rs = psmt.executeQuery();

			if(rs.next())
			{
				
				if (CustomSha256hash.GetHash256(_id, _pwd).equals(rs.getString(1)))
				{
					return _id;
				}
				else
				{
					System.out.println("비밀번호 불일치");
					return "";
				}
			}
			else
			{
				System.out.println("가입된 아이디가 없음");
				return "";
			}
					
				
			
		} catch (Exception e) {
			// TODO: handle exception
			return "검색오류"+e.getMessage();
		}
	}
	public ArrayList<Board> ViewAllBoard()
	{
		ArrayList<Board> _blist = new ArrayList<Board>();
		try {
			String sql = "select * from board";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			
			while(rs.next())
			{
				_blist.add(new Board(rs.getInt("id"), rs.getString("writer"), rs.getString("title"), rs.getString("content"), rs.getString("createdate"), rs.getInt("viewcount")));
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return _blist;
	}
	//board
	public boolean WriteBoard(String _user,String _title,String _content)
	{
		try {
			String sql = "insert into board (writer,title,content,createdate,viewcount) values (?,?,?,date_format(now(), '%y-%m-%d'),0)";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1,_user);
			psmt.setString(2,_title);
			psmt.setString(3,_content);
			psmt.execute();
			
			System.out.println("성공");
			return true;

		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;//"가입 에러\n"+e.getMessage();
		}
		
			
		
	}
	public void SearchDateBoard(String _User)
	{
		try {
			
			String sql = "select * from board where user like ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, _User);
			
			psmt.execute();
			rs = psmt.executeQuery();
			System.out.println("번호 제목 작성자 조회수");
			SimpleDateFormat dtF = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			Date dt = (Date) dtF.parse("1");
			
			cal.setTime(dt);
			
			while(rs.next())
			{
				
			}

		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		
	}
	
	public String SearchIDBoard(int _id)
	{
		try {
			
			String sql = "select title,writer,viewcount,content from board where id like ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, "%"+Integer.toString(_id)+"%");
			
			psmt.execute();
			rs = psmt.executeQuery();
			rs.next();
			System.out.println("제목 작성자 조회수");
			int _view = rs.getInt("viewcount")+1;
			String retUser = rs.getString("Writer");
			System.out.printf("%s %s %d\n내용 : %s\n",rs.getString("title"),retUser,_view,rs.getString("content"));
			sql = "update board set viewcount =? where id=?";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, _view);
			psmt.setInt(2, _id);
			psmt.executeUpdate();
			return retUser;
			
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		return "";
		
	}
	
	public void SearchWriterBoard(String _User)
	{
		try {
			String sql = "select id,title,content,createdate,viewcount from board where writer like ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, "%"+_User+"%");
			
			psmt.execute();
			rs = psmt.executeQuery();
			System.out.println("내가 쓴 글 \n 번호 제목  내용     날짜     조회수");
			while(rs.next())
			{
				System.out.printf("%d %s %s %s %d\n",rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("createdate"), rs.getInt("viewcount"));
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		
	}
	public void UpdeateBoard(int _UpDel,int _id)
	{
		try {
			String sql ="";
			if(_UpDel == 2)
			{
				sql = "update board set content =? where id=?";
				psmt = con.prepareStatement(sql);
				System.out.println("내용 입력");
				String _con = sc.nextLine();
				
				psmt.setString(1, _con);
				psmt.setInt(2,_id);
				psmt.executeUpdate();
				
			}
			else
			{
				sql = "delete from board where id = ?";
				psmt = con.prepareStatement(sql);
				psmt.setInt(1,_id);
				psmt.executeUpdate();
			}			
			
			
			
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		
	}
	
	
	
}

