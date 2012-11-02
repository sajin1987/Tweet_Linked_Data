package firstInterlinking; 


import insertingLinks.FileLinkInserter;
import insertingLinks.SDBLinkUpdater;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import secondInterlinking.DomainEntityQuerier;
import serviceStart.ServiceVariables;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * This class extracts the dbpedia links from the files generated after running SILK.
 * 
 * @author Sajin
 */

public class LinksExtractor {

	private static Model rdfmodel = ModelFactory.createDefaultModel();

	public static void setSilkLinks(ServiceVariables variables) throws IOException {

		ArrayList<String> silkGeneratedFiles = variables.getSilkGeneratedFiles();
		String storeFile = variables.getStoreFile();
		String performRedirect = variables.getPerformRedirect();
		String storeRedirectFile = variables.getStoreRedirectFile();
		String performSecondInterlinking = variables.getPerformSecondInterlinking();
		final String fileType = variables.getFileType();
		final String storeFirstLinkFile = variables.getStoreFirstLinkFile();

		rdfmodel.read("file:///" +storeFile);

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {			

				FileLinkInserter insert = new FileLinkInserter();
				SDBLinkUpdater update = new SDBLinkUpdater();
				ArrayList<String> links = new ArrayList<String>();

				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("entity1")) {
						String dbp = attributes.getValue("rdf:resource");			
						links.add(dbp);
					}

					if (qName.equalsIgnoreCase("entity2")) {
						String local = attributes.getValue("rdf:resource");
						links.add(local);

						if (fileType.equals("TextFile")) {
							try {
								insert.insertTriples(links, rdfmodel, storeFirstLinkFile);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else if (fileType.equals("SDB")) {
							update.updateTriples(links);
						}

						links.clear();
					}
				}
			};
			for (String silkGeneratedFile: silkGeneratedFiles){
				saxParser.parse(silkGeneratedFile, handler);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		if (performRedirect.equals("yes")) {
			RedirectEntityLinker.setRedirectEntities(storeFirstLinkFile, storeRedirectFile);
		}
		else if (performRedirect.equals("no")) {			
			if (performSecondInterlinking.equals("yes")) {
				DomainEntityQuerier.setDomainEntityValues(variables);
			}
		}
	}

	public static void main (String[] args) throws IOException {

		ServiceVariables variables = new ServiceVariables();

		ArrayList<String> silkGeneratedFiles = new ArrayList<String>();

		silkGeneratedFiles.add("C:/masters/twitter_linked/silk_2.5.1/accepted_linksPer.xml");
		silkGeneratedFiles.add("C:/masters/twitter_linked/silk_2.5.1/accepted_linksOrg.xml");
		silkGeneratedFiles.add("C:/masters/twitter_linked/silk_2.5.1/accepted_linksLoc.xml"); 

		variables.setSilkGeneratedFiles(silkGeneratedFiles);
		variables.setStoreFile("file:///C:/masters/twitter_linked/silk_2.5.1/hetunew98.rdf");
		variables.setStoreFirstLinkFile("C:/masters/twitter_linked/silk_2.5.1/firstLink.rdf");
		variables.setPerformRedirect("yes");
		variables.setFileType("TextFile");
		variables.setStoreSecondLinkFile("C:/masters/twitter_linked/silk_2.5.1/secondLink.rdf");

		LinksExtractor.setSilkLinks(variables);
	}
}