package secondInterlinking;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import serviceStart.ServiceVariables;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class selects different property values of the domain entity and stores them in a hashset. If the property value is a resource it sends it to
 * a different class which returns a hashset of different property values of the resource sent. 
 * @author Sajin
 *
 */
public class DomainEntityQuerier {

	public static void setDomainEntityValues(ServiceVariables variables) throws IOException{

		String domainEntity = variables.getDomainEntity();

		Set<String> valuesDomainEntity = new HashSet<String>();

		// query to select the distinct label values from dbpedia dataset for domain entity.
		Query query = QueryFactory.create("SELECT ?prop ?value\nWHERE {\n<"+domainEntity+"> ?prop ?value.\n}");
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		try {
			ResultSet rs = qe.execSelect() ;
			while (rs.hasNext()){
				QuerySolution sol = rs.nextSolution();
				RDFNode value = sol.get("value");
				RDFNode prop = sol.get("prop");

				String propSt = null;
				if (prop==null){
					propSt = "na";
				}
				else {
					propSt = prop.toString();
				}

				String datatype = null;
				String valueLit = value.toString();
				if (value.isLiteral()){
					datatype = value.asLiteral().getDatatypeURI();     //literal values that are strings have datatype as null in dbpedia
					valueLit = value.asLiteral().getString();

					if (datatype == null && !propSt.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && !propSt.equals("http://dbpedia.org/ontology/abstract") 
							&& !propSt.equals("http://www.w3.org/2000/01/rdf-schema#comment")){
						valuesDomainEntity.add(valueLit.toLowerCase());
					}
					else if (datatype == null && !propSt.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && propSt.equals("http://dbpedia.org/ontology/abstract") 
							&& propSt.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
						String splitValues[] = valueLit.toLowerCase().split("[.?!| ]");  // in this case the literal value is more than one words, therefore the string is
						// split into words.

						for(int i=0;i<splitValues.length;i++)
						{    
							valuesDomainEntity.add(splitValues[i].toLowerCase());
						}
					}
				}

				else if (value.isResource()){   
					Resource valueRes = value.asResource();
					Set<String> valuesRes = ResourceQuerier.getResourceValues(valueRes);   // returns a hashset of different property values of the valueRes entity
					valuesDomainEntity.addAll(valuesRes);
					//	System.out.println(valuesLocal);
				}
			}
		}
		finally { qe.close() ; }
		Disambiguation.startDisLinking(valuesDomainEntity, variables);
	}

	public static void main (String[] args) throws IOException {

		ServiceVariables variables = new ServiceVariables();
		variables.setDomainEntity("http://dbpedia.org/resource/United_Kingdom_general_election,_2010");
		variables.setPerformRedirect("yes");
		variables.setStoreRedirectFile("C:/masters/finalCode/testingrdfised.rdf");
		variables.setStoreFirstLinkFile("C:/masters/finalCode/firstLink24.rdf");
		variables.setStoreSecondLinkFile("C:/masters/finalCode/secondLink2.rdf");

		DomainEntityQuerier.setDomainEntityValues(variables);
	}

}
