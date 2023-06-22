package javadb;

import java.util.ArrayList;
import java.util.Objects;

public class Board {
	// private static Board board = new Board();
	// public static Board getInstance() {return board;}
	private int id;
	private String user;
	private String title;
	private String content;
	private String createData;
	private int viewCount;
	
	public Board(int _id,String _user,String _title,String _content,String _createData,int _count)
	{
		this.id = _id;
		this.user = _user;
		this.title = _title;
		this.content = _content;
		this.createData = _createData;
		this.viewCount = _count;
	}
	public String[] GetBoard()
	{
		String[] _strary = {Integer.toString(id),user,title,content,createData,Integer.toString(viewCount)};
		return _strary;
	}

	
	
	
	

}
