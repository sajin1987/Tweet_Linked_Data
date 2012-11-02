package evaluation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadResources {

	public static void main (String[] args) throws IOException 
	{
		FileInputStream fstream = new FileInputStream("");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
	//	StoringTweets sj = new StoringTweets();
  	
		ArrayList<String> textarr = new ArrayList<String>();

		while ((strLine = br.readLine()) != null  )   {
	
			textarr.add(strLine);
			
		}
		System.out.println(textarr.size());
		in.close();
		
		EntityCount.findTriples(textarr);
	//	sj.storeTweets(textarr);
	}

}
