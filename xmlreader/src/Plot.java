import edu.princeton.cs.algs4.StdDraw;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.util.Random;

import static edu.princeton.cs.algs4.StdDraw.save;


public class Plot {

    static Random r = new Random();

    static Location[] squares = new Location[3200];

    //tracks amount of hits in that square
    static HashMap<Location, Integer> amounts = new HashMap<>();

    public static int section(Location l) {
        if (Double.valueOf(l.x) < (- 4 + 1.6)) {
            return 1;
        } else if (Double.valueOf(l.x) < (-4 + (2 * 1.6))) {
            return 2;
        } else if (Double.valueOf(l.x) < (-4 + (3 * 1.6))) {
            return 3;
        } else if (Double.valueOf(l.x) < (-4 + (4 * 1.6))) {
            return 4;
        } else {
            return 5;
        }
    }

    public static double xConverter (String x) {
        return (Double.valueOf(x) / 20) + 0.5;
    }

    public static double yConverter(String y) {
        return (Double.valueOf(y) / 20) + 0.5;
    }

    public static void plotAttack(Location start, Location end, String type) {
        StdDraw.setPenRadius(0.002);
        if (type.equals("kill")) {
            StdDraw.setPenColor(StdDraw.GREEN.darker());
        } else if (type.equals("error")) {
            StdDraw.setPenColor(StdDraw.RED);
        } else if (type.equals("None")){
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        double x0 = xConverter(start.x);
        double x1 = xConverter(end.x);
        double y0 = yConverter(start.y);
        double y1 = yConverter(end.y);
        StdDraw.line(x0, y0, x1, y1);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.point(x1, y1);
        StdDraw.filledSquare(x0, y0, 0.01);
    }

    public static void plotCourt() {
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        // Draw Court
        StdDraw.rectangle(0.5, 0.5,0.2, 0.4);
        StdDraw.line(0.3, 0.5, 0.7, 0.5);
        StdDraw.line(0.3, 0.65, 0.7, 0.65);
        StdDraw.line(0.3, 0.35, 0.7, 0.35);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.point(0.3, 0.1);
    }

    public static boolean containsLocation(Location square, Location l) {
        //Check if square contains hit location
        if ((Double.valueOf(l.x) <= Double.valueOf(square.x) + 0.005) && (Double.valueOf(l.x) >= Double.valueOf(square.x) - 0.005)) {
            if ((Double.valueOf(l.y) <= Double.valueOf(square.y) + 0.005) && (Double.valueOf(l.y) >= Double.valueOf(square.y) - 0.005)) {
                return true;
            }
        }
        return false;
    }

    public static int square(Location l){
        for (int i = 0; i < squares.length; i ++) {
            if (containsLocation(squares[i], l)) {
                return i;
            }
        }
        return -1;
    }

    public static void fillHeatMap(Player p) {
        int x = 0;
        for (double i = 0.105; i < 0.905; i = i + 0.01) {
            for (double j = 0.305; j < 0.705; j = j + 0.01) {
                Location l = new Location(String.valueOf(j), String.valueOf(i));
                squares[x] = l;
                x++;
            }
        }
        //Initiate squares
        for (Location square: squares) {
            amounts.put(square, 0);
        }
        // Count Attacks
        for (Location l: p.attacks.keySet()) {
            ArrayList<Result> ends = p.attacks.get(l);
            for (Result r: ends) {
                if (Double.valueOf(l.y) > Double.valueOf(r.endLocation.y)) {
                    Location y = l;
                    l = r.endLocation;
                    r.endLocation = y;
                }
                if (square(l) != -1) {
                    int temp = amounts.remove(squares[square(l)]);
                    amounts.put(squares[square(l)], temp + 1);
                }
            }
        }
    }

    public static Color color(int i) {
        Color c = StdDraw.MAGENTA;
        for (int x = 0; x < i; x++) {
            c = c.darker();
        }
        return c;
    }

    public static void plotHeatMap() {
        int y = 0;
        for (Location l: squares) {
            if (y > 1600) { //Closest side is under 1600 (other side if 1600+)
                StdDraw.setPenColor(color(amounts.get(l)));
                StdDraw.filledSquare(Double.valueOf(l.x), Double.valueOf(l.y), 0.005);
            }
            y++;
        }
    }

    public static void plotPlayerAttacks(Player p) {
        plotCourt();
        StdDraw.text(0.5, 0.95, p.playerCode);

        // Draw Attacks
        for (Location l: p.attacks.keySet()) {
            ArrayList<Result> ends = p.attacks.get(l);
            for (Result r: ends) {
                if (Double.valueOf(l.y) > Double.valueOf(r.endLocation.y)) {
                    Location x = l;
                    l = r.endLocation;
                    r.endLocation = x;
                }
                plotAttack(l, r.endLocation, r.type);
            }
        }
    }

    public static void fillSection(int section) {
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.filledRectangle(0.3 - 0.04 + (0.08 * (double) section), 0.3, 0.04, 0.2);
    }

    public static void plotPlayerAttacksBySection(Player p, int section) {
        plotCourt();
        StdDraw.text(0.5, 0.95, p.playerCode + ": Section " + section);
        fillSection(section);
        // Draw Attacks
        for (Location l: p.attacks.keySet()) {
            // Collect all attacks (location and type)
            ArrayList<Result> ends = p.attacks.get(l);
            for (Result r : ends) {
                // flip to all be on same side of court
                if (Double.valueOf(l.y) > Double.valueOf(r.endLocation.y)) {
                    Location x = l;
                    l = r.endLocation;
                    r.endLocation = x;
                }
                if (section(l) == section) {
                    plotAttack(l, r.endLocation, r.type);
                }

            }
        }
    }

    public static void plotPlayerFBSAttacksBySection(Player p, int section) {
        plotCourt();
        StdDraw.text(0.5, 0.95, p.playerCode + ": Section " + section);
        fillSection(section);
        // Draw Attacks
        for (Location l: p.attacks.keySet()) {
            // Collect all attacks (location and type)
            ArrayList<Result> ends = p.attacks.get(l);
            for (Result r : ends) {
                // flip to all be on same side of court
                if (Double.valueOf(l.y) > Double.valueOf(r.endLocation.y)) {
                    Location x = l;
                    l = r.endLocation;
                    r.endLocation = x;
                }
                if (section(l) == section && r.fbs) {
                    plotAttack(l, r.endLocation, r.type);
                }

            }
        }
    }

    public static void heatMapTester() {
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);

        // Draw Court
        StdDraw.rectangle(0.5, 0.5,0.2, 0.4);

        Location[] squares = new Location[3200];
        int x = 0;
        for (double i = 0.105; i < 0.905; i = i + 0.01) {
            for (double j = 0.305; j < 0.705; j = j + 0.01) {
                Location l = new Location(String.valueOf(j), String.valueOf(i));
                squares[x] = l;
                x++;
            }
        }

        int y = 0;
        for (Location l: squares) {
            StdDraw.setPenColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            if (y < 1600) { //Closest side is under 1600 (other side if 1600+)
                StdDraw.setPenColor(StdDraw.MAGENTA);
            }
            StdDraw.filledSquare(Double.valueOf(l.x), Double.valueOf(l.y), 0.005);
            y++;
        }

        StdDraw.setPenColor(StdDraw.BLACK);

        // Draw Court
        StdDraw.rectangle(0.5, 0.5,0.2, 0.4);
    }


    public static void main(String[] args) {
//        StdDraw.setPenColor(StdDraw.PINK);
//        StdDraw.point(0.9, 0.9);
//        StdDraw.setPenColor(StdDraw.MAGENTA);
//        plotAttack(new Location("-2", "-7"), new Location("4", "8"), "kill");
//        plotAttack(new Location("-3", "-3"), new Location("4", "8"), "kill");
//        System.out.println(p);

//        plotPlayerAttacks(Parser.getPlayers().get("13277940"));
        heatMapTester();


    }


}
