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

public class Set extends Action { 

    public Set(int playerID) { 
        super(playerID, "set");  // type = "pass", playerID set
    
    }
    
    @Override 
    public String toString() { 
        return "Type:  Set --- playerID " + this.playerID; 
    }


}