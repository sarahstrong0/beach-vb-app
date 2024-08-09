import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PointsParser {
    public static void main(String[] args) {
        String filename = "/path/to/UCLA vs USC 3's 5_5_24  - Hudl Tags.xml";
        Match match = parseMatch(filename);
        // Now match contains all points and their actions
    }

    public static Match parseMatch(String filename) {
        Match match = new Match();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(filename));
            doc.getDocumentElement().normalize();

            NodeList instanceList = doc.getElementsByTagName("instance");

            List<Element> instances = new ArrayList<>();
            for (int i = 0; i < instanceList.getLength(); i++) {
                instances.add((Element) instanceList.item(i));
            }

            instances.sort(Comparator.comparing(e -> Double.parseDouble(e.getElementsByTagName("start").item(0).getTextContent())));

            Point currentPoint = null;
            String currentServeTime = null;

            for (Element instance : instances) {
                String start = instance.getElementsByTagName("start").item(0).getTextContent();
                NodeList labels = instance.getElementsByTagName("label");

                String type = null;
                String playerNum = null;
                for (int i = 0; i < labels.getLength(); i++) {
                    Element label = (Element) labels.item(i);
                    String group = label.getElementsByTagName("group").item(0).getTextContent();
                    String text = label.getElementsByTagName("text").item(0).getTextContent();

                    if (group.equals("type")) {
                        type = text;
                    } else if (group.equals("playerUserId")) {
                        playerNum = text;
                    }
                }

                if (type != null && type.equals("serve")) {
                    if (currentPoint != null) {
                        match.addPoint(currentPoint);
                    }
                    currentServeTime = start;
                    currentPoint = new Point(playerNum);
                }

                if (currentPoint != null) {
                    currentPoint.addAction(type);
                }
            }

            if (currentPoint != null) {
                match.addPoint(currentPoint);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return match;
    }
}