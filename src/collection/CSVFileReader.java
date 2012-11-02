package collection;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import rdfising.TweetRDFModel;
import serviceStart.ServiceVariables;
import extraction.AnnotatedTweet;
import extraction.HashtagExtractor;
import extraction.NamedEntityExtractor;

/**
 * This class collects tweets from CSV file and sets different metadata of tweets into java objects
 * 
 * @author Sajin
 */

public class CSVFileReader {

	public static void readCSVFile(ServiceVariables variables) throws IOException
	{
		String readFile = variables.getReadFile();
		String nerClassifier = variables.getNerClassifier();

		FileInputStream myInput = new FileInputStream(readFile);

		POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

		HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

		HSSFSheet mySheet = myWorkBook.getSheetAt(0);

		Iterator<Row> rowIter = mySheet.rowIterator();
		NamedEntityExtractor tags = new NamedEntityExtractor();
		AnnotatedTweet tweet = new AnnotatedTweet();
		HashtagExtractor extractor = new HashtagExtractor();

		int i = 1;
		while (rowIter.hasNext())   {
			HSSFRow myRow = (HSSFRow) rowIter.next();

			String strLine = myRow.getCell(1).getStringCellValue();
			String date = myRow.getCell(0).getStringCellValue();

			if (strLine.contains("&")) {
				String nwLine = strLine.replace("&", "&amp;");
				HashMap<String, ArrayList<String>> entities = tags.getEntities(nwLine, nerClassifier);
				tweet.setEntities(entities);
			}
			else {
				HashMap<String, ArrayList<String>> entities = tags.getEntities(strLine, nerClassifier);
				tweet.setEntities(entities);
			}

			tweet.setId(i);
			tweet.setText(strLine);
			tweet.setDate(date);
			tweet.setHashtagsNew(extractor.extract(strLine));
			TweetRDFModel.tweetToTriples(tweet, variables);
			i++;
		}  
		myInput.close();
	}
}
