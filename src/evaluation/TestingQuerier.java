package evaluation;

import java.io.IOException;

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

public class TestingQuerier {

	public static void main (String[] args) throws IOException {

		String fileName = "";   //location of RDF dump file
		Model rdfmodel = ModelFactory.createDefaultModel();
		rdfmodel.read(fileName);
		
		Storing st = new Storing();

		// Query to find those tweets which mention the entity directly
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX sioc: <http://rdfs.org/sioc/ns#>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX owl: <http://www.w3.org/2002/07/owl#>\n\nSELECT DISTINCT ?entity ?type ?label ?alreadyLinked\nWHERE {\n?tweet sioc:topic ?entity.\n?entity rdf:type ?type.\n?entity rdfs:label ?label.\nOPTIONAL {?entity owl:sameAs ?alreadyLinked}\n}");
		QueryExecution qe = QueryExecutionFactory.create(query, rdfmodel);

		try {
			ResultSet rs = qe.execSelect() ;
			while (rs.hasNext()){
				QuerySolution sol = rs.nextSolution();
				Resource alreadyLinked = sol.getResource("alreadyLinked");
				Resource entity = sol.getResource("entity");
				if (alreadyLinked==null){
				System.out.println(entity + "     " + alreadyLinked);
				st.store(entity + "     " + alreadyLinked);
				}
			//	Literal tweet = sol.getLiteral("label");
			}
		}
		finally { qe.close() ; 
		} 
	}
}
