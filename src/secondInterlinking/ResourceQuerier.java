package secondInterlinking;

import java.util.HashSet;
import java.util.Set;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class takes those property values of the domain entity that are resources, as input and returns the property values of these resources (only literal values).
 * @author Sajin
 *
 */
public class ResourceQuerier {

	public static Set<String> getResourceValues(Resource dbp){
		Set<String> valuesDomainEntity1 = new HashSet<String>();

		Query query1 = QueryFactory.create("SELECT ?prop ?value\nWHERE {\n<"+dbp+"> ?prop ?value.\n}");
		QueryExecution qe1 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query1);

		try {
			ResultSet rs1 = qe1.execSelect() ;
			while (rs1.hasNext()){
				QuerySolution sol1 = rs1.nextSolution();
				RDFNode value1 = sol1.get("value");
				RDFNode prop1 = sol1.get("prop");

				String propSt1 = null;
				if (prop1==null){
					propSt1 = "na";
				}
				else {
					propSt1 = prop1.toString();
				}

				String datatype1 = null;
				String valueLit1 = value1.toString();
				if (value1.isLiteral()){
					datatype1 = value1.asLiteral().getDatatypeURI();
					valueLit1 = value1.asLiteral().getString();

					if (datatype1 == null && !propSt1.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !propSt1.equals("http://dbpedia.org/ontology/abstract") 
							&& !propSt1.equals("http://www.w3.org/2000/01/rdf-schema#comment")){   // datatype null is chosen because in dbpedia all the property values that
						// are string dont have any datatype mentioned. 
						valuesDomainEntity1.add(valueLit1.toLowerCase());
					}
					else if (datatype1 == null && !propSt1.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && propSt1.equals("http://dbpedia.org/ontology/abstract") 
							&& propSt1.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
						String splitValues1[] = valueLit1.toLowerCase().split("[.?!| ]");

						for(int i=0;i<splitValues1.length;i++)
						{    
							valuesDomainEntity1.add(splitValues1[i].toLowerCase());
						}
					}
				}

			}
		}
		finally { qe1.close() ; }
		return valuesDomainEntity1;

	}
}
