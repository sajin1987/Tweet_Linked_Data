package secondInterlinking;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author Sajin
 *
 */
public class ProbabilityComparer{

	private double probability;
	private ArrayList<Resource> entities;

	public ProbabilityComparer(double probability, ArrayList<Resource> entities) {
		this.probability = probability;
		this.entities = entities;
	}
	
	// getters & setters
	public void setProbability(double probability) {
		this.probability = probability;
	}
	public double getProbability() {
		return probability;
	}

	public void setEntities(ArrayList<Resource> entities) {
		this.entities = entities;
	}
	public ArrayList<Resource> getEntities() {
		return entities;
	}
}
