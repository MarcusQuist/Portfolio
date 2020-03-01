package Path;
import OSM.iModel;

/**
 *  Calculates distance between 2 points using latitude og longitude and the Haversine formula.
 */
/*
The Haversine class has been taken from the following source: https://github.com/jasonwinn/haversine created by Jason Winn, July 10th, 2013
 */
public class Haversine {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    /**
     * @param startLat first point's value (y)
     * @param startLong first point's value (x)
     * @param endLat last point's value (y)
     * @param endLong last point's value (x)
     * @param lonFactor parsing model to get lonfactor.
     * @return distance
     */

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong, float lonFactor ) {


        double newStartLong = startLong/lonFactor;
        double newEndLong = endLong/lonFactor;

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((newEndLong - newStartLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2* (Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        double distance = EARTH_RADIUS * c;

        return distance;


    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }


}
