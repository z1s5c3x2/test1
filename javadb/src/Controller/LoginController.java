package Controller;

import Model.Dao.AccountDao;
import Model.Dao.DBDao;
import Model.Dto.AccountDto;
import Service.FileService;
import Service.HashService;

public class LoginController {

    private static LoginController instance;
	public static LoginController Instance()
	{
		if(instance == null)
		{
			instance = new LoginController();
		}
		return instance;
	}


    public String Login(String _id,String _pwd)
    {
        String _e =AccountDao.Instance().AccountLogin(_id,_pwd);
		if(_e.equals(_id))
		{
			AccountDao.Instance().adt = new AccountDto();
			AccountDao.Instance().adt.SetId(_id);
			FileService.Instance().SaveLoginLog(_id,DBDao.Instance().GetDBTime());
		}
        return _e;
    }
	public String SignUp(String _id,String _pwd)
    {
        String _e = AccountDao.Instance().CheckAccount(_id);
		if(_e.equals(""))
		{
			AccountDao.Instance().CreateUserAccount(_id, HashService.Instance().GetHash256(_id, _pwd));
			return _id;
		}
        return _e;
    }
	public void Logout()
	{
		AccountDao.Instance().adt.SetId("");
	}
    
}
