import java.util.HashMap;
import java.util.ArrayList;

public class Player {
    public String playerID;
    public String playerCode;

    HashMap<Location, ArrayList<Result>> attacks;
    public Player(String playerID, String playerCode) {
        this.playerID = playerID;
        this.playerCode = playerCode;
        this.attacks = new HashMap<>();
    }

    public void addAttack(Location start, Location end, String result, Boolean fbs) {
        if (this.attacks.containsKey(start)) {
            this.attacks.get(start).add(new Result(end, result, fbs));
        } else {
            this.attacks.put(start, new ArrayList<>());
            this.attacks.get(start).add(new Result(end, result, fbs));
        }
    }


}
