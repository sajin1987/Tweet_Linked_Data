package secondInterlinking;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class StopwordsRemoval {

	public static void main(String[] args) throws IOException {
		// Create an output file
		File output=new File("Output.txt");
		if (output.exists())
		{
			output.delete();
			output.createNewFile();
		}
		else
			output.createNewFile();

		HashMap <String, Integer> hm=new HashMap<String, Integer>();

		//Read the Stop Word File
		BufferedReader inStop=new BufferedReader(new InputStreamReader(new FileInputStream("")));
		String stopper ="";
		while ((stopper=inStop.readLine())!=null)
		{
			hm.put(stopper, new Integer(2));
		}

		//Read the input file
		BufferedReader inFile=new BufferedReader(new InputStreamReader(new FileInputStream("")));
		String line="";
		Vector<String> words=new Vector<String>();
		while ((line=inFile.readLine())!=null)
		{
			line=line.toLowerCase();
			Pattern pattern = Pattern.compile("[^a-z]");
			Matcher matcher=pattern.matcher(line);
			String s=matcher.replaceAll(" ");
			StringTokenizer st=new StringTokenizer(s);
			while (st.hasMoreTokens())
			{
				String str=st.nextToken();
				if (!hm.containsKey(str))
					words.add(str);
			}
		}
		for (int i=0;i<words.size();i++)
		{
			PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter(output,true)));
			out.println(words.get(i));
			out.close();
		}
	}
}


