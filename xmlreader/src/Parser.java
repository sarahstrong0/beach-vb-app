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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Parser {
//    private static final String FILENAME = "/Users/maliakowal/Downloads/lexymaggievsUSC.xml";
    static HashMap<String, Player> players = new HashMap<>();
    static HashMap<String, String> codeToPlayerId = new HashMap<>();

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

    public static HashMap<String, Player> fillPlayers(String FILENAME) {
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

            ArrayList<String> prevTypes = new ArrayList<>();

            Boolean serveRecieve = false;



            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    String indent = "     ";

                    String start = element.getElementsByTagName("start").item(0).getTextContent();
                    String end = element.getElementsByTagName("end").item(0).getTextContent();
                    String code = element.getElementsByTagName("code").item(0).getTextContent();


                    System.out.println("Current Element : " + (temp + 1));
                    System.out.println("Start time: " + start);
                    System.out.println("End time: " + end);
                    System.out.println("Code: " + code);
//                    if (!codeToPlayerId.containsKey(code)) {
//                        codeToPlayerId.put(code, null);
//                    }

                    // Label Nodes
                    NodeList labelList = element.getElementsByTagName("label");
                    Player currPlayer = null;
                    String currPlayerID = null;
                    String currType = null;
                    String currRes = null;
                    String currStartX = null;
                    String currStartY = null;
                    String currEndX = null;
                    String currEndY = null;
                    String currSubType = null;

                    for (int i = 0; i < labelList.getLength(); i++) {
                        Node label = labelList.item(i);
                        if (label.getNodeType() == Node.ELEMENT_NODE) {
                            Element l = (Element) label;
                            String group = l.getElementsByTagName("group").item(0).getTextContent();
                            String text = l.getElementsByTagName("text").item(0).getTextContent();
//                            System.out.println(indent + group + ": " + text);

                            switch (group) {
                                case "type":
                                    currType = text;
                                    System.out.println(indent + group + ": " + text);
                                    prevTypes.add(currType);
//                                    System.out.println(prevTypes);
                                    break;
                                case "subType":
                                    currSubType = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "result":
                                    currRes = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "playerUserId":
                                    currPlayerID = text;
                                    if (!players.containsKey(currPlayerID)) {
                                        currPlayer = new Player(currPlayerID, code);
                                        players.put(currPlayerID, currPlayer);
                                        codeToPlayerId.put(code, currPlayerID);
                                    } else {
                                        currPlayer = players.get(currPlayerID);
                                    }
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "attackLocationStartX":
                                    currStartX = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "attackLocationStartY":
                                    currStartY = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "attackLocationEndX":
                                    currEndX = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                case "attackLocationEndY":
                                    currEndY = text;
                                    System.out.println(indent + group + ": " + text);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (currRes == null) {
                        currRes = "None";
                    }
                    Location startLoc = new Location(currStartX, currStartY);
                    Location endLoc = new Location(currEndX, currEndY);

                    if (currType.equals("attack")) {
                        if ((prevTypes.get(prevTypes.size() - 2)).equals("set") && (prevTypes.get(prevTypes.size() - 3)).equals("pass") && (prevTypes.get(prevTypes.size() - 4)).equals("serve")) {
                            currPlayer.addAttack(startLoc, endLoc, currRes, true);
                        }
                        currPlayer.addAttack(startLoc, endLoc, currRes, false);
                    }

                }
            }
            for (Player p: players.values()) {
                System.out.println("Player Name: " + p.playerCode);
//                System.out.println(p.attacks.keySet());
                for (Location loc: p.attacks.keySet()) {
                    System.out.println("     " + "Start: " + loc + " : " + arrayPrinter(p.attacks.get(loc)));
                }
            }

        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return players;
    }


}
