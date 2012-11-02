package collection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import rdfising.TweetRDFModel;
import serviceStart.ServiceVariables;
import extraction.AnnotatedTweet;
import extraction.HashtagExtractor;
import extraction.NamedEntityExtractor;
import firstInterlinking.LinksExtractor;
import firstInterlinking.SILKAPICaller;

/**
 * This class reads a text file containing tweets and sets different metadata of tweets into java objects
 * 
 * @author Sajin
 */

public class TextFileReader {

	public static void readTextFile(ServiceVariables variables) throws IOException
	{
		String readFile = variables.getReadFile();
		String nerClassifier = variables.getNerClassifier();
		String performInterlinking = variables.getPerformInterlinking();

		FileInputStream fstream = new FileInputStream(readFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		NamedEntityExtractor tags = new NamedEntityExtractor();
		AnnotatedTweet tweet = new AnnotatedTweet();
		HashtagExtractor extractor = new HashtagExtractor();
		int i = 1000;
		while ((strLine = br.readLine()) != null)   {
			HashMap<String, ArrayList<String>> entities = tags.getEntities(strLine, nerClassifier);
			tweet.setEntities(entities);
			tweet.setId(i);
			tweet.setText(strLine);
			tweet.setHashtagsNew(extractor.extract(strLine));
			TweetRDFModel.tweetToTriples(tweet, variables);
			i++;
		} 
		in.close();
		if (performInterlinking.equals("yes")){
			ArrayList<String> silklslFiles = variables.getSilkLslFiles();
			SILKAPICaller.callSILK(silklslFiles);
			LinksExtractor.setSilkLinks(variables);
		}
	}
}
