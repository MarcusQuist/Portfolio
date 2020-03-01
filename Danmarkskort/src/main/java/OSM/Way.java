package OSM;

import java.util.ArrayList;
import java.util.function.LongSupplier;

/**
 * Class Way extending Arraylist<Node>
 */

public class Way extends ArrayList<Node> implements LongSupplier {
	long id;
	boolean oneWay;
	int maxSpeed;

	private boolean vehicleAllowed = false;
	private boolean bicycleAllowed = false;
	private boolean walkingAllowed = false;

	private String street;


	public Way(long id) {
		this.id = id;
		oneWay = false;
		street = "no street";

	}

	@Override
	public long getAsLong() {
		return id;
	}

	public long getId() {
		return id;
	}

	public Node getFirst() {
		return get(0);
	}

	public Node getLast() {
		return get(size()-1);
	}

	public void isOneWay()
	{
		oneWay = true;
	}

	public boolean getOneWay()
	{
		return oneWay;
	}

	public void setMaxSpeed(int maxSpeed)
	{
		this.maxSpeed = maxSpeed;
	}
	public int getMaxSpeed()
	{
		return maxSpeed;
	}

	public void setVehicleAllowed()
	{
		vehicleAllowed = true;
	}
	public boolean getVehicleAllowed()
	{
		return vehicleAllowed;
	}

	public void setBicycleAllowed()
	{
		bicycleAllowed = true;
	}
	public boolean getBicycleAllowed()
	{
		return bicycleAllowed;
	}

	public void setWalkingAllowed()
	{
		walkingAllowed = true;
	}
	public boolean getWalkingAllowed()
	{
		return walkingAllowed;
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
