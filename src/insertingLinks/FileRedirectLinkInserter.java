package insertingLinks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author Sajin
 *
 */
public class FileRedirectLinkInserter {

	public void insertRedirectTriples (Resource local, Resource dbp, Model rdfmodel, String LinkFile) throws IOException {

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		rdfmodel.add(rdfmodel.createStatement(local, rdfmodel.createProperty("http://www.w3.org/2002/07/owl#sameAs"), dbp));

		fos = new FileOutputStream(LinkFile);
		osw = new OutputStreamWriter(fos, "UTF-8");
		rdfmodel.write(osw, "RDF/XML-ABBREV");
		fos.close();
		osw.close();
	} 
}
