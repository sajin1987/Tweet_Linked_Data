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
 * 
 * @author Sajin
 *
 */
public class BagofWords {

	public ProbabilityComparer setFeatures (Resource candidateEntity, Resource localEntity, Set<String> valuesDomainEntity){

		Set<String> valuesCandidateEntity = new HashSet<String>();

		Query query3 = QueryFactory.create("PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n\nSELECT DISTINCT ?prop ?value\nWHERE {\n{ <"+candidateEntity+"> ?prop ?value.}\nUNION {\n?redirectEntity dbpedia-owl:wikiPageRedirects <"+candidateEntity+">.\n?redirectEntity rdfs:label ?value.\n}\n}");
		QueryExecution qe3 = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query3);
		ResultSet rs3 = qe3.execSelect();
		while (rs3.hasNext()){
			QuerySolution sol3 = rs3.nextSolution();
			RDFNode value3 = sol3.get("value");
			RDFNode prop3 = sol3.get("prop");

			String propSt3 = null;
			if (prop3==null){
				propSt3 = "na";
			}
			else {
				propSt3 = prop3.toString();
			}

			String datatype3 = null;
			String valueLit3 = value3.toString();

			if (value3.isLiteral()){
				datatype3 = value3.asLiteral().getDatatypeURI();
				valueLit3 = value3.asLiteral().getString();

				if (datatype3==null && !propSt3.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !propSt3.equals("http://dbpedia.org/ontology/abstract") 
						&& !propSt3.equals("http://www.w3.org/2000/01/rdf-schema#comment"))  
				{			
					valuesCandidateEntity.add(valueLit3.toLowerCase());
				}
				else if (datatype3 == null && !propSt3.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && propSt3.equals("http://dbpedia.org/ontology/abstract") 
						&& propSt3.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
					String splitValues3[] = valueLit3.toLowerCase().split("[.?!| ]");  // in this case the literal value is more than one words, therefore the string is
					// split into words.

					for(int i=0;i<splitValues3.length;i++)
					{    
						valuesCandidateEntity.add(splitValues3[i].toLowerCase());
					}
				}
			}
		}
		CosineSimilarity csm = new CosineSimilarity();
		ProbabilityComparer compare = csm.post(valuesDomainEntity, valuesCandidateEntity, localEntity, candidateEntity);
		return compare;
	}
}
