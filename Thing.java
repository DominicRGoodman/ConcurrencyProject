import java.util.Scanner;

/**
 * Thing.java
 * 
 * @author Dominic Goodman 3/26/2017 updated 4/7/2017 Purpose: Parent to most
 *         classes present in this project, maintains the simple data of name,
 *         index, and the parent object's index
 */
public class Thing implements Comparable<Thing>
{
	int index;
	String name;
	int parent;

	public Thing()
	{

	}

	public Thing(Scanner sc)
	{
		if (sc.hasNext())
			name = sc.next();
		if (sc.hasNext())
			index = sc.nextInt();
		if (sc.hasNext())
			parent = sc.nextInt();
	}

	public String toString()
	{

		return name + " " + index;
	}

	public String listSummary()
	{
		return toString();
	}

	public int compareTo(Thing arg0)
	{
		return name.compareTo(arg0.name);
	}

}
