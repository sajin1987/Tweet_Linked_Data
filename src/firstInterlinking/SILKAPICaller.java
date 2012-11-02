package firstInterlinking;

import java.io.File;
import java.util.ArrayList;
import de.fuberlin.wiwiss.silk.Silk;

/**
 * This class calls SILK Single Machine to start the process of interlinking entities present in tweet text
 * to the similar entites present in DBPedia dataset.
 * 
 * @author Sajin
 */
public class SILKAPICaller {

	public static void callSILK(ArrayList<String> silklslFiles) {

		for (String silklslFile: silklslFiles){
			File configFile = new File(silklslFile);
			Silk.executeFile(configFile, "orgs", 1, true);
		}
	} 
}
