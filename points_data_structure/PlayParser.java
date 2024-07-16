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

public class PlayParser { 

    ArrayList<Action> plays = new ArrayList<>();  // PLAYS

    static HashMap<String, Player> players = new HashMap<>(); // PLAYER
    static HashMap<String, String> codeToPlayerId = new HashMap<>(); // Code to PlayerID

    public static HashMap<String, Player> getPlayers() {
        return players;
    }

    public static String arrayPrinter(ArrayList<Result> arr) {
        StringBuilder ret = new StringBuilder();
        for (Result r: arr) {
            ret.append(r.toString() + "\n");
        }
        return ret.toString();
    }

    public static void parse(String FILENAME) { 
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

             // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            // get <instance>
            NodeList list = doc.getElementsByTagName("instance");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    





                }





            }








        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }






    }


}