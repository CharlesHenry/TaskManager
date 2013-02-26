import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @ToDoList.java - A Java based Task Manager used to manage simple tasks
 *
 *
 * @author Charles Henry
 * @version 1.00 2011/04/02
 */


public class ToDoList extends JFrame implements ActionListener {
	
	protected static JList list;
	protected static DefaultListModel listModel;
	private static JScrollPane scrollPane;

	private static JPanel rightPanel;
	private static JPanel lowerPanel;
	private static JPanel middlePanel;
	
	//Used for task info display
	private static JLabel taskName;
	private static JLabel startDate;
	private static JLabel endDate;
	private static JLabel priority;
	private static JLabel percComp;
	private static JLabel category;
	private static JLabel note;
	private static JTextField taskNameDisplay;
	private static JTextField startDateDisplay;
	private static JTextField endDateDisplay;
	private static JTextField priorityDisplay;
	private static JTextField percCompDisplay;
	private static JTextField categoryDisplay;
	private static JTextField noteDisplay;
	
	//Buttons
	private static JButton add;
	private static JButton edit;
	private static JButton filterBtn;
	private static JButton clearFilterBtn;
	
	//Menu bar
	private static JMenuBar menubar1;
	private static JMenu fileMenu;
	private static JMenuItem importItem;
	private static JMenuItem exportItem;
	private static JMenu editMenu;
	private static JMenuItem addItem;
	private static JMenuItem delItem;
	private static JMenuItem editSelItem;
	
	//used for the management of exporting tasks
	private static String exportText;
	protected static boolean exported = true;				//true = data has been saved (tasks do not to be saved on program close)
	protected static int currentEditIndex = 0;
	protected static boolean listenerOn = true;				//used to disable the JListListener when modifying it to prevent errors
	
	protected static AddWindow add1 = new AddWindow();
	protected static EditWindow edit1 = new EditWindow();
	protected static FilterWindow filter1 = new FilterWindow();
	
	protected static ArrayList<Task> myTasks = new ArrayList<Task>();	//array used to store task Objects from the Task class
	
	public ToDoList()
	{
		this.setLayout(new BorderLayout());
		this.setTitle("ToDoList Manager");
		this.setSize(640,260);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	//Makes the program terminate properly when the close button is pressed
	
		//adding the Menu bar to the window and to the action listener
		menubar1 = new JMenuBar();						// Makes a new menu bar (now you can add items to this menu...)
		this.setJMenuBar(menubar1); 					// Attaches the named menubar (menubar1) to the window

		fileMenu = new JMenu("File"); 					//Makes a menubar button
		menubar1.add(fileMenu); 						//add the menubar button to the menu bar
			importItem = new JMenuItem("Import Tasks");			//creates drop down buttons for the menubar button
			exportItem = new JMenuItem("Export Tasks");
			fileMenu.add(importItem);						
			fileMenu.add(exportItem);
			importItem.addActionListener(this);
			exportItem.addActionListener(this);

		editMenu = new JMenu("Edit");
		menubar1.add(editMenu);
			addItem = new JMenuItem("Add");
			delItem = new JMenuItem("Delete");
			editSelItem = new JMenuItem("Edit Selected");
			editMenu.add(addItem);
			editMenu.add(delItem);
			editMenu.add(editSelItem);
			addItem.addActionListener(this);
			delItem.addActionListener(this);
			editSelItem.addActionListener(this);

		//adding the buttons and labels to the window and the ActionListener
		taskName = new JLabel("Task Name: ");
		startDate = new JLabel("Start Date: ");
		endDate = new JLabel("End Date: ");
		priority = new JLabel("Priority: ");
		percComp = new JLabel("Percent Complete: ");
		category = new JLabel("Category: ");
		note = new JLabel("Note: ");
		taskName.setHorizontalAlignment(JTextField.CENTER);
		startDate.setHorizontalAlignment(JTextField.CENTER);
		endDate.setHorizontalAlignment(JTextField.CENTER);
		priority.setHorizontalAlignment(JTextField.CENTER);
		percComp.setHorizontalAlignment(JTextField.CENTER);
		category.setHorizontalAlignment(JTextField.CENTER);
		note.setHorizontalAlignment(JTextField.CENTER);
		
			//make display fields noneditable and white in colour
		taskNameDisplay  = new JTextField();	taskNameDisplay.setEditable(false);		taskNameDisplay.setBackground(Color.white);
		startDateDisplay = new JTextField();	startDateDisplay.setEditable(false);	startDateDisplay.setBackground(Color.white);
		endDateDisplay   = new JTextField();	endDateDisplay.setEditable(false);		endDateDisplay.setBackground(Color.white);
		priorityDisplay  = new JTextField();	priorityDisplay.setEditable(false);		priorityDisplay.setBackground(Color.white);
		percCompDisplay  = new JTextField();	percCompDisplay.setEditable(false);		percCompDisplay.setBackground(Color.white);
		categoryDisplay  = new JTextField();	categoryDisplay.setEditable(false);		categoryDisplay.setBackground(Color.white);
		noteDisplay  	 = new JTextField();	noteDisplay.setEditable(false);			noteDisplay.setBackground(Color.white);	
		
		//Create a panel with a grid layout and add Labels etc
		rightPanel = new JPanel(new GridLayout(7, 2));				
		rightPanel.add(taskName);	rightPanel.add(taskNameDisplay);
		rightPanel.add(startDate);	rightPanel.add(startDateDisplay);
		rightPanel.add(endDate);	rightPanel.add(endDateDisplay);
		rightPanel.add(priority);	rightPanel.add(priorityDisplay);
		rightPanel.add(percComp);	rightPanel.add(percCompDisplay);
		rightPanel.add(category);	rightPanel.add(categoryDisplay);
		rightPanel.add(note);		rightPanel.add(noteDisplay);
		
		//set up JList and add it to a scrollPane
		list = new JList();
		listModel = new DefaultListModel();
		list.setModel(listModel);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 			//Make sure only one item can be selected on the tree
		scrollPane = new JScrollPane(list);
		middlePanel = new JPanel(new GridLayout(1, 2));				//Create a panel with a grid layout
		middlePanel.add(scrollPane);				
		middlePanel.add(rightPanel);
		this.getContentPane().add(BorderLayout.CENTER, middlePanel);	//Add the panel to the EAST of the BorderLayout
		
		filterBtn = new JButton("Filter");
		clearFilterBtn = new JButton("Clear filter");
		add = new JButton("Add Task");
		edit = new JButton("Edit Selected");
		filterBtn.addActionListener(this);
		clearFilterBtn.addActionListener(this);
		add.addActionListener(this);
		edit.addActionListener(this);
		lowerPanel = new JPanel(new FlowLayout());
		lowerPanel.add(add);
		lowerPanel.add(edit);
		lowerPanel.add(filterBtn);
		lowerPanel.add(clearFilterBtn);
		this.getContentPane().add(BorderLayout.SOUTH, lowerPanel);	//Add the panel to the SOUTH of the BorderLayout

		//import Tasks on startup
		try
		{
			importTasks();
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "Error, task file could not be exported.");
		}
		
