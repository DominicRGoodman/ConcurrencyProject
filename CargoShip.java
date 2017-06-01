import java.util.Scanner;

/**
 * CargoShip.java
 * 
 * @author Dominic Goodman 3/26/2017
 * 
 *         Purpose: to contain all the data of a CargoShip object
 */
public class CargoShip extends Ship
{

	double cargeValue, cargoVolume, cargeWeight;

	public CargoShip(Scanner sc)
	{
		super(sc);
		if (sc.hasNextDouble())
			cargeWeight = sc.nextDouble();
		if (sc.hasNextDouble())
			cargoVolume = sc.nextDouble();
		if (sc.hasNextDouble())
			cargeValue = sc.nextDouble();

	}

	public String toString()
	{
		return "Cargo " + super.toString();
	}

}
