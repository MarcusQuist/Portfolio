package Path;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DistanceType {

    /**
     * Takes a value of distance and determines whether it should add the suffix and format for kilometers
     * or meters.
     * @param kilometers distance
     * @return decimalformat of distance
     */

    public String distance(double kilometers) {
        if (kilometers >= 1.00) {
            String suffix = "km";
            DecimalFormat df = new DecimalFormat("#.00");

            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(kilometers) + suffix;

        } else {
            String suffix = "m";
            DecimalFormat df = new DecimalFormat("#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(kilometers*1000) + suffix;

        }

    }

    /**
     * Take a value of time and determines and converts it to minutes or hours and minutes.
     * @param hours time
     * @return decimalformat of distance
     */

    public String time(double hours) {
        int totalMinutes = (int) (hours * 60);
        int restMinutes = totalMinutes % 60;
        int restHours = totalMinutes / 60;

        String output = "";
        if (restHours > 0){
            output = restHours + " hours and ";
        }

        return output + restMinutes + " minutes";
    }


}