		listListener myListListener = new listListener();
		list.addListSelectionListener(myListListener);
		list.setSelectedIndex(0);		
		
		//export tasks on close
		this.addWindowListener(new WindowAdapter()
		{
			/**
			 * Method used to export tasks when the program is closed
			 * 
			 * @ param exported boolean used to show whether the tasks have been saved of not
			 */
			public void windowClosing(WindowEvent we)
		    {
				if(!exported)	//if tasks are not saved already
				{
					exportTasks();
				}
		    }
		});
		
		validate();

	}
	
	/**
	 * ListListener used monitor JList list selection changes
	 * @author Charles
	 *
	 */
	private class listListener implements ListSelectionListener{
		/**
		 * Changes the task details display to display the details of the current selected task
		 * @param listenrOn used to turn on and off the JList listener when modifiying the list
		 */
		public void valueChanged(ListSelectionEvent e) {
			if(listenerOn)
			{
				changeOutputDetails(list.getSelectedIndex());
			}
			
		}
	}
	/**
	 * Method used to update the display to show the task details of the
	 * selected task in the JList display
	 * @param index the index of the selected item on the JList (correlates with the same task index in the array)
	 */
	public static void changeOutputDetails(int index)
	{
		//update task output details from array data
		taskNameDisplay.setText(myTasks.get(index).getName());
		startDateDisplay.setText(""+myTasks.get(index).getStartDate());
		endDateDisplay.setText(""+myTasks.get(index).getEndDate());
		priorityDisplay.setText(""+myTasks.get(index).getPriority());
		percCompDisplay.setText(""+myTasks.get(index).getPercentComplete()+"%");
		categoryDisplay.setText(myTasks.get(index).getCategory());
		noteDisplay.setText(myTasks.get(index).getNote());
	}
	/**
	 * Method used to open the edit window
	 */
	public void edit()
	{
		if(list.getSelectedIndex() < 0)	//if there is no task selected, notify the user
		{
			JOptionPane.showMessageDialog(null, "Error, no task selected.");
		}
		else
		{
			currentEditIndex = list.getSelectedIndex();	//store the current selected task index
			EditWindow.populate();						//populate EditWindow with task data
			listenerOn = false;							//Disable the listListener while editing the list
			edit1.setVisible(true);	
		}
	}
	/**
	 * method used to import tasks from a text file
	 */
	public void importTasks()
	{
		try
		{
			File f = new File("C:\\ToDoList\\ToDoListTasks.txt");
			Scanner textFile;
			try {
				textFile = new Scanner(f);
				while(textFile.hasNextLine())
				{
					String tString = textFile.nextLine();
					String [] str = tString.split("¬");		//use "¬" to differentiate between each block of information and store it in a new array
					
					Task t = new Task();					//create a new task
					//Add task details from string
					t.setName(str[0]);
					t.setStartDate(new myDate(Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3])));
					t.setEndDate(new myDate(Integer.parseInt(str[4]), Integer.parseInt(str[5]), Integer.parseInt(str[6])));
					t.setPriority(str[7]);
					t.setPercentComplete(Integer.parseInt(str[8]));
					t.setCategory(str[9]);
					t.setNote(str[10]);
					
					myTasks.add(t);			//add the task to the array list
					listModel.addElement(myTasks.get(myTasks.size()-1).getName());	//add the task to the JList display
				}
			}
			catch (FileNotFoundException e)//if the tasks text file cannot be found, notify the user
			{
				JOptionPane.showMessageDialog(null, "Error, the following file could not be found or is corrupt:" + "\n" + "'C:\\ToDoList\\ToDoListTasks.txt'" + "\n" + "Please create this file to export task list data.");
			}
		}
		catch(NumberFormatException z)//if the task file is not formatted properly notify the user
		{
			JOptionPane.showMessageDialog(null, "Error, Tasks may not have been imported successfully due to file corruption");
		}
	}
	/**
	 * Method used to export the tasks into a text file
	 */
	public void exportTasks()
	{
		try 
		{
			PrintWriter p = new PrintWriter("C:\\ToDoList\\ToDoListTasks.txt");		//create text file
			
			exportText = "";										//empty the string for new input
			int counter = 0;										//counter used to count how many tasks have been exported
			for(int i = 0; i < myTasks.size(); i++)					//loop through the array
			{
				Task t = new Task();								//create a new task
				t = myTasks.get(i);									//copy the task details from the array at index 'i' into the new array 't'
				exportText = exportText + t.toStringExport();		//extract the task data and store it in exportText
				p.println(exportText);								//add task data to the text file
				exportText = "";									//empty the string for new input
				counter++;											//increment count to show a task has been exported
			}
			
			p.close();												//close the printWriter
			exported = true;										//set exported to true to show tasks have been saved
			JOptionPane.showMessageDialog(null, counter + " tasks exported.");
			
		} 
		catch (FileNotFoundException e)//if the file cannot be found or created, notify the user
		{
			JOptionPane.showMessageDialog(null, "Error, the following file could not be created:" + "\n" + "'C:\\ToDoList\\ToDoListTasks.txt'" + "\n" + "Please create this file to export task list data.");
		}
	}
	/**
	 * Method used to track actions performed
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == addItem)
		{
			add1.setVisible(true);	//open the add window
		}
		if(e.getSource() == add)
		{
			add1.setVisible(true);	//open the add window
		}
		if(e.getSource() == delItem)
		{
			if(list.getSelectedIndex() < 0)	//if not item has been selected on the JList, notify the user
			{
				JOptionPane.showMessageDialog(null, "Error, no task selected.");
			}
			else
			{
				listenerOn = false;										//disable the JList Listener while editing
				myTasks.remove(list.getSelectedIndex());				//remove the task from the array
				listModel.remove(list.getSelectedIndex());				//remove the task from the JList
				listenerOn = true;										//enable the JList listener
				exported = false;										//set exported to false as data has been changed
				JOptionPane.showMessageDialog(null, "Task deleted.");
				//clear Task display text
				taskNameDisplay.setText("");
				startDateDisplay.setText("");
				endDateDisplay.setText("");
				priorityDisplay.setText("");
				percCompDisplay.setText("");
				categoryDisplay.setText("");
				noteDisplay.setText("");
			}
		}
		if(e.getSource() == edit)
		{
			edit();
		}
		if(e.getSource() == editSelItem)
		{
			edit();
		}
		if(e.getSource() == importItem)
		{
			importTasks();
			exported = false; 	//set exported to false as data has been changed
			
		}
		if(e.getSource() == exportItem)
		{
			exportTasks();
			
		}
		if(e.getSource() == filterBtn)
		{
			//reset the filter window and make it visible
			filter1.clear();
			filter1.setVisible(true);
		}
		if(e.getSource() == clearFilterBtn)
		{
			//loop through the array and set JList titles back to normal
			for(int i = 0; i < ToDoList.myTasks.size(); i++)
			{
				ToDoList.listenerOn = false;
				ToDoList.listModel.remove(i);
				ToDoList.listModel.add(i, myTasks.get(i).getName());
				ToDoList.listenerOn = true;
			}
			
		}
	}
	
}