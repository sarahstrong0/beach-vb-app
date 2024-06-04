public class Result {
    Location endLocation;
    String type;
    Boolean fbs;

    public Result(Location endLocation, String type, Boolean fbs){
        this.endLocation = endLocation;
        this.type = type;
        this.fbs = fbs;
    }


    @Override
    public String toString() {
        return "End Location: " + this.endLocation + ", Result: " + this.type + ", FBS: " + this.fbs;
    }
}
