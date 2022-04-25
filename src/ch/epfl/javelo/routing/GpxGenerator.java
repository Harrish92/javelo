package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;


/**
 *
 * GpxGenerator représente un générateur d'itinéraire au format GPX
 *
 * @author Harrishan Raveendran (345291)
 *
 */
public class GpxGenerator {

    /**
     *
     * @param itineraire itinéraire
     * @param profile profile de l'itinéraire
     * @return un document GPX
     */
    public static Document createGpx(Route itineraire, ElevationProfile profile){
        Document doc = newDocument(); // voir plus bas

        Element root = doc
                .createElementNS("http://www.topografix.com/GPX/1/1",
                        "gpx");
        doc.appendChild(root);

        root.setAttributeNS(
                "http://www.w3.org/2001/XMLSchema-instance",
                "xsi:schemaLocation",
                "http://www.topografix.com/GPX/1/1 "
                        + "http://www.topografix.com/GPX/1/1/gpx.xsd");
        root.setAttribute("version", "1.1");
        root.setAttribute("creator", "JaVelo");

        Element metadata = doc.createElement("metadata");
        root.appendChild(metadata);

        Element name = doc.createElement("name");
        metadata.appendChild(name);
        name.setTextContent("Route JaVelo");

        Element rte = doc.createElement("rte");
        root.appendChild(rte);

        double length = 0;
        List<PointCh> list = itineraire.points();
        for(int i=0; i < list.size(); i++){
            Element rtept = doc.createElement("rtept");
            rte.appendChild(rtept);
            rtept.setAttribute("lon", String.format(Locale.ROOT,"%.5f", Math.toDegrees(list.get(i).lon())));
            rtept.setAttribute("lat", String.format(Locale.ROOT, "%.5f", Math.toDegrees(list.get(i).lat())));

            Element ele = doc.createElement("ele");
            rtept.appendChild(ele);
            ele.setTextContent(String.format(Locale.ROOT, "%.2f", profile.elevationAt(length)));
            if(itineraire.edges().size() > i)
                length += itineraire.edges().get(i).length();

        }

        return doc;
    }

    /**
     *
     * @return un nouveau document
     */
    private static Document newDocument() {
        try {
            return DocumentBuilderFactory
                    .newDefaultInstance()
                    .newDocumentBuilder()
                    .newDocument();
        } catch (ParserConfigurationException e) {
            throw new Error(e); // Should never happen
        }
    }


    /**
     *
     * @param file_name nom de fichier
     * @param itineraire itinéraire
     * @param profile profile de l'itinéraire
     * @throws IOException exception lancée s'il y'a une erreur d'entrée/sortie
     *
     * méthode écrit le document GPX correspondant dans le fichier
     */
    public static void writeGpx(String file_name, Route itineraire, ElevationProfile profile) throws
            IOException {

        Document doc = createGpx(itineraire, profile);
        Writer w = Files.newBufferedWriter(Path.of(file_name));

        try {
            Transformer transformer = TransformerFactory
                    .newDefaultInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc),
                    new StreamResult(w));
        } catch(TransformerException e){
            throw new Error(e);
        }

    }


}
