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

public class Attack extends Action { 

    public String result;
    public Location startLocation;
    public Location endLocation;
    public String type;

    public Attack(int playerID, String result, Location startLocation, Location endLocation) { 
        super(playerID);
        this.type = "attack";  // type = "attack", playerID set
        this.result = result;
        this.startLocation = startLocation; 
        this.endLocation = endLocation; 
    }

    public String getResult() {
        return this.result;
    }

    public Location getStartLocation() { 
        return this.startLocation;
    }

    public Location getEndLocation() { 
        return this.endLocation;
    }
    
    @Override 
    public String toString() { 
        return "Type: Pass --- Result: " + this.result + " --- StartLocation: " 
        + this.startLocation.toString() + " --- EndLocation: " + this.endLocation + " --- playerID: " + this.playerID;
    }


}