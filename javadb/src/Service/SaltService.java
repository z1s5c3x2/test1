package Service;


public class SaltService extends Thread  {
    private static SaltService instance;
	public static SaltService Instance()
	{
		if(instance == null)
		{
			instance = new SaltService();
		}
		return instance;
	}
    
	private static Long getTime;
    private final int updateCycle = 5000; //1000 = 1sec
    
    public void run() {

        try {
            getTime = FileService.Instance().GetSaltUpdateTime();
            while (true) {
                // System.currentTimeMillis() 1000 =1sec
                Thread.sleep(1000);
                if (System.currentTimeMillis() > getTime+updateCycle) {
                    // System.out.println(System.currentTimeMillis());
					//System.out.printf("%d ? %d\n",getTime+updateCycle,System.currentTimeMillis());
                    FileService.Instance().SaltSave(System.currentTimeMillis(),RandomSalt());
					getTime = System.currentTimeMillis();

                }
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public String RandomSalt()
	{
		String R = "1234567890qwertyuiop[]as;dlfkgjhzx/c.,v./mbn\"'?><}{=-+_/*-`~"; // salt에 들어갈 문자열
		String _salt = "";
		int _saltSize = 20;
		
		for(int i=0;i<_saltSize;i++)
		{
			_salt += R.charAt((int)(Math.random()*R.length()));
		}
		
		return _salt;
		
	}

}
