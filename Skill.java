/**
 * 
 * @author Dominic Goodman
 * 
 *         To be used with workers and ships to track current and needed skills
 * 
 */
public class Skill
{
	String type;

	public Skill(String type)
	{
		this.type = type;
	}

	public String toString()
	{
		return type;
	}
}
