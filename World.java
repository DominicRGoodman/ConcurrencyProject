import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * World.java
 * 
 * @author Dominic Goodman 3/26/2017 updated 4/7/2017 Purpose: Maintains a list
 *         of ports, allows for searching the list and the lists own lists to
 *         find keyterms given from user
 */
public class World extends Thing
{
	ArrayList<SeaPort> ports = new ArrayList<SeaPort>();
	portTime time;
	HashMap<Integer, Thing> listOfThings = new HashMap<Integer, Thing>();

	/**
	 * Defines the possible modes of search
	 */
	public enum Mode
	{
		NAME, INDEX, SKILL
	}

	/**
	 * 
	 * @param infile
	 *            - file containing data used to make lists
	 * @throws NullPointerException
	 *             - thrown when process tries to add a Thing that doesn't exist
	 * @throws FileNotFoundException
	 */
	public World(File infile) throws NullPointerException,
			FileNotFoundException
	{
		Scanner scan;
		try
		{
			scan = new Scanner(infile);
		} catch (FileNotFoundException e)
		{
			throw new FileNotFoundException("Invalid File");
		}

		while (scan.hasNextLine())
		{
			String next = scan.nextLine();
			if (next.equals(""))
				continue;
			process(next);
		}

		scan.close();
		for (SeaPort nextPort : ports)
		{
			nextPort.freeWorkers = new WorkerQueue(nextPort.persons);
		}

	}

	/**
	 * 
	 * @param nextLine
	 *            - line to be analyzed
	 * 
	 */
	private void process(String nextLine)
	{
		Scanner sc = new Scanner(nextLine);
		String next = sc.next();
		// determines next Thing to create and sends the scanner appropriately
		switch (next)
		{
		case "port":
			SeaPort nextPort = new SeaPort(sc);
			listOfThings.put(nextPort.index, nextPort);
			ports.add(nextPort);
			break;
		case "dock":
			Dock nextDock = new Dock(sc);
			listOfThings.put(nextDock.index, nextDock);

			try
			{
				((SeaPort) listOfThings.get(nextDock.parent)).docks
						.add(nextDock);
			} catch (NullPointerException | ClassCastException e1)
			{
				throw new NullPointerException("Target Port does not exist");
			}
			break;
		case "pship":
			PassengerShip nextPShip = new PassengerShip(sc);
			listOfThings.put(nextPShip.index, nextPShip);

			try
			{
				Dock target = (Dock) listOfThings.get(nextPShip.parent);
				target.ship = nextPShip;
				SeaPort parentPort = (SeaPort) listOfThings.get(target.parent);
				parentPort.ships.add(nextPShip);
			} catch (NullPointerException | ClassCastException e1)
			{
				try
				{
					SeaPort parentPort = (SeaPort) listOfThings
							.get(nextPShip.parent);
					parentPort.que.add(nextPShip);
					parentPort.ships.add(nextPShip);
				} catch (NullPointerException e2)
				{
					throw new NullPointerException(
							"Target Port does not exist.");
				}
			}
			break;
		case "cship":
			CargoShip nextCShip = new CargoShip(sc);
			listOfThings.put(nextCShip.index, nextCShip);

			try
			{
				Dock target = (Dock) listOfThings.get(nextCShip.parent);
				target.ship = nextCShip;
				SeaPort parentPort = (SeaPort) listOfThings.get(target.parent);
				parentPort.ships.add(nextCShip);
			} catch (NullPointerException | ClassCastException e1)
			{
				try
				{
					SeaPort parentPort = (SeaPort) listOfThings
							.get(nextCShip.parent);
					parentPort.que.add(nextCShip);
					parentPort.ships.add(nextCShip);
				} catch (NullPointerException | ClassCastException e2)
				{
					throw new NullPointerException(
							"Target Parent does not exist.");
				}
			}
			break;
		case "person":
			Person nextPerson = new Person(sc);
			listOfThings.put(nextPerson.index, nextPerson);

			try
			{
				((SeaPort) listOfThings.get(nextPerson.parent)).persons
						.add(nextPerson);
			} catch (NullPointerException | ClassCastException e1)
			{
				throw new NullPointerException("Target Port does not exist.");
			}
			break;
		case "job":
			Job nextJob = new Job(sc);
			listOfThings.put(nextJob.index, nextJob);
			try
			{
				((Ship) listOfThings.get(nextJob.parent)).jobs.add(nextJob);

			} catch (NullPointerException | ClassCastException e1)
			{
				throw new NullPointerException("Target Ship does not exist.");
			}
			break;

		}
		sc.close();
	}

