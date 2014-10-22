import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author Sohil Shah
 * @author Adiba Arif
 * @author Sneha Rateria
 * 
 * This class creates the GUI using java swings which is required for displaying the output.
 * 
 */
public class BigDataProject {
	static Object dataValues_max[][] = { { "", "", "", "" } };
	static Object dataValues_min[][] = { { "", "", "", "" } };
	static Object dataValues_state[][] = { { "", "", "", "" } };
	static String ColumnNames_max[] = { "Max City Name", "2010", "2011", "2012" };
	static String ColumnNames_min[] = { "Min City Name", "2010", "2011", "2012" };
	static String ColumnNames_state[] = { "State Name" };
	static JTable max_table = new JTable(dataValues_max, ColumnNames_max);
	static JTable min_table = new JTable(dataValues_max, ColumnNames_min);
	static JTable state_table = new JTable(dataValues_max, ColumnNames_state);

	/*
	 * This method uses the layered architecture. Panels have been put one 
	 * on top of the other. The heirarchy is as follows
	 * 
	 * 	frame
	 * 		panel
	 * 			panel1
	 * 				panel11 - contains Jlabel and JTextfields
	 * 				panel12 - contains JButton named refresh.
	 * 			panel2 - contains the 3 tables where output is to be shown.
	 * 	
	 */
	public static void main(String args[]) {
		final JFrame frame = new JFrame();
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		JPanel panel12 = new JPanel();
		JPanel panel11 = new JPanel();
		panel11.setLayout(new BoxLayout(panel11, BoxLayout.X_AXIS));
		panel11.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JLabel no_of_entries = new JLabel();
		no_of_entries.setText("Enter the number of cities desired - ");
		panel11.add(no_of_entries);

		final JTextField text = new JTextField();
		text.createToolTip();
		text.setToolTipText("Enter the number of cities to be displayed.");
		panel11.add(text);

		JLabel population = new JLabel();
		population.setText("Enter threshold population : ");
		panel11.add(population);

		final JTextField populationText = new JTextField();
		populationText.createToolTip();
		populationText.setToolTipText("Enter the threshold population.");
		panel11.add(populationText);

		JLabel statesText = new JLabel();
		statesText.setText("Enter number of states : ");
		panel11.add(statesText);

		final JTextField numOfStates = new JTextField();
		numOfStates.createToolTip();
		numOfStates.setToolTipText("Enter number of states.");

		panel11.add(numOfStates);

		JButton button = new JButton();
		button.setText("REFRESH");
		Dimension dimButton = new Dimension(100, 40);
		button.addActionListener(new ActionListener() {
			/*
			 * This method is called when refresh button is pressed it creates the data
			 * updates it to the tables.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = text.getText();
				String population = populationText.getText();
				String stateNum = numOfStates.getText();
				if (value.length() == 0 || population.length() == 0
						|| stateNum.length() == 0) {
					JOptionPane.showMessageDialog(frame,
							"Value cannot be empty", "Inane error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int n = Integer.parseInt(value);
					PromisingMarkets pm;
					int pop = Integer.parseInt(population);
					int state = Integer.parseInt(stateNum);
					if (n == 0 || pop == 0 || state == 0) {
						JOptionPane.showMessageDialog(frame,
								"Value cannot be zero", "Inane error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						try {

							pm = new PromisingMarkets(n, pop, state);
							pm.init();
							max_table.invalidate();
							dataValues_max = new Object[pm.maxGrowth.length][4];
							dataValues_min = new Object[pm.minGrowth.length][4];
							dataValues_state = new Object[pm.stateNode.length][1];

							for (int i = 0; i < pm.maxGrowth.length; i++) {
								if (pm.maxGrowth[i] != null) {
									dataValues_max[i][0] = pm.maxGrowth[i].city;
									dataValues_max[i][1] = pm.maxGrowth[i].population_in_2010;
									dataValues_max[i][2] = pm.maxGrowth[i].population_in_2011;
									dataValues_max[i][3] = pm.maxGrowth[i].population_in_2012;
								}
							}
							for (int i = 0; i < pm.minGrowth.length; i++) {
								if (pm.minGrowth[i] != null) {
									dataValues_min[i][0] = pm.minGrowth[i].city;
									dataValues_min[i][1] = pm.minGrowth[i].population_in_2010;
									dataValues_min[i][2] = pm.minGrowth[i].population_in_2011;
									dataValues_min[i][3] = pm.minGrowth[i].population_in_2012;
								}
							}
							for (int i = 0; i < pm.stateNode.length; i++) {
								if (pm.stateNode[i] != null) {
									dataValues_state[i][0] = pm.stateNode[i].state;

								}
							}
							panel2.removeAll();
							panel2.revalidate();
							panel2.repaint();
							DefaultTableModel model_max = new DefaultTableModel(
									dataValues_max, ColumnNames_max);
							DefaultTableModel model_min = new DefaultTableModel(
									dataValues_min, ColumnNames_min);
							DefaultTableModel model_state = new DefaultTableModel(
									dataValues_state, ColumnNames_state);
							max_table = new JTable(model_max);
							max_table.revalidate();
							max_table.repaint();
							max_table.setEnabled(false);
							max_table = setTableProperties(max_table);
							min_table = new JTable(model_min);
							min_table.revalidate();
							min_table.repaint();
							min_table.setEnabled(false);
							min_table = setTableProperties(min_table);
							state_table = new JTable(model_state);
							state_table.revalidate();
							state_table.repaint();
							state_table.setEnabled(false);
							state_table = setTableProperties(state_table);
							panel2.add(new JScrollPane(max_table),
									BoxLayout.X_AXIS);
							panel2.add(new JScrollPane(min_table),
									BoxLayout.X_AXIS);
							panel2.add(new JScrollPane(state_table),
									BoxLayout.X_AXIS);
							panel2.revalidate();
							panel2.repaint();

						} catch (Exception e1) {
							System.out.println(e1);
						}
					}
				}
			}
		});
		button.setPreferredSize(dimButton);
		panel12.add(button);

		panel1.add(panel11);
		panel1.add(panel12);

		setTableProperties(max_table);

		panel.add(panel1);
		panel.add(panel2);

		panel2.add(new JScrollPane(max_table), BoxLayout.X_AXIS);
		panel2.add(new JScrollPane(min_table), BoxLayout.X_AXIS);
		panel2.add(new JScrollPane(state_table), BoxLayout.X_AXIS);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		frame.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				text.requestFocusInWindow();
			}
		});
	}
	/*
	 * Method that sets the properties of JTable.
	 */
	public static JTable setTableProperties(JTable table) {
		table.setAutoscrolls(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return table;
	}
}
