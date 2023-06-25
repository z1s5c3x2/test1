package Model.Dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
public class DBDao {
    public Connection con;
    private ResultSet rs;
	private PreparedStatement ps;
    
    private static DBDao instance;
	public static DBDao Instance()
	{
		if(instance == null)
		{
			instance = new DBDao();
		}
		return instance;
	}
    
    public String DBConnect()
    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
            return "성공";
        } catch (Exception e) {

            return e.getMessage();
        }
    }

    public Date GetDBTime()
    {
        Date _d = new Date(0);
        try {
            String sql = "select now()";
            ps = con.prepareStatement(sql);
			ps.execute();
			rs = ps.executeQuery();
			rs.next();
			_d = rs.getDate(1);
			
			
			
        } catch (Exception e) {
            // TODO: handle exception
        }
        return _d;
    }

}
