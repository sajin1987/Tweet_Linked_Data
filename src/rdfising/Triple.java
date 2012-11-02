package rdfising;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * contains the subject object and predicate of a triple
 * * @author Sajin
 */

public class Triple {
	private String subject;
	private String object;
	private String predicate;
	private Resource subjectJena;
	private Property predicateJena;
	private RDFNode objectJena;


	public Triple(String s, String p, String o) {
		this.subject = s;
		this.predicate = p;
		this.object = o;

	}

	public void setTripleJena(Resource s, Property p, RDFNode o){
		this.subjectJena = s;
		this.predicateJena = p;
		this.objectJena = o;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getObject() {
		return object;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getPredicate() {
		return predicate;
	}

	public void setSubjectJena(Resource subject) {
		this.subjectJena = subject;
	}
	public Resource getSubjectJena() {
		return subjectJena;
	}
	public void setObjectJena(RDFNode object) {
		this.objectJena = object;
	}
	public RDFNode getObjectJena() {
		return objectJena;
	}
	public void setPredicateJena(Property predicate) {
		this.predicateJena = predicate;
	}
	public Property getPredicateJena() {
		return predicateJena;
	}


	public String toString(){
		return subject + " " + predicate + " " + object + " .\n";
	}
}
