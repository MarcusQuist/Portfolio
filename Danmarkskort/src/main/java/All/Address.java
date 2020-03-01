package All;

import OSM.Node;

import java.io.Serializable;
import java.sql.SQLOutput;

/**
 *  Represents an address on the map
 */

public class Address implements Serializable {
    private final String street, house, floor, side, postcode, city;
    private final OSM.Node node;

    /**
     * Address object with associated node
     * @param _node Node for address
     * @param _street Street name
     * @param _house House number
     * @param _floor Floor number
     * @param _side side left/right
     * @param _postcode City/area postcode
     * @param _city City name
     */
    public Address(String _street, String _house, String _floor, String _side, String _postcode, String _city, OSM.Node _node) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
        node = _node;
    }


    public Node getNode() {
        return node;
    }


    /**
     * Creates string from address information
     * @return Address string representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(street.substring(0, 1).toUpperCase());
        sb.append(street.substring((1)));
        sb.append(" ");
        sb.append(house);
        appendToString(sb, floor, side);
        appendToString(sb, postcode, city);
        return sb.toString();
    }

    /**
     * Appends strings to StringBuilder
     * @param sb StringBuilder to append to
     * @param arg1 First String to append
     * @param arg2 Second String to append
     */
    private void appendToString(StringBuilder sb, String arg1, String arg2){
        if (arg1 != null && arg2 != null){
            sb.append(", ");
            sb.append(arg1);
            sb.append(" ");
            sb.append(arg2);
        }
    }

    /**
     * Creates string from address information
     * @return Address string without housenumber
     */
    public String toStringNoHouse() {
        StringBuilder sb = new StringBuilder();
        sb.append(street.substring(0, 1).toUpperCase());
        sb.append(street.substring((1)));
        appendToString(sb, floor, side);
        appendToString(sb, postcode, city);
        return sb.toString();
    }

}