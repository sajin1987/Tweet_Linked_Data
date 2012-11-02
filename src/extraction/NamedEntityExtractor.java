package extraction;

import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class takes the tweet string as input and uses stanford ner classifier to generate a xml string tagged with different entities.
 *
 * @author Sajin
 */

public class NamedEntityExtractor {

	public HashMap<String, ArrayList<String>> getEntities(String str, String serializedClassifier) {

		AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		String xmlString = classifier.classifyWithInlineXML(str);
		System.out.println(xmlString);
		XMLParser parseString = new XMLParser();
		return parseString.getTagValues("<output><S>" + xmlString + "</S></output>");   //extracting entities from the tweet string
	}
}

