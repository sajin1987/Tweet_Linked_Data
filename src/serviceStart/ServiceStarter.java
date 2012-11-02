package serviceStart;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import collection.CSVFileReader;
import collection.StreamingAPIReader;
import collection.TextFileReader;
import twitter4j.TwitterException;

/**
 * This class starts the process of converting tweets to linked data
 * @author Sajin
 *
 */
public class ServiceStarter {

	public static void main (String[] args) throws IOException, TwitterException{

		Properties props = new Properties();

		props.load(new FileReader("config.properties"));

		String processType = props.getProperty("processType");
		String readFile = props.getProperty("readFile");
		String storeFile = props.getProperty("storeFile");
		String nerClassifier = props.getProperty("nerClassifier");
		String fileType = props.getProperty("fileType");
		String performInterlinking = props.getProperty("performInterlinking");
		String silklslPerFile = props.getProperty("silklslPerFile");
		String silklslOrgFile = props.getProperty("silklslOrgFile");
		String silklslLocFile = props.getProperty("silklslLocFile");
		String silkGeneratedPerFile = props.getProperty("silkGeneratedPerFile");
		String silkGeneratedOrgFile = props.getProperty("silkGeneratedOrgFile");
		String silkGeneratedLocFile = props.getProperty("silkGeneratedLocFile");
		String storeFirstLinkFile = props.getProperty("storeFirstLinkFile");
		String performRedirect = props.getProperty("performRedirect");
		String storeRedirectFile = props.getProperty("storeRedirectFile");
		String performSecondInterlinking = props.getProperty("performSecondInterlinking");
		String storeSecondLinkFile = props.getProperty("storeSecondLinkFile");
		String domainEntity = props.getProperty("domainEntity");
		
		ArrayList<String> silklslFiles = new ArrayList<String>();
		ArrayList<String> silkGeneratedFiles = new ArrayList<String>();
		
		silklslFiles.add(silklslPerFile);
		silklslFiles.add(silklslOrgFile);
		silklslFiles.add(silklslLocFile);
		
		silkGeneratedFiles.add(silkGeneratedPerFile);
		silkGeneratedFiles.add(silkGeneratedOrgFile);
		silkGeneratedFiles.add(silkGeneratedLocFile);

		ServiceVariables variables = new ServiceVariables();

		variables.setProcessType(processType);
		variables.setReadFile(readFile);
		variables.setStoreFile(storeFile);
		variables.setNerClassifier(nerClassifier);
		variables.setFileType(fileType);
		variables.setPerformInterlinking(performInterlinking);
		variables.setSilkLslFiles(silklslFiles);
		variables.setSilkGeneratedFiles(silkGeneratedFiles);
		variables.setStoreFirstLinkFile(storeFirstLinkFile);
		variables.setPerformRedirect(performRedirect);
		variables.setStoreRedirectFile(storeRedirectFile);
		variables.setPerformSecondInterlinking(performSecondInterlinking);
		variables.setStoreSecondLinkFile(storeSecondLinkFile);
		variables.setDomainEntity(domainEntity);

		// whether to read the tweets from text file, csv file or from twitter streaming api
		if (processType.equals("text")){   
			TextFileReader.readTextFile(variables);
		} 
		
		else if (processType.equals("csv")) {
			CSVFileReader.readCSVFile(variables);
		}
		
		else if (processType.equals("api")){
			StreamingAPIReader.readAPI(variables);
		}
	}
}
