package insertingLinks;

import java.util.ArrayList;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

/**
 * This class updates the local SDB store with the triples (localDatasetEntity owl:sameAs DBPedia Entity)
 * using SPARQL Update. 
 * 
 * @author Sajin
 */

public class SDBLinkUpdater {

	public void updateTriples(ArrayList<String> links){

		String local = links.get(1);
		String dbp = links.get(0);

		StoreDesc storeDesc = StoreDesc.read("sdb.ttl") ;
		Store store = SDBFactory.connectStore(storeDesc) ;
		Dataset ds = SDBFactory.connectDataset(store) ;
		UpdateRequest request = UpdateFactory.create() ;
		System.out.println("<"+local+">" + "http://www.w3.org/2002/07/owl#sameAs" + "" + " " + "<"+dbp+">");
		request.add("INSERT {<"+local+"> <http://www.w3.org/2002/07/owl#sameAs> <"+dbp+"> } WHERE {?s ?p ?o}");
		UpdateAction.execute(request,ds);
		store.close();
	}
}
