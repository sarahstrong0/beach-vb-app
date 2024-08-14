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

public class Serve extends Action {
    public String result;
    public String quality;
    public String type;

    public Serve(String playerID, String result, String quality) { 
        super(playerID); 
        this.type = "serve";  // type = "serve", playerID set
        this.result = result;
        this.quality = quality;
    }

    public int quality() { 
        return Integer.valueOf(this.quality);
    }
    
    @Override 
    public String toString() { 
        return "Type: Serve --- result: " + this.result + " --- Quality: " + this.quality + " --- playerID: " + this.playerID;
    }


}
    

