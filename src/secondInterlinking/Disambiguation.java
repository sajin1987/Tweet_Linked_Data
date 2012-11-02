package secondInterlinking;

import insertingLinks.FileSecondLinkInserter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.WordUtils;
import serviceStart.ServiceVariables;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class gives the disambiguation resources for entities that are present as half names. 
 * @author Sajin
 *
 */
public class Disambiguation {

	public static void startDisLinking(Set<String> valuesDomainEntity, ServiceVariables variables) throws IOException {

		String performRedirect = variables.getPerformRedirect();
		String storeRedirectFile = variables.getStoreRedirectFile();
		String storeFirstLinkFile = variables.getStoreFirstLinkFile();

		FileSecondLinkInserter insert = new FileSecondLinkInserter();

		Model rdfmodel = ModelFactory.createDefaultModel();

		if(performRedirect.equals("yes")){
			rdfmodel.read("file:///" +storeRedirectFile);
		}

		else if(performRedirect.equals("no")){
			rdfmodel.read(storeFirstLinkFile);
		}

		String storeSecondLinkFile = variables.getStoreSecondLinkFile();

		ArrayList<ArrayList<Resource>> triples = new ArrayList<ArrayList<Resource>>();

		double max = 0;
		ArrayList<Resource> entities = null;

		// query to select the distinct label values from local dataset.
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX sioc: <http://rdfs.org/sioc/ns#>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX owl: <http://www.w3.org/2002/07/owl#>\n\nSELECT DISTINCT ?entity ?type ?label ?alreadyLinked\nWHERE {\n?tweet sioc:topic ?entity.\n?entity rdf:type ?type.\n?entity rdfs:label ?label.\nOPTIONAL {?entity owl:sameAs ?alreadyLinked}\n}");
		QueryExecution qe = QueryExecutionFactory.create(query, rdfmodel);

		BagofWords bow = new BagofWords();
		try {
			ResultSet rs = qe.execSelect() ;
			while (rs.hasNext()){

				QuerySolution sol = rs.nextSolution();
				Literal label = sol.getLiteral("label");
				Resource localEntity = sol.getResource("entity");
				Resource type = sol.getResource("type");
				Resource alreadyLinked = sol.getResource("alreadyLinked");

				if(alreadyLinked==null){  // if the entity in local dataset is not already linked

					String strLabel = label.getString();
					String typeL = type.toString();

					Set<String> labels = new HashSet<String>();
					Set<Resource> dbps = new HashSet<Resource>();

					// different possible ways in which a particular label can exist in dbpedia dataset
					String labelLC = strLabel.toLowerCase();
					String labelUC = strLabel.toUpperCase();
					String labelFC = WordUtils.capitalize(strLabel);
					String labelFFC = WordUtils.capitalizeFully(strLabel);  

					//duplicate labels will not be added to the set
					labels.add(strLabel);
					labels.add(labelLC);
					labels.add(labelUC);
					labels.add(labelFC);
					labels.add(labelFFC);

					for (String lab: labels){
						String dbpType = null;
						if (typeL.equals("http://xmlns.com/foaf/0.1/Organization")) {
							dbpType = "dbpedia-owl:Organisation";
						}
						else if (typeL.equals("http://xmlns.com/foaf/0.1/Person")) {
							dbpType = "dbpedia-owl:Person";
						}
						else if (typeL.equals("http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing")) {
							dbpType = "dbpedia-owl:Place";
						} 
						// query to select the disambiguation resources from dbpedia (if they exist) for each label from local dataset
						Query query2 = QueryFactory.create("PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\nSELECT DISTINCT ?dbp\nWHERE {\n?disamb dbpedia-owl:wikiPageDisambiguates ?dbp.\n?dbp rdf:type "+dbpType+".\n?disamb rdfs:label \""+lab+"\"@en\n}");
						QueryExecution qe2 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query2);
						ResultSet rs2 = qe2.execSelect() ;
						while (rs2.hasNext()){
							QuerySolution sol2 = rs2.nextSolution();
							Resource candidateEntity = sol2.getResource("dbp");
							dbps.add(candidateEntity);     // this makes sure that the same disambiguation resource is not added more than once if the label values in dbpedia 
							//  are similar to more than one form of the same label in local dataset.
							//	System.out.println(entity + "   " + dbp);
						}
						qe2.close();
					}

					for (Resource candidateEntity: dbps){
						ProbabilityComparer compare = bow.setFeatures(candidateEntity, localEntity, valuesDomainEntity);

						if (compare.getProbability() > max) {
							max = compare.getProbability();
							entities = compare.getEntities();
						}
					}

					if (max>0) {
						triples.add(entities);
						System.out.println(max + "      " + entities);
						max = 0;
					}

					dbps.clear();
					labels.clear();
				}
			}
		}
		finally { qe.close() ; 
		}
		System.out.println(triples);
		for (ArrayList<Resource> triple: triples) {
			insert.insertTriples(triple, rdfmodel, storeSecondLinkFile);
		}

	}
}
