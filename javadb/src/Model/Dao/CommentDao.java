package Model.Dao;
import java.sql.ResultSet;
import java.util.ArrayList;

import Model.Dto.CommentDto;

import java.sql.PreparedStatement;
public class CommentDao {
    private static CommentDao instance;
	public static CommentDao Instance()
	{
		if(instance == null)
		{
			instance = new CommentDao();
		}
		return instance;
	}
    private ResultSet rs;
    private PreparedStatement ps;
    public ArrayList<CommentDto> GetCommentList(int _boardId)
    {
        ArrayList<CommentDto> comlist = new ArrayList<CommentDto>();

        try {
            String sql = "select * from comment where board_id like ? order by id desc";
            ps = DBDao.Instance().con.prepareStatement(sql);
            ps.setInt(1, _boardId);
            rs = ps.executeQuery();
            while(rs.next())
            {
                comlist.add(new CommentDto(rs.getInt("id"), rs.getString("writer"), rs.getString("content"), rs.getDate("createdate")));
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        return comlist;
    }
    public void AddComment(String _writer,String _com,int _boardId)
    {
        try {
            String sql = "insert into comment (board_id,writer,content) values(?,?,?)";
            ps = DBDao.Instance().con.prepareStatement(sql);
            ps.setInt(1, _boardId);
            ps.setString(2, _writer);
            ps.setString(3, _com);
            ps.execute();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
