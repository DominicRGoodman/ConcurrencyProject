import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * SeaPortProgram.java
 * 
 * @author Dominic Goodman 3/26/2017 updated 4/21/2017
 * 
 *         Purpose: To create a GUI that will handle a new world that will use
 *         internal data structures to handle the data contained with a text
 *         file
 */

public class SeaPortProgram extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static World world;

	/**
	 * Creates the GUI and defines close operation and minimum size
	 */
	public SeaPortProgram()
	{
		super("Sea Port Program");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
	}

	public static void main(String[] args)
	{

		final JFileChooser fileChooser = new JFileChooser(".");
		// forces user to select a file
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				world = new World(fileChooser.getSelectedFile());
			} catch (NullPointerException e1)
			{
				JOptionPane.showMessageDialog(new JPanel(), e1.getMessage()
						+ ": Exiting");
				System.exit(0);

			} catch (FileNotFoundException e2)
			{
				JOptionPane.showMessageDialog(new JPanel(), e2.getMessage()
						+ ": Exiting");
				System.exit(0);
			}
		} else
		{
			JOptionPane.showMessageDialog(new JPanel(), "No file selected");
			System.exit(0);
		}

		JFrame launchMode = new JFrame("Launch");
		launchMode.setSize(300, 100);
		launchMode.setResizable(false);
		launchMode.setLayout(new GridLayout(1, 2));
		JButton legBtn = new JButton("Legacy Verison");
		launchMode.add(legBtn);
		JButton ThreadBtn = new JButton("Threaded Verison");
		launchMode.setLocationRelativeTo(null);
		launchMode.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		launchMode.add(ThreadBtn);
		legBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				originalOptions();
			}
		});
		ThreadBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				threadedVer();
			}
		});

		launchMode.setVisible(true);

	}

	/**
	 * Functions added for project 3 and 4
	 */
	public static void threadedVer()
	{
		world.begin();
		final SeaPortProgram GUI = new SeaPortProgram();

		GUI.setLayout(new BorderLayout());
		JButton workersButton = new JButton("Workers");
		workersButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String workerInfo = "";
				for (SeaPort nextSeaPort : world.ports)
				{
					for (Person nextPerson : nextSeaPort.persons)
					{
						workerInfo += nextPerson.name;
						if (nextPerson.isFree)
							workerInfo += " is free.\n";
						else
							workerInfo += " is working.\n";
					}

				}
				JFrame workInfo = new JFrame("Workers");
				workInfo.setLayout(new BorderLayout());

				JTextArea output = new JTextArea(workerInfo);
				output.setEditable(false);
				workInfo.setSize(200, 200);
				workInfo.setLocationRelativeTo(null);
				workInfo.add(output, BorderLayout.CENTER);

				JScrollPane aScrollBar = new JScrollPane(output);
				aScrollBar
						.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				workInfo.add(aScrollBar, BorderLayout.CENTER);
				workInfo.setVisible(true);
			}

		});
		GUI.add(workersButton, BorderLayout.SOUTH);
		int numOfRows = 0;
		String[] headers =
		{ "Port", "Dock", "Ship", "Working Jobs" };
		for (SeaPort nextPort : world.ports)
		{
			numOfRows += nextPort.docks.size();
		}
		Object[][] data = new Object[numOfRows][4];
		int row = 0;
		for (SeaPort nextPort : world.ports)
		{
			for (final Dock nextDock : nextPort.docks)
			{
				data[row][0] = nextPort.name;
				data[row][1] = nextDock.name;
				JPanel nextCell = new JPanel();
				nextCell.setLayout(new GridLayout(1, 1));
				nextCell.add(nextDock.docked);
				data[row][2] = nextCell;
				nextCell = new JPanel();
				nextCell.setLayout(new GridLayout(1, 1));
				JButton jobButton = new JButton("Jobs");
				jobButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JFrame jobsInfo = new JFrame("Job Times");
						jobsInfo.setSize(300, 300);
						jobsInfo.setLocationRelativeTo(null);
						jobsInfo.setLayout(new GridLayout(nextDock.ship.jobs
								.size(), 2));
						jobsInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						for (final Job nextJob : nextDock.ship.jobs)
						{
							final JProgressBar nextBar = nextJob.getProgress();
							jobsInfo.add(nextBar);
							JButton cancelButton = new JButton("Cancel");
							if (nextJob.workers.size() == nextJob.requirements
									.size())
							{
								cancelButton
										.addActionListener(new ActionListener()
										{
											public void actionPerformed(
													ActionEvent e)
											{
												nextJob.completed = true;
												nextBar.setValue(nextBar
														.getMaximum());
												for (Person nextWorker : nextJob.workers)
													nextWorker.isFree = true;
											}
										});
							} else
								cancelButton.setEnabled(false);
							jobsInfo.add(cancelButton);
						}

						jobsInfo.setVisible(true);
					}
				});
				nextCell.add(jobButton);
				data[row][3] = nextCell;
				row++;
			}
		}

		final JTable table = new JTable(new DefaultTableModel(data, headers))
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			};
		};
		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				int col = table.columnAtPoint(p);
				if (col == 3)
				{
					try
					{
						JPanel target = (JPanel) table.getValueAt(row, col);
						((JButton) target.getComponent(0)).getActionListeners()[0]
								.actionPerformed(new ActionEvent(this,
										ActionEvent.ACTION_PERFORMED, null)
								{
									private static final long serialVersionUID = 1L;
								});
					} catch (ArrayIndexOutOfBoundsException e)
					{

					}
				}

			}
		});
		table.getColumnModel().getColumn(2)
				.setCellRenderer(new ProgressPanel());
		table.getColumnModel().getColumn(3)
				.setCellRenderer(new ProgressPanel());
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		GUI.add(table.getTableHeader(), BorderLayout.NORTH);
		GUI.add(table, BorderLayout.CENTER);
		final JScrollPane scrollBar = new JScrollPane(table);
		scrollBar
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GUI.add(scrollBar, BorderLayout.CENTER);
		GUI.setVisible(true);

	}

	/**
	 * Functions added for project 1 and 2
	 */
	public static void originalOptions()
	{

		SeaPortProgram GUI = new SeaPortProgram();
		// defines the textArea for the output information
		final JTextArea output = new JTextArea(world.toString());
		output.setEditable(false);
		output.setFont(new java.awt.Font("Monospaced", 0, 12));
		final JScrollPane scrollBar = new JScrollPane(output);
		scrollBar
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GUI.add(scrollBar, BorderLayout.CENTER);

		// defines layout of the search panel
		final JPanel searchArea = new JPanel();
		searchArea.setLayout(new GridLayout(3, 1, 3, 3));

		// input location for search term
		final JTextField searchTerm = new JTextField();
		searchTerm.setFont(new java.awt.Font("Monospaced", 0, 12));

		TitledBorder searchTextTitle = new TitledBorder("Search Term");
		searchTerm.setBorder(searchTextTitle);

		searchArea.add(searchTerm);
		// defines panel to contain options for searching
		final JPanel searchOptions = new JPanel();

		TitledBorder searchOpTitle = new TitledBorder("Search Options");
		searchOptions.setBorder(searchOpTitle);

		final JRadioButton nameOption = new JRadioButton("Name");
		final JRadioButton indexOption = new JRadioButton("Index #");
		final JRadioButton skillOption = new JRadioButton("Skill");
		final JButton searchButton = new JButton("Search");
		final JButton resetButton = new JButton("Reset");
		ButtonGroup searchRadio = new ButtonGroup();

		nameOption.setSelected(true);

		nameOption.setFont(new java.awt.Font("Monospaced", 0, 12));
		indexOption.setFont(new java.awt.Font("Monospaced", 0, 12));
		skillOption.setFont(new java.awt.Font("Monospaced", 0, 12));

		searchRadio.add(nameOption);
		searchRadio.add(indexOption);
		searchRadio.add(skillOption);

		searchOptions.add(nameOption);
		searchOptions.add(indexOption);
		searchOptions.add(skillOption);
		searchOptions.add(searchButton);
		searchOptions.add(resetButton);

		// Sorting option area
		JPanel sortOptions = new JPanel();
		sortOptions.setLayout(new GridLayout(2, 1, 3, 3));
		JLabel sortShipLabel = new JLabel("Ship sort by:");
		final JComboBox<String> shipSort = new JComboBox<String>();
		TitledBorder sortTextTitle = new TitledBorder("Sort Options");
		sortOptions.setBorder(sortTextTitle);
		shipSort.addItem("Name");
		shipSort.addItem("Width");
		shipSort.addItem("Draft");
		shipSort.addItem("Length");
		shipSort.addItem("Weight");
		sortOptions.add(sortShipLabel);
		sortOptions.add(shipSort);

		// changes output to use current search options
		shipSort.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				world.setShipSortMode((String) shipSort.getSelectedItem());
				output.setText(world.toString());
				output.setCaretPosition(0);

			}
		});

		// defines the reset button's actions
		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (int i = 0; i < 2; i++)
				{
					output.setText(world.toString());
					output.setCaretPosition(0);
					searchTerm.setText("");
				}
			}
		});
		// defines search buttons actions
		searchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String target = searchTerm.getText();
				if (nameOption.isSelected())
				{
					output.setText(world.search(target, World.Mode.NAME));
				} else if (indexOption.isSelected())
				{
					output.setText(world.search(target, World.Mode.INDEX));
				} else if (skillOption.isSelected())
				{
					output.setText(world.search(target, World.Mode.SKILL));
				}

			}
		});

		searchArea.add(searchOptions);
		searchArea.add(sortOptions);
		GUI.add(searchArea, BorderLayout.NORTH);

		GUI.setVisible(true);
	}
}
