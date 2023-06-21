package javadb;

public class SaltUpdate extends Thread  {

	public void run() 
	{
		try
		{
		while(true)
		{
			Thread.sleep(10000);
			
			CustomSha256hash.RandomSalt();
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
