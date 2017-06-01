import java.util.Scanner;

/**
 * PassengerShip.java
 * 
 * @author Dominic Goodman 3/26/2017
 * 
 *         Purpose: Contains data of a passenger ship
 */
public class PassengerShip extends Ship
{

	int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;

	public PassengerShip(Scanner sc)
	{
		super(sc);
		if (sc.hasNextInt())
			numberOfPassengers = sc.nextInt();
		if (sc.hasNextInt())
			numberOfRooms = sc.nextInt();
		if (sc.hasNextInt())
			numberOfOccupiedRooms = sc.nextInt();
	}

	public String toString()
	{
		return "Passenger " + super.toString();
	}
}
