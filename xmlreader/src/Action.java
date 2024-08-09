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

public class Action { 

    public int playerID; 

    public Action(int playerID) { 
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return String.valueOf(this.playerID);
    }

    public String toString() { 
        return "PlayerID : " + this.playerID;
    }

}