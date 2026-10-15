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

public class DrawingProject implements ActionListener, ChangeListener, MouseListener{
	
	private JFrame frame = new JFrame("DrawingProjStartUpWindow");
	private JFrame sizingFrame = new JFrame("Select Canvas Size");
	private JFrame loadingFrame = new JFrame("Load Pre-existing image");
	private JFrame drawingFrame = new JFrame("Drawing Project");
	// get screen dimensions, useful thanks frameDemo2
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//variables to decide the painting canvas size, default one quarter of total screen.
	private int canX = screenSize.width/2;
	private int canY = screenSize.height/2;
	private final int BORDERSIZE = 75;
	
	private final boolean ISTESTING = true;
	
	
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
		testButton.setActionCommand("NewDrawing");
		testButton.addActionListener(this);
		//now the same for the load button. I'll figureout functionality later.
		loadButton.setActionCommand("LoadDrawing");
		loadButton.addActionListener(this);
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
		JLabel emptyLabel = new JLabel("Choose Size");
		emptyLabel.setHorizontalAlignment(JLabel.CENTER);
		sizingFrame.getContentPane().add(emptyLabel, BorderLayout.PAGE_START);
		//Okay I want 2 fields that will contain numbers in pixels and a create button.
		//I don't know if text input fields are a thing that is so easily do-able but I'll find out!
		//Just looked up how / if text inputfields can be and I found https://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html
		//which has JSlider and I'm evil enough to put a slider into this.
		//Also JInternalFrame??? I laughed outloud I think that's a delightfully silly idea.
		//Sliders!
		JSlider xIn = new JSlider (JSlider.HORIZONTAL,0,screenSize.width-BORDERSIZE,canX); //- on the max since I want a border on the frame
		xIn.setName("xIn");
		xIn.setPaintTicks(true);
		xIn.setMinorTickSpacing(screenSize.width/20);
		xIn.setMajorTickSpacing(screenSize.width/10);
		xIn.addChangeListener(this);
		//now the same for the y slider
		JSlider yIn = new JSlider (JSlider.VERTICAL,0,screenSize.height-BORDERSIZE,canY);
		yIn.setName("yIn");
		yIn.setPaintTicks(true);
		yIn.setMinorTickSpacing(screenSize.height/20);
		yIn.setMajorTickSpacing(screenSize.height/10);
		yIn.addChangeListener(this);
		//add both to a panel and add panel.
		JPanel slides = new JPanel(new BorderLayout());
		slides.add(yIn, BorderLayout.LINE_START);
		slides.add(xIn, BorderLayout.PAGE_END);
		sizingFrame.getContentPane().add(slides, BorderLayout.CENTER);
		//Button
		JButton start = new JButton("Create");
		start.setActionCommand("CreateCanvas");
		start.addActionListener(this);
		JPanel btnPanel = new JPanel ();
		btnPanel.add(start);
		sizingFrame.getContentPane().add(btnPanel, BorderLayout.PAGE_END);
		sizingFrame.pack();
		sizingFrame.setSize(new Dimension(screenSize.width/4, screenSize.height/4));
		sizingFrame.setVisible(true);
	}
	
	private void createNewDrawingWindow()
	{
		drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Okay now that we are here, we need to find some sort of way to make a canvas or image or panel or something that we can draw upon.
		//that thing will be the base center of this window.
		//though stumbling about the java api and scouring StdDraw for clues I found BufferedImage, ImageIO
		//Found this video: https://www.youtube.com/watch?v=gp9H0WLxKgU which might become usefull when I want to load an image, but I want to have a canvas first.
		//Found Graphics2D which contains the word paint and thus might be vital
		//Graphics2D canvas = new Graphics2D(); Ah. it's abstract. useful to know. what uses you then?
		//Ah. there's a component called Canvas. that seems glaringly obvious lets make that
		Canvas can = new Canvas();
		can.addMouseListener(this);
		can.setSize(canX,canY);
		drawingFrame.getContentPane().add(can,BorderLayout.CENTER);
		drawingFrame.pack();
		drawingFrame.setSize(new Dimension(canX+BORDERSIZE,canY+BORDERSIZE));
		drawingFrame.setVisible(true);
	}
	
	//Listener handling methods
	//button handling
	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case "NewDrawing":
				createAndOpenSizingWindow();
				frame.dispose();
				break;
			case "CreateCanvas":
				createNewDrawingWindow();
				sizingFrame.dispose();
				break;
			default:
				IO.println("WARNING: actionHandler default case reached.\nCheck Action Commands for "+e.getActionCommand());
				break;
		}
		/*
		if ("enable".equals(e.getActionCommand()))
		{
			createAndOpenSizingWindow();
			frame.dispose();
		}*/
	}
	
	//slider handling
	public void stateChanged(ChangeEvent e) 
	{// after ages of dealing with it not recognizing ChangeListener, it's because I needed another import
		JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
			if (ISTESTING)
			{
				int fps = (int)source.getValue();
				IO.println("value "+source.getName()+" changed to: "+fps);
				IO.println("Previous Values: canX ="+canX+", canY ="+canY);
			}
            switch(source.getName())
            {
				case "xIn":
					canX= (int)source.getValue();
					break;
				case "yIn":
					canY= (int)source.getValue();
					break;
				default:
					IO.println("WARNING: changeHandler default case reached.\nCheck name for "+source.getName());
					break;
			}
        } 
	}
	//Mouse handling for canvas
	public void mouseEntered (MouseEvent e)
	{
		IO.println("gothere");
	}
	public void mouseExited (MouseEvent e)
	{
		IO.println("gothere1");
	}
	public void mousePressed (MouseEvent e)
	{
		IO.println("gothere2");
	}
	public void mouseReleased (MouseEvent e)
	{
		IO.println("gothere3");
	}
	public void mouseClicked (MouseEvent e)
	{
		IO.println("gothere4");
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

