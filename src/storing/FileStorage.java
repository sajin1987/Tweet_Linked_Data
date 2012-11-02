package storing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * @author Sajin
 * This class takes the model as input and stores it as a graph in a rdf file
 */
public class FileStorage {

	public void storeRDF(ArrayList<Model> arrModel, String storeFile) throws IOException {

		HashMap<String, String> prefixes = new HashMap<String, String>();
		prefixes.put("siocType", "http://rdfs.org/sioc/types#");
		prefixes.put("sioc", "http://rdfs.org/sioc/ns#");
		prefixes.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		prefixes.put("ctag", "http://commontag.org/ns#");
		prefixes.put("foaf", "http://xmlns.com/foaf/0.1/");
		prefixes.put("twit", "http://www.deritweetproject.com/ns#");
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		for (Model m: arrModel) {

			m.setNsPrefixes(prefixes);
			fos = new FileOutputStream(storeFile);
			osw = new OutputStreamWriter(fos, "UTF-8");
			RDFWriter w = m.getWriter("RDF/XML-ABBREV");   
			w.setProperty("showXmlDeclaration","true");
			w.write(m, osw, "RDF/XML-ABBREV");    //writes the model to the file in RDF/XML-ABBREV format
			System.out.println(m);
			fos.close();
			osw.close();
		}

	}  
}

