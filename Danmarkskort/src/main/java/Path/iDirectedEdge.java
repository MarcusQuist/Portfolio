package Path;
/**
 * Interface of DirectedEdge
 */

public interface iDirectedEdge {

    public int from();
    public int to();
    public double weight();
    public int getMaxSpeed();
    public boolean getVehicleAllowed();
    public boolean getBicycleAllowed();
    public boolean getWalkingAllowed();
    public boolean oneWay();
    public String toString();
    public int getV();
    public int getW();
    public String getStreet();

}
