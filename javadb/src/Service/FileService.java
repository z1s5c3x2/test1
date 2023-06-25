package Service;
import Model.Dao.AccountDao;
import Model.Dto.FileDto;
import java.sql.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

public class FileService extends FileDto{
    private static FileService instance;
	public static FileService Instance() 
	{
		if(instance == null)
		{
			instance = new FileService();
		}
		return instance;
	}

	public String SaltSaveCheck(String _id) {
		try {
			String _salt = UserSaltLoad(_id);
			if (!_salt.equals("")) {
				return _salt;
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			
		}
		return UserSaltSave(_id);
	}

	public Long GetSaltUpdateTime() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(saltSaveFilePath));
		Long _time = Long.parseLong(br.readLine().split(":")[0]);
		br.close();
		return _time;
	}
	public String SaltSave(Long _time,String _salt) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(saltSaveFilePath));
		bw.write(String.format("%d:%s",_time,_salt));
		bw.close();
		return _salt;
	}
	public Long SaltLoad() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(saltSaveFilePath));
		Long _salt = Long.parseLong(br.readLine().split(":")[1]);
		br.close();
		return _salt;
	}

	public String UserSaltSave(String _User)  
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(userSaltListFilePath,true));
			String _salt = SaltService.Instance().RandomSalt();
			
			bw.write(String.format("%s:%s", _User,_salt));
			bw.newLine();
			bw.close();
			return _salt;	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return "";
		}
		
	}
	public void SaveLoginLog(String _User,Date _d)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(UserLoginLogPath,true));
			bw.write(String.format("%s:%s",_User,_d.toString()));
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	public int LoadLoginCount() {
		int count = 0;
		try {

			BufferedReader br = new BufferedReader(new FileReader(UserLoginLogPath));

			while (true) {
				String _str = br.readLine();
				if (_str == null) {
					break;
				}
				if (AccountDao.Instance().adt.GetId().equals(_str.split(":")[0])) {
					count++;
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return count;
	}

    String UserSaltLoad(String _User) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(userSaltListFilePath));
		while(true)
        {
            String _br = br.readLine();
            if(_br == null)
            {
				
                break; 
            }
            if(_User.equals(_br.split(":")[0]))
			{
				br.close();
				return _br.split(":")[1];
			}
        }
		
		br.close();
		return "";
	}
}
