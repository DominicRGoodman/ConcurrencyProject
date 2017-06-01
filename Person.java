import java.util.Scanner;

/**
 * Person.java
 * 
 * @author Dominic Goodman 3/26/2017
 * 
 *         Purpose: Stores data of the person along with what they are skilled
 *         in
 */
public class Person extends Thing
{

	Skill skill;
	boolean isFree = true;

	public Person(Scanner sc)
	{
		super(sc);
		if (sc.hasNext())
			skill = new Skill(sc.next());
	}

	public String toString()
	{
		return "Person: " + super.toString() + " " + skill.toString() + " "
				+ isFree;
	}

}
