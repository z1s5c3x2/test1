package Model.Dto;
import java.sql.Date;
public class BoardDto {
    private int id;
    private String writer;
    private String title;
    private String content;
    private Date createDate;
    private int viewCount;
    public static final int pageSize =3;
    
    public BoardDto(){}
    public BoardDto(int _id,String _w, String _t,String _c,Date _d,int _v)    
    {
        super();
        this.id =_id;
        this.writer=_w;
        this.title=_t;
        this.content=_c;
        this.createDate=_d;
        this.viewCount=_v;
        
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWriter() {
        return this.writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getViewCount() {
        return this.viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    
}
