/*
 * DrawingProject.java
 * 
 * 
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class DrawingProject implements ActionListener, ChangeListener{
	
	JFrame frame = new JFrame("DrawingProjStartUpWindow");
	JFrame sizingFrame = new JFrame("Select Canvas Size");
	JFrame loadingFrame = new JFrame("Load Pre-existing image");
	JFrame drawingFrame = new JFrame("Drawing Project");
	// get screen dimensions, useful thanks frameDemo2
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	void main () 
	{
		// step one. figure out how to open a window that can have buttons.
		//how am i already panicing?
		// Looking at StdDraw to see how they did things. which means I need to credit them immediately, yay
		//they're using Jframe lets look that up and go from there.
		// found this tutorial on frames by oracle. https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
		
		createAndOpenStartUpWindow();
		
	}
	
	//Opens the start up window which will give the user the choice to start new or load image.
	//for now i'm only interested in getting that first button in there and maybe some text.
	private void createAndOpenStartUpWindow()
	{
		//create frame
		//JFrame frame = new JFrame("DrawingProjStartUpWindow"); moved so I can close it when clicking a button.
		//make frame close when closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//create label
		JLabel emptyLabel = new JLabel("");//only doing this because the tutorial did. not sure I need to, consider removing
		//add label to frame
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		//create buttons
		JButton testButton = new JButton("New Drawing");
		JButton loadButton = new JButton("Load Drawing");
		//make button a reasonable size... now how do I do that? back to the java tutorials!
		//Okay, so the button needs to be in a panel
		JPanel buttonPanel = new JPanel();
		//buttonPanel.setSize(new Dimension(screenSize.width/2, screenSize.height/2)); //seemed to do nothing put it in multiple spots
		//testButton.setSize(new Dimension(screenSize.width/2, screenSize.height/2)); // also nothing
		//before addign the button to the panel, lets try making it do something.
		testButton.setActionCommand("enable");
		testButton.addActionListener(this);
		//now add buttons to panel
		buttonPanel.add(testButton);
		buttonPanel.add(loadButton);
		//add button to frame
		frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
		//Lets get some text in here
		JLabel testLabel = new JLabel("Drawing Project");
		testLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.getContentPane().add(testLabel, BorderLayout.PAGE_START);
		//pack the frame into one window
		frame.pack();
		//set window size
		frame.setSize(new Dimension(screenSize.width/4, screenSize.height/4));
		//show window
		frame.setVisible(true);
	}
	
	private void createAndOpenSizingWindow()
	{
		sizingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel emptyLabel = new JLabel("");
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		//Okay I want 2 fields that will contain numbers in pixels and a create button.
		//I don't know if text input fields are a thing that is so easily do-able but I'll find out!
		//Just looked up how / if text inputfields can be and I found https://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html
		//which has JSlider and I'm evil enough to put a slider into this.
		//Also JInternalFrame??? I laughed outloud I think that's a delightfully silly idea.
		//Sliders!
		JSlider xIn = new JSlider (JSlider.HORIZONTAL,0,screenSize.width,screenSize.width/2);
		xIn.addChangeListener(this);
		JSlider yIn = new JSlider (JSlider.HORIZONTAL,0,screenSize.height,screenSize.height/2);
		JPanel slides = new JPanel();
		slides.add(xIn);
		slides.add(yIn);
		sizingFrame.getContentPane().add(slides, BorderLayout.CENTER);
		//Button
		JButton start = new JButton("Create");
		start.setActionCommand("create");
		start.addActionListener(this);
		JPanel btnPanel = new JPanel ();
		btnPanel.add(start);
		sizingFrame.getContentPane().add(btnPanel, BorderLayout.PAGE_END);
		sizingFrame.pack();
		sizingFrame.setSize(new Dimension(screenSize.width/4, screenSize.height/4));
		sizingFrame.setVisible(true);
	}
	
	
	//Listener handling methods
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case "enable":
				createAndOpenSizingWindow();
				frame.dispose();
				break;
		}
		/*
		if ("enable".equals(e.getActionCommand()))
		{
			createAndOpenSizingWindow();
			frame.dispose();
		}*/
	}
	
	public void stateChanged(ChangeEvent e) 
	{// after ages of dealing with it not recognizing ChangeListener, it's because I needed another import
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
            IO.println("value changed to: "+fps);
        } 
	}
}

/*So day one of working on this project, what have I learned.
 I learned that the default Java window system, called JFrame which I last used almost 10 years ago, is a little complicated
 A Window, or "Frame" consists of several "Panes". A Root Pane, Menu Pane, Content Pane, and a Glass Pane.
 I don't know what the root pane or glass pane do. Menu Pane is currious look into later.
 The Content Pane is our main concern.
 The Content Pane can have a lot of things added to it. there's a whole big list of things and they all mostly work the same I hope.
 Some of those things expect to be put into Panels which then go into the Pane.
 I do not know how to change a panel's size or shape.
 The Content Pane and Panels seem to use Layouts. The content pane seems to use the Border Layout by default and Panels use FlowLayout. you can change this.
 Also I learned how to obtain the screen size. that's wild.*/

