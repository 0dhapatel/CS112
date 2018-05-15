package lse;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class LSEDriver {

	/**
	 * @param args
	 * @throws IOException 
	 */
		// TODO Auto-generated method stub
		static Scanner sc = new Scanner(System.in);
		
		static String getOption() 
		{
			System.out.print("getKeyWord(): ");
			String response = sc.next();
			return response;
		}
		
		public static void main(String args[]){
			LittleSearchEngine lse = new LittleSearchEngine();
			
			try {
			System.out.println(lse.loadKeywordsFromDocument("AliceCh1.txt"));
			}
			catch (FileNotFoundException e)
			{}
			
			try
			{
				lse.makeIndex("docs.txt", "noisewords.txt");
			} 
			catch (FileNotFoundException e)
			{}
			
			/*for (String hi : lse.keywordsIndex.keySet())
				System.out.println(hi+" "+lse.keywordsIndex.get(hi));*/
			
		System.out.println(lse.getKeyword("tIme!"));
			
			System.out.println(lse.top5search("deep", "world"));
		}

}
