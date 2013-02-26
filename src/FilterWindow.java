import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class used to filter tasks
 * @author Charles Henry
 *
 */
public class FilterWindow extends JFrame implements ActionListener{
	
	private static JPanel mainPanel;
	private static JPanel upperPanel;
	private static JPanel lowerPanel;
	private static JLabel label;
	private static JLabel label2;
	private static JLabel label3;
	private static JLabel label4;
	private static JComboBox filterCombo;
	private static JComboBox priorityCombo;
	private static JComboBox categoryCombo;
	private static JTextField input;
	private static JButton filter;
	private static JButton cancel;
	private static int filterLoop;
	
	private static String[] filterOptions = {"Name", "Category", "Priority"};
	private static String[] priorityList = {"Low", "Medium", "High", "Other"};
	private static String[] categoryList = {"Personal", "Social", "Business", "School", "Other"};
	
	/**
	 * Method used to create a filter window
	 */
	FilterWindow()
	{
		this.setLayout(new BorderLayout());
		this.setTitle("Filter Tasks");
		this.setSize(350,150);
		this.setResizable(false);
		
		label = new JLabel("Category:");
		label2 = new JLabel("Is:");
		label3 = new JLabel("Is:");
		label4 = new JLabel("Contains:");
		label.setHorizontalAlignment(JTextField.CENTER);
		label2.setHorizontalAlignment(JTextField.CENTER);
		label3.setHorizontalAlignment(JTextField.CENTER);
		label4.setHorizontalAlignment(JTextField.CENTER);
		filterCombo = new JComboBox(filterOptions);
		filterCombo.addActionListener(this);
		priorityCombo = new JComboBox(priorityList);
		categoryCombo = new JComboBox(categoryList);
		input = new JTextField(10);
		
		upperPanel = new JPanel(new FlowLayout());				//Create a panel with a flow layout
		upperPanel.add(label);
		upperPanel.add(filterCombo);
		
		
		lowerPanel = new JPanel(new FlowLayout());
		lowerPanel.add(label2); 	lowerPanel.add(priorityCombo);
		lowerPanel.add(label3); 	lowerPanel.add(categoryCombo);
		lowerPanel.add(label4); 	lowerPanel.add(input);
		label3.setVisible(false); 	categoryCombo.setVisible(false);
		label2.setVisible(false); 	priorityCombo.setVisible(false);
		
		mainPanel = new JPanel(new GridLayout(2,1));
		mainPanel.add(upperPanel);
		mainPanel.add(lowerPanel);
		this.getContentPane().add(BorderLayout.CENTER, mainPanel);	//Add the panel to the CENTER of the BorderLayout
		
		filter = new JButton("Filter");
		cancel = new JButton("Cancel");
		filter.addActionListener(this);
		cancel.addActionListener(this);
		
		lowerPanel = new JPanel(new FlowLayout());
		lowerPanel.add(filter);
		lowerPanel.add(cancel);
		this.getContentPane().add(BorderLayout.SOUTH, lowerPanel);	//Add the panel to the SOUTH of the BorderLayout
		
		validate();
	}
	/**
	 * Method used to reset the window
	 */
	public void clear()
	{
		input.setText("");
		filterCombo.setSelectedItem("Name");
	}
	/**
	 * Method used to filter out items in the JList
	 */
	public void confirmFilter()
	{
		ToDoList.listenerOn = false;
		ToDoList.listModel.remove(filterLoop);
		ToDoList.listModel.add(filterLoop, "-");
		ToDoList.listenerOn = true;
	}
	/**
	 * Method used to act upon filter options chosen
	 * @param index index of filter option chosen
	 */
	public void myFilter(int index)
	{
		if(filterCombo.getSelectedIndex() == 0)
		{
			for(filterLoop = 0; filterLoop< ToDoList.myTasks.size(); filterLoop++)
			{
				if(ToDoList.myTasks.get(filterLoop).getName().contains(input.getText()))
				{
					//DO NOTHING
				}
				else
				{
					confirmFilter();
				}
			}
			this.setVisible(false);
		}
		if(filterCombo.getSelectedIndex() == 1)
		{
				for(filterLoop = 0; filterLoop < ToDoList.myTasks.size(); filterLoop++)
				{
					if(ToDoList.myTasks.get(filterLoop).getCategory().equals((String) categoryCombo.getSelectedItem()))
					{
						//DO NOTHING
					}
					else
					{
						confirmFilter();
					}
				}
			this.setVisible(false);
		}
		if(filterCombo.getSelectedIndex() == 2)
		{
			for(filterLoop = 0; filterLoop < ToDoList.myTasks.size(); filterLoop++)
			{
				if(ToDoList.myTasks.get(filterLoop).getPriority().equals((String) priorityCombo.getSelectedItem()))
				{
					//DO NOTHING
				}
				else
				{
					confirmFilter();
				}
			}
		this.setVisible(false);
		}
	}
	/**
	 * Method used to track action performed
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == filter)
		{
			myFilter(filterCombo.getSelectedIndex());
		}
		if(e.getSource() == cancel)
		{
			this.setVisible(false);
		}
		if(e.getSource() == filterCombo)
		{
			if(filterCombo.getSelectedIndex() == 0)
			{
				label4.setVisible(true); 	input.setVisible(true);
				label3.setVisible(false); 	categoryCombo.setVisible(false);
				label2.setVisible(false); 	priorityCombo.setVisible(false);
			}
			else if(filterCombo.getSelectedIndex() == 1)
			{
				label4.setVisible(false); 	input.setVisible(false);
				label3.setVisible(true); 	categoryCombo.setVisible(true);
				label2.setVisible(false); 	priorityCombo.setVisible(false);
			}
			else if(filterCombo.getSelectedIndex() == 2)
			{
				label4.setVisible(false); 	input.setVisible(false);
				label3.setVisible(false); 	categoryCombo.setVisible(false);
				label2.setVisible(true); 	priorityCombo.setVisible(true);
			}
		}
	}
	
	
	
	
}
