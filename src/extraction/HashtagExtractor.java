package extraction;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class extracts hashtags from tweet text
 * 
 * @author Sajin
 */

public class HashtagExtractor {

	public ArrayList<String> extract(String text) {
		ArrayList<String> tags = new ArrayList<String>();
		Pattern p = Pattern.compile("(#[a-zA-Z_0-9\\-]+)");
		Matcher m = p.matcher(text);  
		while (m.find()) {
			String tag = m.group();
			tags.add(tag);
		}
		return tags;
	}
}

