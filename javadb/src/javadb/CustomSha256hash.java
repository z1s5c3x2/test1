package javadb;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

public class CustomSha256hash 
{
	
	public static final String userSaltFilePath = "C:/usersalt.txt";
	public static final String saltFilePath = "C:/salt.txt";
	
	//1. salt추가 2.salt값 파일처리 혹은 DB처리 3.쓰레드로 salt값 일정 시간마다 변경
	//
	static Long getSaltUTime()throws IOException 
	{
		BufferedReader br = new BufferedReader(new FileReader(saltFilePath));
		Long _Lo = Long.parseLong(br.readLine().split(":")[0]);
		br.close();
		
		 return _Lo;
	}
	static String SaltSave(String _User) throws IOException 
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(userSaltFilePath,true));
		String _salt;
		
		//salt 파일에서 불러와 저장
		BufferedReader br = new BufferedReader(new FileReader(saltFilePath));
		_salt = br.readLine().split(":")[1];
		br.close();
		
		bw.write(String.format("%s:%s", _User,_salt));
		bw.newLine();
		bw.close();
		return _salt;
	}

	static String SaltLoad(String _User) throws IOException
	{
		
		BufferedReader br = new BufferedReader(new FileReader(userSaltFilePath));

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
		return SaltSave(_User);
	}
	public static String GetHash256(String _user,String _pwd) throws IOException
	{
		//salt값이 없을시 생성 있으면 파일에서 불러오기
		
		String salt = SaltLoad(_user); //salt 
		_pwd +=salt;
		
		
		//System.out.println(salt);

        //+salt; 

		ArrayList<String> hashmsg = new ArrayList<String>();
		
		//padding parsing
		Padding(hashmsg,_pwd,_pwd.length()); 

		//H값 가장 작은 소수 순서대로 8개를 루트를 씌운 후 소수점 아래 32비트 저장후 16진수로 저장
		int h0 = 0x6a09e667;  
    	int h1 = 0xbb67ae85;
    	int h2 = 0x3c6ef372;
    	int h3 = 0xa54ff53a;
    	int h5 = 0x9b05688c;
    	int h4 = 0x510e527f;
    	int h6 = 0x1f83d9ab;
    	int h7 = 0x5be0cd19;


		
		
		ArrayList<Integer> wList = new ArrayList<Integer>();
		ArrayList<String> WStrList = new ArrayList<String>();
		// 32비트로 쪼개진 리스트를 16개씩 새로운 리스트에 저장후 블럭수 만큼 W와 Round 연산 실행
		for(int x=0;x<hashmsg.size()/16;x++)
		{
			for(int y=x*16;y<(x+1)*16;y++) // 16개씩 자르기(16*32 512bit)
			{
				
				WStrList.add(hashmsg.get(y));
			}
			//get W
			wList = GetW(WStrList);	
			
			int a = h0;
			int b = h1;
			int c = h2;
			int d = h3;
			int e = h4;
			int f = h5;
			int g = h6;
			int h = h7;
			//Round
			for (int i = 0; i < 64; i++) {

				// h+Σ1(e)+ch(efg)+K[i]+w[i]
				int t1 = h + RodS1(e) + RodCh(e, f, g) + K[i] + wList.get(i);
				// Σ0(a) + Maj(a,b,c)
				int t2 = RodS0(a) + RodMaj(a, b, c);

				h = g;
				g = f;
				f = e;
				// e' = Σ1(e) + Ch(e,f,g) + h + W0 + K0 + d
				e = (d + t1);
				d = c;
				c = b;
				b = a;
				// a' = Σ0(a) + Maj(a,b,c) + t1
				a = (t1 + t2);

			}
			
			h0 +=a;
			h1 +=b;
			h2 +=c;
			h3 +=d;
			h4 +=e;
			h5 +=f;
			h6 +=g;
			h7 +=h;
			WStrList.clear();
		}
		//round 지금까지 구한 H,K,W를 가지고 연산하여 출력될 64개의 해시 문자열 생성
		
		String sha256HashString = String.format("%08x%08x%08x%08x%08x%08x%08x%08x",h0,h1,h2,h3,h4,h5,h6,h7).toUpperCase();
		return sha256HashString;
		/* 16진수 0으로 시작시 0을 생략하여 서식문자를 이용하여 자릿수 맞춰서 출력
		System.out.println(_tmp.toUpperCase());
		System.out.printf((Integer.toHexString(h0)+
		Integer.toHexString(h1)+
		Integer.toHexString(h2)+
		Integer.toHexString(h3)+
		Integer.toHexString(h4)+
		Integer.toHexString(h5)+
		Integer.toHexString(h6)+
		Integer.toHexString(h7)).toUpperCase());*/
    }
	//salt
	static Long RandomSalt(Long _time) throws IOException
	{
		String R = "1234567890qwertyuiop[]as;dlfkgjhzx/c.,v./mbn\"'?><}{=-+_/*-`~"; // salt에 들어갈 문자열
		String _salt = "";
		Long _savTime = _time +5000;
		int _saltSize = 20;
		
		for(int i=0;i<_saltSize;i++)
		{
			_salt += R.charAt((int)(Math.random()*R.length()));
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(saltFilePath));
		
		bw.write(String.format("%d:%s",_savTime,_salt));
		bw.close();
		return _savTime;
		
	}
	//padding
	static void Padding(ArrayList<String> _strList ,String _msg,int _msglen)
	{
		byte[] _b = _msg.getBytes(); //문자 n개 각각의 바이트를 배열에 저장

		
		for(int i=0;i<_msg.length();i++) // 가져온 바이트를 8비트로 바이너리화 저장
		{
			_strList.add(String.format("%08d",Integer.parseUnsignedInt(Integer.toBinaryString(_b[i]))));
		}
		//바이너리화된 리스트 맨 뒤에 10000000 추가 10진수 128 == 1000 0000
		_strList.add(String.format("%08d",Integer.parseUnsignedInt(Integer.toBinaryString(128))));

		
		//zeropadding 문자 1개에 8비트가 들어가, 메세지의 길이에 8을 곱한 후 마지막 원본 메세지의 길이를 저장할 공간(64bit)을 확보하여 512bit단위를 만들기 위해 빈 공간을 0으로 채움
		while((_strList.size()*8+64)%512 != 0)  
		{
			_strList.add(String.format("%08d",Integer.parseUnsignedInt(Integer.toBinaryString(0))));
			
		}
		
	
		String _64bitpad = String.format("%064d", Long.parseUnsignedLong(Long.toBinaryString(_msglen*8)));//확보된 공간 64비트에원본 메세지길이 저장
		
		for(int i =0;i<8;i++) // 64비트로 추가된 원본비트갯수를 8비트씩 쪼개서 저장 ,512bit단위 완성
		{
			_strList.add(_64bitpad.substring(i*8,(i+1)*8));
		}

		
		ArrayList<String> _tmpList = new ArrayList<String>(_strList);
		
		//parsing 위에 생성된 512bit단위의 리스트를 32비트씩 저장
		_strList.clear();
		String _str = "";
		
		
		for(int i=1;i<=_tmpList.size();i++) // 8비트단위의 512bit단위의 리스트를 8비트씩 가져와 4개씩 합쳐서 32비트단위로 저장
		{
			_str+=_tmpList.get(i-1);
			if(i%4 == 0)
			{
				_strList.add(_str);
				_str ="";
			}
			
		}

	}
	

	//K 가장 작은 소수 순서대로 64개의 소수아래의 32bit 추출
	private static final int[] K = {
		0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
    0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
    0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
    0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
    0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
    0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
    0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
    0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
	};

	//get W
	static ArrayList<Integer> GetW(ArrayList<String> _list)
	{
	
		
		//Parsing단계를 거쳐 32비트로 쪼개진 리스트를 정수형으로 저장
		ArrayList<Integer> retWList = new ArrayList<Integer>();
		//System.out.println(_list);
		//System.out.println(Integer.toBinaryString(Integer.parseUnsignedInt("10000000000000000000000000000000",2)));
		//w 0~15까지는 원본 그대로 저장
		for(String _str:_list) 
		{
			
			retWList.add(Integer.parseUnsignedInt(_str,2));
		}

		//w 16~64는 연산과정을 거쳐 저장		
		for(int i=16;i<64;i++)
		{
				
			retWList.add( GetWa1Function(retWList.get(i-2)) + retWList.get(i-7) + GetWa0Function(retWList.get(i-15)) + retWList.get(i-16)    );
		}

		return retWList;
	}
	//get W A0연산  [w-2]를 각각 오른쪽으로 7번 로테이션,오른쪽으로 18번 로테이션, 오른쪽으로 3번 비트이동 (>>> 부호에 상관없이 오른쪽으로 n번 이동후 남은 공간 0으로 채움)
	//위 3개의 값을  xor연산
	static int GetWa0Function(int _int) 
	{
		return Integer.rotateRight(_int, 7) ^ Integer.rotateRight(_int, 18) ^ (_int >>> 3);	
	}
	//get W A0연산  [w-15]를 각각 오른쪽으로 17번 로테이션,오른쪽으로 19번 로테이션, 오른쪽으로 10번 비트이동 
	//위 3개의 값을  xor연산
	static int GetWa1Function(int _int) {
		return Integer.rotateRight(_int, 17) ^ Integer.rotateRight(_int, 19) ^ (_int >>> 10);
	}


	//Round S0,S1,Maj,Ch 시프트 연산 연산 과정을 시뮬레이션하면서 봤지만 아래4개 상황과 비슷한 다른 문제가 나올때 그 문제를 보고 시프트연산으로 처리할수 없을거같음

	//Σ0 은 인자값을 2, 13, 22 만큼 각각 오른쪽으로 돌리고 그 세값을 비교하여 합이 홀수인 곳은 1, 0이거나 짝수인 곳은 0으로 표기한다
	static int RodS0(int _int) {
		return Integer.rotateRight(_int, 2) ^ Integer.rotateRight(_int, 13) ^ Integer.rotateRight(_int, 22);
	}
	//Σ1 은 인자값을 6, 11, 25 만큼 각각 오른쪽으로 돌리고 그 세값을 비교하여 합이 홀수인 곳은 1, 0이거나 짝수인 곳은 0으로 표기한다. 
	static int RodS1(int _int) {
		return Integer.rotateRight(_int, 6) ^ Integer.rotateRight(_int, 11) ^ Integer.rotateRight(_int, 25);
	}
	//Ch 는 인자로 사용된 세 값을 a,b,c라 하면 a의 비트가 1이면 그 위치는 b의 비트로, 0이면 c의 비트를 선택한다. 
	static int RodCh(int _x, int _y, int _z) {
		return (_x & _y) ^ ((~_x) & _z);
	}
	//Maj 는 인자로 사용된 세 값의 같은 위치 비트가 1이 많으면 1, 0이 많으면 0이 된다. 
	static int RodMaj(int _x, int _y, int _z) {
		return (_x & _y) ^ (_x & _z) ^ (_y & _z);
	}

}