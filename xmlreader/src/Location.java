public class Location {
    String x;
    String y;
    public Location(String x, String y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

}
