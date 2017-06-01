import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressPanel extends JPanel implements TableCellRenderer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1542225607711504725L;

	public Component getTableCellRendererComponent(JTable table, Object panel,
			boolean isSelected, boolean hasFocus, int row, int column)
	{

		return (JPanel) panel;
	}
}