	/**
	 * 
	 * @param type
	 *            - mode to change all ships to
	 * 
	 *            changes the mode of all ships sort
	 */
	public void setShipSortMode(String type)
	{
		for (SeaPort nextPort : ports)
		{
			for (Ship nextShip : nextPort.ships)
			{
				switch (type)
				{
				case "Draft":
					nextShip.sortMode = Ship.Mode.DRAFT;
					break;
				case "Length":
					nextShip.sortMode = Ship.Mode.LENGTH;
					break;
				case "Width":
					nextShip.sortMode = Ship.Mode.WIDTH;
					break;
				case "Weight":
					nextShip.sortMode = Ship.Mode.WEIGHT;
					break;
				default:
					nextShip.sortMode = Ship.Mode.NAME;
					break;
				}

			}
			for (Ship nextShip : nextPort.que)
			{
				switch (type)
				{
				case "Draft":
					nextShip.sortMode = Ship.Mode.DRAFT;
					break;
				case "Length":
					nextShip.sortMode = Ship.Mode.LENGTH;
					break;
				case "Width":
					nextShip.sortMode = Ship.Mode.WIDTH;
					break;
				case "Weight":
					nextShip.sortMode = Ship.Mode.WEIGHT;
					break;
				default:
					nextShip.sortMode = Ship.Mode.NAME;
					break;
				}

			}
		}
	}

	/**
	 * 
	 * @param keyword
	 *            - term to be searched for
	 * @param modes
	 *            - mode to be used by switch statement
	 * @return - a String containing the toStrings of all found Things
	 */
	public String search(String keyword, Mode modes)
	{
		ArrayList<Thing> foundThings = new ArrayList<Thing>();
		String toSend = "None found!";
		switch (modes)
		{
		case NAME:
			foundThings = searchByName(keyword);
			break;
		case INDEX:
			try
			{
				toSend = searchByIndex(Integer.parseInt(keyword)).toString();
			} catch (NullPointerException e1)
			{
			}
			break;
		case SKILL:
			for (Person nextPerson : searchBySkill(keyword))
			{
				toSend += nextPerson.toString() + "\n";
			}
			return toSend;
		}
		if (foundThings.size() > 0)
		{
			toSend = "";
		}

		for (Thing nextThing : foundThings)
		{

			toSend += nextThing.toString() + "\n";
		}
		return toSend;
	}

	/**
	 * 
	 * @param x
	 *            - target index to be found
	 * @return an ArrayList<Thing> of found targets
	 * 
	 *         Returns more then 1 object if the defined file has more then 1
	 *         matching index
	 */
	private Thing searchByIndex(int x)
	{
		return listOfThings.get(x);
	}

	/**
	 * @param keyword
	 *            - target term to be found
	 * @return - an ArrayList<Thing> of found targets
	 * 
	 *         Searches all Things for all matches
	 */
	private ArrayList<Thing> searchByName(String keyword)
	{
		ArrayList<Thing> targets = new ArrayList<Thing>();
		for (SeaPort msp : ports)
		{
			if (msp.name.equalsIgnoreCase(keyword))
				targets.add(msp);
			for (Dock md : msp.docks)
				if (md.name.equalsIgnoreCase(keyword))
					targets.add(md);
			for (Ship ms : msp.ships)
			{
				if (ms.name.equalsIgnoreCase(keyword))
					targets.add(ms);
				for (Job mj : ms.jobs)
				{
					if (mj.name.equalsIgnoreCase(keyword))
						targets.add(mj);
				}
			}
			for (Person mp : msp.persons)
			{
				if (mp.name.equalsIgnoreCase(keyword))
					targets.add(mp);
			}
		}
		return targets;
	}

	/**
	 * @param keyword
	 *            - term to be found
	 * @return - list of found matching objects
	 */
	private ArrayList<Person> searchBySkill(String keyword)
	{
		ArrayList<Person> targets = new ArrayList<Person>();
		for (SeaPort msp : ports)
		{
			for (Person mp : msp.persons)
			{
				if (mp.skill.equals(new Skill(keyword)))
					targets.add(mp);
			}
		}
		return targets;
	}

	public String toString()
	{
		String list = "";
		for (SeaPort nextPort : ports)
		{
			list += nextPort.getPortList();
		}
		return list;
	}

	public void begin()
	{
		for (SeaPort nextPort : ports)
		{
			(new Thread(nextPort)).start();
		}
	}
}
