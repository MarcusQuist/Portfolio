package OSM;

import java.io.Serializable;
import java.util.function.LongSupplier;

/**

 * Nodes used by the parser to create ways. Has an id and a coordinate set.
 */
public class Node implements LongSupplier, Serializable {
	private float lat, lon;
	private long id;
	private String street;

	/**
	 *
	 * @param id id of the node
	 * @param lon the longitude
	 * @param lat the latitude
	 */
	public Node(long id, float lon, float lat) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}


	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public long getAsLong() {
		return id;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getStreet()
	{
		return street;
	}

}
