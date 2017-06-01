import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * SeaPortProgram.java
 * 
 * @author Dominic Goodman 3/26/2017
 * 
 *         Purpose: Contains the duration of the job and its requirements of the
 *         person to complete it
 */
public class Job extends Thing implements Runnable
{

	double duration;
	JProgressBar progress;
	// some of the skills of the persons
	ArrayList<Skill> requirements = new ArrayList<Skill>();
	boolean completed = false;
	ArrayList<Person> workers = new ArrayList<Person>();
	WorkerQueue listOfHirees;

	public Job(Scanner sc)
	{
		super(sc);
		if (sc.hasNextDouble())
			duration = sc.nextDouble();
		while (sc.hasNext())
		{
			requirements.add(new Skill(sc.next()));
		}
		progress = new JProgressBar(0, (int) Math.round(duration));
		progress.setStringPainted(true);
		progress.setForeground(Color.red);
		progress.setValue(progress.getMaximum());
		progress.setString("WAITING");
		String temp = name;
		for (Skill next : requirements)
		{
			temp += " " + next;
		}
		progress.setToolTipText(temp);

	}

	public String toString()
	{
		String list = "Job: " + super.toString();
		for (Skill req : requirements)
		{
			list += " " + req.toString();
		}
		return list;
	}

	public synchronized JProgressBar getProgress()
	{
		return progress;
	}

	public void run()
	{
		boolean started = false;
		try
		{
			while (!started)
				for (Skill next : requirements)
				{
					Person nextWorker = listOfHirees.searchWorker(next);
					if (nextWorker != null)
					{
						workers.add(nextWorker);
					} else
					{
						for (Person nextPerson : workers)
							nextPerson.isFree = true;
						workers.removeAll(workers);
					}
					if (requirements.size() == workers.size())
					{
						started = true;
						break;
					}
				}
		} catch (NullPointerException e)
		{
			progress.setString("CANNOT START");
			completed = true;
			return;
		}
		String toolTip = toString();
		for (Person worker : workers)
			toolTip += " " + worker.name;
		progress.setValue(0);
		progress.setToolTipText(toolTip);
		progress.setForeground(Color.blue);
		progress.setString(null);

		while (progress.getPercentComplete() < 1)
		{
			try
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						progress.setValue(progress.getValue() + 1);
					}
				});
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
			}
			if (completed)
				break;
		}
		for (Person nextWorker : workers)
			nextWorker.isFree = true;
		completed = true;
	}

}
