package Controller;
import Model.Dao.AccountDao;
import Model.Dao.BoardDao;
import Model.Dto.BoardDto;

import java.util.ArrayList;
public class BoardController {
    private static BoardController instance;
	public static BoardController Instance()
	{
		if(instance == null)
		{
			instance = new BoardController();
		}
		return instance;
	}
    public int nowPage;


    public void WriteBoardInit(String _title, String _content)
    {
        String _w = AccountDao.Instance().adt.GetId();
        BoardDao.Instance().WriteBoard(_w,_title,_content);

    }
    public void UpdateBoard(BoardDto _bdt)
    {
        BoardDao.Instance().UpdateBoard(_bdt);
    }
    public void DeleteBoard(int _id)
    {
        BoardDao.Instance().DeleteBoard(_id);
    }
    public ArrayList<BoardDto> MyBoard()
    {
        return BoardDao.Instance().GetMyBoard(AccountDao.Instance().adt.GetId(),nowPage*BoardDto.pageSize);
    }
    public boolean PageCheck(int _page)
    {
        if(nowPage+_page < 0 || nowPage+_page > 10)
        {
            return false;
        }
        return true;
    }
    public ArrayList<BoardDto> SearchDetail(String _User,String _title)
    {
        return BoardDao.Instance().SearchWriterandTitle(_User, _title, nowPage*BoardDto.pageSize);
    }
    public ArrayList<String> GetCreateCount()
    {
        return BoardDao.Instance().SearchMyBoardCount(AccountDao.Instance().adt.GetId());
    }
    public ArrayList<BoardDto> ViewBoard()
    {
        ArrayList<BoardDto> _glist = new ArrayList<BoardDto>();
        
        _glist = BoardDao.Instance().GetBoard(nowPage*BoardDto.pageSize);    

        return  _glist;
    }
    public BoardDto SearchIdBoard(int _num)
    {
        return BoardDao.Instance().GetIdBoard(_num);
    }

}
