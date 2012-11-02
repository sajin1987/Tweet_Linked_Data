package insertingLinks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * This class inserts the triples (localDatasetEntity owl:sameAs DBPedia Entity) to the RDF file
 * 
 * @author Sajin
 */

public class FileLinkInserter {

	public void insertTriples (ArrayList<String> links, Model rdfmodel, String LinkFile) throws IOException {

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		String local = links.get(1);
		String dbp = links.get(0);

		rdfmodel.add(rdfmodel.createStatement(rdfmodel.createResource(local), rdfmodel.createProperty("http://www.w3.org/2002/07/owl#sameAs"), rdfmodel.createResource(dbp)));

		fos = new FileOutputStream(LinkFile);
		osw = new OutputStreamWriter(fos, "UTF-8");
		rdfmodel.write(osw, "RDF/XML-ABBREV");
		fos.close();
		osw.close();
	} 
}
