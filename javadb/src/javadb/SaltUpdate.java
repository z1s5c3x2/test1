package javadb;

public class SaltUpdate extends Thread  {
	public Long getTime;
	public void run() 
	{

		try
		{
		while(true)
		{
			// System.currentTimeMillis() 1000 =1sec 
			Thread.sleep(1000);
			if(System.currentTimeMillis() > getTime)
			{
				//System.out.println(System.currentTimeMillis());
				
				getTime = CustomSha256hash.RandomSalt(System.currentTimeMillis());
				
			}
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
