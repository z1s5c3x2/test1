package Model.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Service.HashService;
import Model.Dto.AccountDto;

public class AccountDao {
    private static AccountDao instance;
	public static AccountDao Instance()
	{
		if(instance == null)
		{
			instance = new AccountDao();
		}
		return instance;
	}
    public AccountDto adt;
    private ResultSet rs;
    private PreparedStatement ps;

    public String AccountLogin(String _id,String _pwd)
    {
        try {
            String sql = "select userpwd from useraccount where userid=?";
            ps = DBDao.Instance().con.prepareStatement(sql);
            ps.setString(1, _id);
            
            rs = ps.executeQuery();

            
            if(rs.next())
            {

                if(HashService.Instance().GetHash256(_id, _pwd).equals(rs.getString("userpwd"))){return _id;}
                else{return "비밀번호 불일치";}
            }   
            else{return "일치하는 아이디 없음";}

        } catch (Exception e) {
            // TODO: handle exception
            
        }
        
        return "";
    }
    //
    public String CheckAccount(String _id)
    {
        String sql = "select userid from useraccount where userid=?";
        try {
            ps = DBDao.Instance().con.prepareStatement(sql);
            ps.setString(1, _id);
            rs = ps.executeQuery();

            if(rs.next()) {   return "중복 아이디";  }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("가입 에러"+e.getMessage());
            
        }
        return "";
    }
    public String CreateUserAccount(String _id,String _sha256pwd)
    {
        
        try {
            String sql = "insert into useraccount (UserId,UserPwd) values (?,?)";
            ps = DBDao.Instance().con.prepareStatement(sql);
			ps.setString(1,_id); 
			ps.setString(2,_sha256pwd);

			ps.execute();
            return _id;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        
    }
    public void WriteUserLog()
    {
        
    }
}
