package Controller;

import java.util.ArrayList;
import Model.Dao.CommentDao;
import Model.Dto.CommentDto;
public class CommentController {
    private static CommentController instance;
	public static CommentController Instance()
	{
		if(instance == null)
		{
			instance = new CommentController();
		}
		return instance;
	}
    public ArrayList<CommentDto> GetCommentList(int _boardId)
    {
        return CommentDao.Instance().GetCommentList(_boardId);
    }
    public void AddComment(String _writer,String  _com,int _boardId)
    {
        CommentDao.Instance().AddComment(_writer, _com, _boardId);
    }
}
