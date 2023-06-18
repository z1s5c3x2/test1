package javadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DBC {
	private Connection con;

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
			psmt.setString(2,org.apache.commons.codec.digest.DigestUtils.sha256Hex(_pwd));

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
				
				if (org.apache.commons.codec.digest.DigestUtils.sha256Hex(_pwd).equals(rs.getString(1)))
				{
					return "로그인 성공";
				}
				else
				{
					return "비밀번호 불일치";
				}
			}
			else
			{
				
				return "가입된 아이디가 없음";
			}
					
				
			
		} catch (Exception e) {
			// TODO: handle exception
			return "검색오류"+e.getMessage();
		}
		
		
	}
}
