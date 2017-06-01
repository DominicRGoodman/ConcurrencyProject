import java.util.ArrayList;
import java.util.Scanner;

/**
 * Ship.java
 * 
 * @author Dominic Goodman 3/26/2017 updated 4/7/2017 Purpose: Contains data of
 *         the ship including the list of jobs the ship needs
 */
public class Ship extends Thing
{
	portTime arrivalTime, dockTime;
	double draft, length, weight, width;
	ArrayList<Job> jobs = new ArrayList<Job>();
	Mode sortMode = Mode.NAME;

	public enum Mode
	{
		NAME, DRAFT, LENGTH, WEIGHT, WIDTH
	}

	public Ship()
	{
		name = "Empty";
		index = 0;
	}

	public Ship(Scanner sc)
	{
		super(sc);
		if (sc.hasNextDouble())
			weight = sc.nextDouble();
		if (sc.hasNextDouble())
			length = sc.nextDouble();
		if (sc.hasNextDouble())
			width = sc.nextDouble();
		if (sc.hasNextDouble())
			draft = sc.nextDouble();

	}

	public String toString()
	{

		return "Ship: " + super.toString();
	}

	public String listSummary()
	{
		String summary = toString() + "\n";

		for (Job nextJob : jobs)
		{
			summary += "  >" + nextJob.toString() + "\n";
		}
		return summary;
	}

	public int compareTo(Thing arg0)
	{

		if (arg0 instanceof Ship)
		{
			Ship next = (Ship) arg0;
			switch (sortMode)
			{
			case NAME:
				return super.compareTo(arg0);
			case DRAFT:
				if (draft == next.draft)
					return 0;
				if (draft > next.draft)
					return 1;
				return -1;
			case LENGTH:
				if (length == next.length)
					return 0;
				if (length > next.length)
					return 1;
				return -1;
			case WEIGHT:
				if (weight == next.weight)
					return 0;
				if (weight > next.weight)
					return 1;
				return -1;
			case WIDTH:
				if (width == next.width)
					return 0;
				if (width > next.width)
					return 1;
				return -1;
			default:
				return 0;
			}

		} else
			return super.compareTo(arg0);

	}

	public synchronized boolean isCompleted()
	{

		for (Job nextJob : jobs)
		{
			if (!nextJob.completed)
			{
				return false;
			}
		}
		return true;
	}

}
