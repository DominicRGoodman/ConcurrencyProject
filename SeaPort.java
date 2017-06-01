import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * SeaPort.java
 * 
 * @author Dominic Goodman 3/26/2017
 * 
 *         Purpose: Maintains the list of all docks, ships, and persons present
 *         at the port. Also allows for printing a list of all values stored
 */
public class SeaPort extends Thing implements Runnable
{
	ArrayList<Dock> docks = new ArrayList<Dock>();
	ArrayList<Ship> que = new ArrayList<Ship>();
	ArrayList<Ship> ships = new ArrayList<Ship>();
	ArrayList<Person> persons = new ArrayList<Person>();
	WorkerQueue freeWorkers;

	public SeaPort(Scanner sc)
	{
		super(sc);
	}

	private synchronized Ship getNextInQueue()
	{
		List<Ship> shipsInQueue = Collections.synchronizedList(que);
		if (shipsInQueue.isEmpty())
			throw new NullPointerException();
		Ship nextShip = shipsInQueue.get(0);
		shipsInQueue.remove(0);
		return nextShip;
	}

	public void run()
	{
		List<Dock> docksAtPort = Collections.synchronizedList(docks);

		for (Dock nextDock : docksAtPort)
		{
			nextDock.workers = freeWorkers;
			(new Thread(nextDock, nextDock.name)).start();
		}
		while (!que.isEmpty())
		{
			for (Dock nextDock : docksAtPort)
			{
				if (nextDock.isFinished)
				{
					try
					{
						nextDock.changeShip(getNextInQueue());
					} catch (NullPointerException e)
					{
						continue;
					}
				}
			}
		}
	}

	public String toString()
	{
		return "Port: " + super.toString();
	}

	/**
	 * 
	 * @return - a String containing all the data stored within the SeaPort and
	 *         it's lists
	 */
	public String getPortList()
	{
		String list = "";
		list += "Port: " + super.toString();
		Collections.sort(ships);
		Collections.sort(que);

		for (Dock nextDock : docks)
		{
			list += "\n" + nextDock.listSummary();
		}
		list += "\n\n" + "--Ships at port";
		for (Ship nextShip : ships)
		{
			list += "\n>" + nextShip.toString();
		}
		list += "\n\n" + "--Ships in queue";
		for (Ship nextShip : que)
		{
			list += "\n>" + nextShip.listSummary();

		}
		list += "\n\n" + "--People on port";
		for (Person nextPerson : persons)
		{
			list += "\n>" + nextPerson.listSummary();
		}
		return list + "\n\n";
	}

}
