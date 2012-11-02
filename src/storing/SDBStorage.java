package storing;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.query.Dataset;

/**
 * @author Sajin
 * This class takes the model as input and stores it as a graph in a SDB store backed by MySQL database
 */
public class SDBStorage {

	public void storeToSDB(ArrayList<Model> arrModel)
	{
		HashMap<String, String> prefixes = new HashMap<String, String>();
		prefixes.put("siocType", "http://rdfs.org/sioc/types#");
		prefixes.put("sioc", "http://rdfs.org/sioc/ns#");
		prefixes.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		prefixes.put("ctag", "http://commontag.org/ns#");
		prefixes.put("foaf", "http://xmlns.com/foaf/0.1/");
		prefixes.put("twit", "http://www.deritweetproject.com/ns#");
		
		StoreDesc storeDesc = StoreDesc.read("sdb.ttl") ;	
		Store store = SDBFactory.connectStore(storeDesc) ;
		Dataset ds = SDBFactory.connectDataset(store) ;
		Model mNew = ds.getDefaultModel();
		
		for (Model m: arrModel) {
			m.setNsPrefixes(prefixes);
			RDFWriter w = m.getWriter("RDF/XML-ABBREV"); 
			w.setProperty("showXmlDeclaration","true");
			mNew.add(m);
			System.out.println("done");
		}
		store.close() ;
	}
}
