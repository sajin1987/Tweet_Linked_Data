package storing;

import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;

/**
 * This class is used to create the necessary tables and indexes required for a full SDB store
 * 
 * @author Sajin
 */
public class TableCreator {
	public static void main(String[] args)
	{
		StoreDesc storeDesc = StoreDesc.read("sdb.ttl") ;	
		Store store = SDBFactory.connectStore(storeDesc) ;
		store.getTableFormatter().create();
		store.close() ;
	}
}
