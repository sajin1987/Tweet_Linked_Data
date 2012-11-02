package extraction; 

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

/**
 * This class takes the xml string input from Tagging class and extracts the text from different tags. Then it returns a hashmap which contains arraylist
 * of per, org and loc.
 * 
 * @author Sajin
 */

public class XMLParser{

	public HashMap<String, ArrayList<String>> getTagValues(final String xmlString){

		HashMap<String, ArrayList<String>> tagsMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> perTags = new ArrayList<String>();
		ArrayList<String> orgTags = new ArrayList<String>();
		ArrayList<String> locTags = new ArrayList<String>();

		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new java.io.StringReader(xmlString));
			Document doc = docBuilder.parse(inStream);

			// normalize text representation
			doc.getDocumentElement ().normalize ();
			NodeList listOfTweets = doc.getElementsByTagName("S");

			for(int s=0; s<listOfTweets.getLength() ; s++){

				Node firstTweetNode = listOfTweets.item(s);

				if(firstTweetNode.getNodeType() == Node.ELEMENT_NODE){

					try {       	
						Element firstTweetElement = (Element)firstTweetNode;

						NodeList personList = firstTweetElement.getElementsByTagName("PERS");
						for(int el=0; el<personList.getLength() ; el++){                        //extract the text between all the PERSON tags in the input xml string
							Element personElement = (Element)personList.item(el);
							NodeList textPNList = personElement.getChildNodes();
							String perTag = (textPNList.item(0)).getNodeValue();
							perTags.add(perTag);                                               
						}

						NodeList orgList = firstTweetElement.getElementsByTagName("ORG");
						for(int or=0; or<orgList.getLength() ; or++){
							Element orgElement = (Element)orgList.item(or);
							NodeList textORGList = orgElement.getChildNodes();
							String orgTag = (textORGList.item(0)).getNodeValue();
							orgTags.add(orgTag);
						}

						NodeList locList = firstTweetElement.getElementsByTagName("LOC");
						for(int lo=0; lo<locList.getLength() ; lo++){
							Element locElement = (Element)locList.item(lo);
							NodeList textLOCList = locElement.getChildNodes();
							String locTag = (textLOCList.item(0)).getNodeValue();
							locTags.add(locTag);
						}
					}
					catch (NullPointerException e){
						e.getMessage();
					}
				}
			}
			tagsMap.put("person", perTags);
			tagsMap.put("org", orgTags);
			tagsMap.put("loc", locTags);

		}catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());

		}catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();

		}catch (Throwable t) {
			t.printStackTrace ();
		}
		return tagsMap;
	}
}