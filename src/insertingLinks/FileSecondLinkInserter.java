package insertingLinks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This class inserts the triples (localDatasetEntity owl:sameAs DBPedia Entity) to the RDF file
 * 
 * @author Sajin
 */

public class FileSecondLinkInserter {

	public void insertTriples (ArrayList<Resource> links, Model rdfmodel, String LinkFile) throws IOException {

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		Resource local = links.get(0);
		Resource dbp = links.get(1);

		rdfmodel.add(rdfmodel.createStatement(local, rdfmodel.createProperty("http://www.w3.org/2002/07/owl#sameAs"), dbp));

		fos = new FileOutputStream(LinkFile);
		osw = new OutputStreamWriter(fos, "UTF-8");
		rdfmodel.write(osw, "RDF/XML-ABBREV");
		fos.close();
		osw.close();
	} 
}
