import java.util.HashMap;

public class Main {
    public static HashMap<String, Player> p;

    public static void main(String[] args) {
        // PlayerIDs for lexyMaggie: {15589734, 14263176, 6721869, 15766560, 13277940}
        // PlayerIDs for kenzieJaden: test: {18678926, 15589725, 14294594, 18661529, 17880295}
        // Parser.fillPlayers("/Users/maliakowal/Documents/BeachVBHittingCharts/Kenzie_JadenUSC.xml");
        // /Users/maliakowal/Documents/BeachVBHittingCharts
//        p = Parser.fillPlayers("/Users/maliakowal/Downloads/Kenzie_JadenUSC.xml");
        // PlayerIDs: {15589734, 14263176, 15546919, 14263177, 10532553, 15766560, 15589725, 14294594, 13277940, 17880295, 18678926, 13690992, 6721869, 18436971, 16075145, 13670020, 13670014, 18661529}
        p = Parser.fillPlayers("/Users/maliakowal/Documents/BeachVBHittingCharts/lexymaggievsUSC.xml");
        Parser.fillPlayers("/Users/maliakowal/Documents/BeachVBHittingCharts/Kenzie_JadenUSC.xml");
        Parser.fillPlayers("/Users/maliakowal/Documents/BeachVBHittingCharts/FAULSU4s-5_5_23XML.xml");
//        System.out.println("PlayerIDs: " + p);
        // Maggie Boyd: 13277940
//        Plot.plotPlayerAttacksBySection(p.get("10532553"), 5);
        Plot.plotPlayerAttacksBySection(p.get("13277940"), 5);
    }
}