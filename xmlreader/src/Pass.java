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

public class Pass extends Action { 

    public boolean isServeRecieve;
    public Location serveLocation;
    public int quality;
    public String type;

    public Pass(int playerID, boolean isServeRecieve, Location serveLocation, int quality) { 
        super(playerID); 
        this.type = "pass";  // type = "pass", playerID set
        this.isServeRecieve = isServeRecieve;
        this.serveLocation = serveLocation; 
        this.quality = quality;
    }

    public String getIsServeRecieve() {
        return this.isServeRecieve;
    }

    public Location getServeLocation() { 
        return this.serveLocation;
    }

    public int quality() { 
        return this.quality;
    }
    
    @Override 
    public String toString() { 
        return "Type: Pass --- serveRecieve?: " + String.valueOf(this.isServeRecieve) + " --- StartLocation: " 
        + this.startLocation.toString() + " --- Quality: " + String.valueOf(quality) + " --- playerID: " + this.playerID;
    }


}