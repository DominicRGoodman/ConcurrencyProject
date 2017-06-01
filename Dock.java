import java.util.Scanner;

import javax.swing.JTextArea;

/**
 * Dock.java
 * 
 * @author Dominic Goodman 3/26/2017 updated: 4/23/2017 Purpose: Maintains a
 *         link between the SeaPort and a ship contained by it and
 */
public class Dock extends Thing implements Runnable
{

	volatile Ship ship;
	boolean isFinished = false;
	JTextArea docked = new JTextArea();
	WorkerQueue workers;

	public Dock(Scanner sc)
	{
		super(sc);

	}

	public String toString()
	{
		return "Pier: " + super.toString();
	}

	public String listSummary()
	{
		return "Pier: " + super.toString() + "\n " + ship.toString();

	}

	public synchronized void changeShip(Ship nextShip)
	{
		isFinished = false;
		ship = nextShip;
		run();
	}

	public synchronized void startJobs(Job nextJob)
	{
		// for (Skill nextSkill : nextJob.requirements)
		// {
		// try
		// {
		// Person nextPerson = workers.searchWorker(nextSkill);
		// nextJob.workers.add(nextPerson);
		// // workers.searchWorker(nextJob);
		// } catch (NullPointerException e)
		// {
		// nextJob.progress.setString("CANNOT START");
		// return;
		// }
		// }
		// System.out.println("Starting " + nextJob.name+" "+ship.name);
		nextJob.listOfHirees = workers;
		new Thread(nextJob, ship.name).start();
	}

	public void run()
	{
		docked.setText(ship.name);
		for (Job nextJob : ship.jobs)
			startJobs(nextJob);

		while (!ship.isCompleted())
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		ship = new Ship();
		docked.setText(ship.name);
		isFinished = true;
	}
}
