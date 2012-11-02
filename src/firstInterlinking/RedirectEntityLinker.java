package firstInterlinking;

import insertingLinks.FileRedirectLinkInserter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
import org.apache.commons.lang.WordUtils;

/**
 * This class does the redirect round of interlinking when it is not done using SILK. It links those entities whose label values are similar to the label values of resources in DBpedia
 * that redirect to the original resource in DBpedia. For example, dbpedia:LibDem redirects to the original resource dbpedia:Liberal_Democrats
 * @author Sajin
 */
public class RedirectEntityLinker {

	public static void setRedirectEntities(String storeFirstLinkFile, String storeRedirectFile) throws IOException
	{

		FileRedirectLinkInserter insert = new FileRedirectLinkInserter();

		Model rdfmodel = ModelFactory.createDefaultModel();
		rdfmodel.read("file:///" +storeFirstLinkFile);

		// query to select the distinct label values from local dataset.
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX sioc: <http://rdfs.org/sioc/ns#>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX owl: <http://www.w3.org/2002/07/owl#>\n\nSELECT DISTINCT ?entity ?type ?label ?alreadyLinked\nWHERE {\n?tweet sioc:topic ?entity.\n?entity rdf:type ?type.\n?entity rdfs:label ?label.\nOPTIONAL {?entity owl:sameAs ?alreadyLinked}\n}");
		QueryExecution qe = QueryExecutionFactory.create(query, rdfmodel);
		try {
			ResultSet rs = qe.execSelect() ;
			while (rs.hasNext()){

				QuerySolution sol = rs.nextSolution();
				Literal label = sol.getLiteral("label");
				Resource entity = sol.getResource("entity");
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
					String labelFC = WordUtils.capitalize(strLabel);  // first letter of each word is capitalized
					String labelFFC = WordUtils.capitalizeFully(strLabel);  // first letter of each word is capitalized and other are made lowercase

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

						// query to select the dbpedia entities that are redirected by other dbpedia entities whose label value match the local dataset label value
						Query query2 = QueryFactory.create("PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\nSELECT ?dbp\nWHERE {\n?redirect dbpedia-owl:wikiPageRedirects ?dbp.\n?dbp rdf:type "+dbpType+".\n?redirect rdfs:label \""+lab+"\"@en.\n}");
						QueryExecution qe2 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query2);
						ResultSet rs2 = qe2.execSelect() ;
						while (rs2.hasNext()){
							QuerySolution sol2 = rs2.nextSolution();
							Resource dbp = sol2.getResource("dbp");
							dbps.add(dbp);
							System.out.println(entity + "   " + dbp);
						}
						qe2.close();
					}
					for (Resource dbp: dbps) {
						insert.insertRedirectTriples(entity, dbp, rdfmodel, storeRedirectFile);
					}
					dbps.clear();
					labels.clear();
				}
			}
		} finally { qe.close() ; 
		} 
	}
}
