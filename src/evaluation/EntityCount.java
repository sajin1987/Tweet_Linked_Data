package evaluation;

import java.io.IOException;
import java.util.ArrayList;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class EntityCount {

	public static void findTriples (ArrayList<String> textArray) throws IOException {
		Storing st = new Storing();
		for (int i=0; i<textArray.size(); i=i+2){
			String loc = textArray.get(i);


			String fileName = "";   //location of RDF dump file
			Model rdfmodel = ModelFactory.createDefaultModel();
			rdfmodel.read(fileName);

			// Query to find those tweets which mention the entity directly
			Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX sioc: <http://rdfs.org/sioc/ns#>\nPREFIX sioc-type: <http://rdfs.org/sioc/types#>\nPREFIX dbpo: <http://dbpedia.org/property/>\nPREFIX twit: <http://www.deritweetproject.com/ns#>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n\nSELECT ?label \nWHERE {?tweet rdf:type sioc-type:MicroblogPost.\n?tweet sioc:topic <"+loc+">.\n<"+loc+"> rdfs:label ?label\n}");
			QueryExecution qe = QueryExecutionFactory.create(query, rdfmodel);

			try {
				ResultSet rs = qe.execSelect() ;
				while (rs.hasNext()){
					QuerySolution sol = rs.nextSolution();
					Literal tweet = sol.getLiteral("label");
					System.out.println(tweet);   //prints the distinct tweets
					st.store(tweet.toString());
				}
			} finally { qe.close() ; 
			} 
		}

	}
}
