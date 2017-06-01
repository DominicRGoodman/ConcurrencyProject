import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WorkerQueue
{
	private Map<String, Vector<Person>> skillList = Collections
			.synchronizedMap(new HashMap<String, Vector<Person>>());

	public WorkerQueue(ArrayList<Person> workers)
	{
		for (Person nextWorker : workers)
		{
			setWorker(nextWorker);
		}
	}

	public synchronized void setWorker(Person nextWorker)
	{
		if (skillList.containsKey(nextWorker.skill))
		{
			skillList.get(nextWorker.skill.type).add(nextWorker);
		} else
		{
			Vector<Person> nextList = new Vector<Person>();
			nextList.add(nextWorker);
			skillList.put(nextWorker.skill.type, nextList);
		}
	}

	public synchronized Person searchWorker(Skill nextSkill)
			throws NullPointerException
	{

		if (skillList.containsKey(nextSkill.type))
		{
			for (Person nextPerson : skillList.get(nextSkill.type))
			{
				if (nextPerson.isFree)
				{
					nextPerson.isFree = false;
					this.notify();
					return nextPerson;
				}
			}
			return null;
		}
		throw new NullPointerException();
	}

}
