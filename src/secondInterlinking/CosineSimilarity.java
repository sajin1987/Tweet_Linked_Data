package secondInterlinking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author Sajin
 *
 */
public class CosineSimilarity {

	// calculating word similarity between two objects.

	//	@SuppressWarnings("unchecked")

	private HashMap<String, Integer> Sem = new HashMap<String, Integer>(); 

	ArrayList<Resource> entities = new ArrayList<Resource>();

	//	@SuppressWarnings("unchecked")

	public ProbabilityComparer post(Set<String> s, Set<String> t, Resource localEntity, Resource candidateEntity)
	{
		if(t.equals(""))
		{}
		int count=0;
		for(String t1: t)
		{    
			Sem.put(t1,1);
		}
		for(String s1: s)
		{
			if(Sem.containsKey(s1))
			{
				count=count+1;
			}
		}
		double d=Math.pow((double)s.size(),0.5)*Math.pow((double)t.size(),0.5);
		double p=((double)count/d);

		t.clear();
		entities.add(localEntity);
		entities.add(candidateEntity);
		ProbabilityComparer compare = new ProbabilityComparer(p, entities);
		return compare;
	}
}


