package rdfising;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import serviceStart.ServiceVariables;
import storing.FileStorage;
import storing.SDBStorage;
import twitter4j.HashtagEntity;
import twitter4j.URLEntity;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import extraction.AnnotatedTweet;

/**
 * 
 * The Class gets an annotated tweet and gives back rdf triples 
 * 
 * @author sajin
 */
public class TweetRDFModel {

	private final static Logger LOG = Logger.getLogger(TweetRDFModel.class);
	private static Model model = ModelFactory.createDefaultModel();
	static ArrayList<Model> arrModel = new ArrayList<Model>();

	public static List<Triple> tweetToTriples(AnnotatedTweet tweet, ServiceVariables variables){
		List<Triple> triples = new ArrayList<Triple>(); 
		Triple triple;
		long id = tweet.getId();
		String idString = String.valueOf(id);
		String author = tweet.getUser();
		String text = tweet.getText();
		String date = tweet.getDate();

		Resource tweetIdJena = model.createResource("http://www.deritweetproject.com/tweets/tweet/"+idString);

		String fileType = variables.getFileType();
		String storeFile = variables.getStoreFile();
		String processType = variables.getProcessType();

		triple = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", "<http://rdfs.org/sioc/types#MicroblogPost>");
		triple.setTripleJena(tweetIdJena, model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://rdfs.org/sioc/types#MicroblogPost"));
		triples.add(triple);

		triple = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://rdfs.org/sioc/ns#content>", "\""+replaceSlash(text)+"\"");
		triple.setTripleJena(tweetIdJena, model.createProperty("http://rdfs.org/sioc/ns#content"), model.createLiteral(replaceSlash(text)));
		triples.add(triple);

		HashMap<String, ArrayList<String>> entities = tweet.getEntities();
		//for each entity extracted from the tweet generate triples
		try{
			for(String person: entities.get("person")){
				Triple triplePerEntity = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://rdfs.org/sioc/ns#topic>", "<http://www.deritweetproject.com/persons/person/"+replaceSpace(person)+">");
				triplePerEntity.setTripleJena(tweetIdJena, model.createProperty("http://rdfs.org/sioc/ns#topic"), model.createResource("http://www.deritweetproject.com/persons/person/"+replaceSpace(person)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/persons/person/"+replaceSpace(person)), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(person)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/persons/person/"+replaceSpace(person)), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://xmlns.com/foaf/0.1/Person")));
				triples.add(triplePerEntity);
			}

			for(String loc: entities.get("loc")){
				Triple tripleLocEntity = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://rdfs.org/sioc/ns#topic>", "<http://www.deritweetproject.com/locations/location/"+replaceSpace(loc)+">");
				tripleLocEntity.setTripleJena(tweetIdJena, model.createProperty("http://rdfs.org/sioc/ns#topic"), model.createResource("http://www.deritweetproject.com/locations/location/"+replaceSpace(loc)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/locations/location/"+replaceSpace(loc)), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(loc)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/locations/location/"+replaceSpace(loc)), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing")));
				triples.add(tripleLocEntity);
			}

			for(String org: entities.get("org")){
				Triple tripleOrgEntity = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://rdfs.org/sioc/ns#topic>", "<http://www.deritweetproject.com/organizations/organization/"+replaceSpace(org)+">");
				tripleOrgEntity.setTripleJena(tweetIdJena, model.createProperty("http://rdfs.org/sioc/ns#topic"), model.createResource("http://www.deritweetproject.com/organizations/organization/"+replaceSpace(org)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/organizations/organization/"+replaceSpace(org)), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(org)));
				model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/organizations/organization/"+replaceSpace(org)), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://xmlns.com/foaf/0.1/Organization")));
				triples.add(tripleOrgEntity);
			}

		}catch(NullPointerException e){
			LOG.info(idString + " No Entities found");
		}

		if (processType.equals("api")){
			HashtagEntity[] hashtags = tweet.getHashtags();
			//for each hashtag from the tweets generate triples
			try{
				for(HashtagEntity htag: hashtags){
					Triple tripleTag = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://commontag.org/ns#tagged>", "<http://twitterproject.org/hashtag/"+htag.getText()+">");
					tripleTag.setTripleJena(tweetIdJena, model.createProperty("http://commontag.org/ns#tagged"), model.createResource("http://twitterproject.org/hashtag/"+htag.getText()));
					model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/hashtags/hashtag/"+replaceTag(htag.getText())), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(replaceTag(htag.getText()))));
					model.add(model.createStatement(model.createResource("http://twitterproject.org/hashtag/"+htag.getText()), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://commontag.org/ns#Tag")));

					triples.add(tripleTag);
				}
			}
			catch(NullPointerException e){
				LOG.info(idString+" No Hash tags found");
			}
		}

		else {
			ArrayList<String> hashtagsNew = tweet.getHashtagsNew();
			//for each hashtag from the tweets generate triples
			try{
				for(String htagnew: hashtagsNew){
					Triple tripleTag = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://commontag.org/ns#tagged>", "<http://www.deritweetproject.com/hashtags/hashtag/"+replaceTag(htagnew)+">");
					tripleTag.setTripleJena(tweetIdJena, model.createProperty("http://commontag.org/ns#tagged"), model.createResource("http://www.deritweetproject.com/hashtags/hashtag/"+replaceTag(htagnew)));
					model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/hashtags/hashtag/"+replaceTag(htagnew)), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(replaceTag(htagnew))));
					model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/hashtags/hashtag/"+replaceTag(htagnew)), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://commontag.org/ns#Tag")));
					triples.add(tripleTag);
				}
			}
			catch(NullPointerException e){
				LOG.info(idString+" No Hash tags found");
			}
		}

		URLEntity[] urls = tweet.getUrls();
		//for each url from the tweets generate triples
		try{
			for(URLEntity url: urls){
				URL urlString = url.getURL();
				Triple tripleURL = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://twitterproject.org/ns#has_url>", "<"+urlString+">");
				tripleURL.setTripleJena(tweetIdJena, model.createProperty("http://www.deritweetproject.com/ns#has_url"), model.createResource(urlString.toString()));
				triples.add(tripleURL);
			}
		}
		catch(NullPointerException e){
			LOG.info(idString+" No URLs found");
		}

		if (author!=null) {
			triple = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://rdfs.org/sioc/ns#has_creator>", "<http://twitterproject.org/user/"+replaceSpace(author)+">");
			triple.setTripleJena(tweetIdJena, model.createProperty("http://rdfs.org/sioc/ns#has_creator"), model.createResource("http://twitterproject.org/user/"+replaceSpace(author)));
			model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/authors/author/"+replaceSpace(author)), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createLiteral(author)));
			model.add(model.createStatement(model.createResource("http://www.deritweetproject.com/authors/author/"+replaceSpace(author)), model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.createResource("http://rdfs.org/sioc/ns#UserAccount")));
			triples.add(triple);
		}

		if (date!=null){
			triple = new Triple("<http://www.deritweetproject.com/tweets/tweet/"+idString+">", "<http://purl.org/dc/elements/1.1/date>", "\""+replaceSlash(date)+"\"");
			triple.setTripleJena(tweetIdJena, model.createProperty("http://purl.org/dc/elements/1.1/date"), model.createLiteral(replaceSlash(date)));
			triples.add(triple);
		}

		for(Triple triple1: triples){
			model.add(model.createStatement(triple1.getSubjectJena(), triple1.getPredicateJena(), triple1.getObjectJena()));

		}
		arrModel.add(model);

		if (fileType.equals("TextFile")) {
			try {
				FileStorage store = new FileStorage();
				store.storeRDF(arrModel, storeFile);
				arrModel.clear();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

		else if (fileType.equals("SDB")) {
			SDBStorage storeSDB = new SDBStorage();
			storeSDB.storeToSDB(arrModel);
			arrModel.clear();
		}

		LOG.debug("Generated "+triples.size()+" triples. ");

		return triples;
	}

	public static String replaceSlash(String toReplace){
		String withoutForwardSlash = toReplace.replace("\\", "\\\\");
		String withoutDoubleQuotes = withoutForwardSlash.replace("\"", "\\\"");
		String withoutLineBreaks = withoutDoubleQuotes.replace("\n", "");
		return withoutLineBreaks;
	}

	public static String replaceSpace(String creator){
		String withoutSpace = creator.replace(" ", "_");
		return withoutSpace;
	}

	public static String replaceTag(String htag){
		String withoutTag = htag.replace("#", "");
		return withoutTag;
	}
}
