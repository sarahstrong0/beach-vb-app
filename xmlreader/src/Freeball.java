import java.util.HashMap;
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

public class Freeball extends Action { 
    public String result = null;
    public String type;

    public Freeball(int playerID, String result) { 
        super(playerID); 
        this.type = "freeball";  // type = "block", playerID set
        this.result = result;
    }
    
    @Override 
    public String toString() { 
        if (this.result != null) { 
            return "Type:  Dig --- Result: " + this.result + "playerID: " + this.playerID;
        } else  {
            return "Type:  Dig --- playerID: " + this.playerID; 
        }
    }

}

