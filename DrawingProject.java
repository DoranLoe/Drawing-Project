/*
 * DrawingProject.java
 * 
 * 
 * 
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;

public class DrawingProject implements ActionListener, ChangeListener, MouseInputListener{
	
	private JFrame frame = new JFrame("DrawingProjStartUpWindow");
	private JFrame sizingFrame = new JFrame("Select Canvas Size");
	private JFrame loadingFrame = new JFrame("Load Pre-existing image");
	private JFrame drawingFrame = new JFrame("Drawing Project");
	// get screen dimensions, useful thanks frameDemo2
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//variables to decide the painting canvas size, default one quarter of total screen.
	private int canX = screenSize.width/2;
	private int canY = screenSize.height/2;
	private final int BORDERSIZE = 0;
	
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
		xIn.setLabelTable(xIn.createStandardLabels(screenSize.width/20));// with the ticks created we now label them
		xIn.setPaintLabels(true);
		xIn.addChangeListener(this);
		//now the same for the y slider
		JSlider yIn = new JSlider (JSlider.VERTICAL,0,screenSize.height-BORDERSIZE,canY);
		yIn.setName("yIn");
		yIn.setPaintTicks(true);
		yIn.setMinorTickSpacing(screenSize.height/20);
		yIn.setMajorTickSpacing(screenSize.height/10);
		yIn.setLabelTable(yIn.createStandardLabels(screenSize.height/20));// with the ticks created we now label them
		yIn.setPaintLabels(true);
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
		sizingFrame.setSize(new Dimension(screenSize.width/3, screenSize.height/3));
		sizingFrame.setLocation(frame.getLocation().x,frame.getLocation().y);
		sizingFrame.setVisible(true);
	}
	
	private Graphics2D graph;
	private BufferedImage img;
	private JLabel can; //my canvas becasue the canvas component sucks appearently
	private short drawMode;
	
	private void createNewDrawingWindow()
	{
		drawingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*Okay now that we are here, we need to find some sort of way to make a canvas or image or panel or something that we can draw upon.
		that thing will be the base center of this window.
		though stumbling about the java api and scouring StdDraw for clues I found BufferedImage, ImageIO
		Found this video: https://www.youtube.com/watch?v=gp9H0WLxKgU which might become usefull when I want to load an image, but I want to have a canvas first.
		Found Graphics2D which contains the word paint and thus might be vital
		Graphics2D canvas = new Graphics2D(); Ah. it's abstract. useful to know. what uses you then?
		Ah. there's a component called Canvas. that seems glaringly obvious lets make that*/
		
		//new day new me. STDraw uses Graphics2d and bufferedImage to get a Graphics2d it also uses two of them one onscreen and one offscreen. let me just mess around a bit.
		//Canvas can = new Canvas();
		initCanvas();
		//JLabel testL = new JLabel();
			//testL.setIcon(New ImageIcon(graph));
		//graph.fill(new Ellipse2D.Double(250, 250, 500, 500));//seemed to do nothing
		//graph.fillOval(0, 0, 500, 500); //nothing again
		//can.paint(graph);// im starting to think this doesn't do anything. like at all.
		//can.addMouseListener(this);
		can.addMouseListener(this);
		can.addMouseMotionListener(this);
		can.setIcon(new ImageIcon(img));
		//can.setSize(canX,canY);
		drawingFrame.getContentPane().add(can,BorderLayout.CENTER);
		drawingFrame.setJMenuBar(createDrawingMenu());
		drawingFrame.pack();
		drawingFrame.setSize(new Dimension(canX+BORDERSIZE,canY+BORDERSIZE));
		drawingFrame.setLocation(sizingFrame.getLocation().x,sizingFrame.getLocation().y);
		drawingFrame.setVisible(true);
	}
	
	//inspired by the method in STDraw, but I don't want to just copy their stuff. I want to know what it's doing
	private void initCanvas()
	{
		if (graph != null) graph.dispose();
        
        img  = new BufferedImage(canX, canY, BufferedImage.TYPE_INT_ARGB);
        graph = img.createGraphics();
        
        graph.setBackground(Color.WHITE);//might not be doing anything as it's in a panel and thus using it's background
        graph.clearRect(0, 0, canX, canY);
        graph.scale(1.0,1.0);//don't think I need this
        
        graph.setColor(Color.BLACK);
        drawMode =0;
        
        can = new JLabel();
	}
	
	private JMenuBar createDrawingMenu()
	{
		JMenuBar bar = new JMenuBar();
		
		JMenu fMenu = new JMenu("File"); 
		fMenu.setMnemonic(KeyEvent.VK_F);
		fMenu.getAccessibleContext().setAccessibleDescription("Nothing yet");
		bar.add(fMenu);
		
		JMenu dmMenu = new JMenu("Brushes"); 
		dmMenu.setMnemonic(KeyEvent.VK_B);
		dmMenu.getAccessibleContext().setAccessibleDescription("Select what type of drawing mode to use");
		bar.add(dmMenu);
		
		JMenuItem pen = new JMenuItem("Pen");
		pen.setMnemonic(KeyEvent.VK_P);
		pen.getAccessibleContext().setAccessibleDescription("Default Selection");
		pen.setActionCommand("setPen");
		pen.addActionListener(this);
		dmMenu.add(pen);
		
		return bar;
	}
	
	private void drawCircle(int x, int y)
	{//currently a test
		IO.println("entered DrawCircle with corrds: "+x+", "+y);
		//IO.println(graph);
		//graph.fill(new Ellipse2D.Double(x-100, y-100, 200, 200)); //these two methods need to do SOMETHING
		graph.fillOval(x-5, y+27, 10, 10);//figure out why those values keep breaking. is it the border?
		updateImg();
	}
	
	/*private boolean mD = false; //a test
	private void drawMoreCircles()
	{
		while(mD)
		{
			//this should cause an infinite loop if I understand correctly.
			//correct it did. this is because mouseReleased couldn't happen.
			//so how do I correct for that? can I run something to check if a mouse button is STILL being pressed?
		}
	}*/
	
	private void updateImg()
	{
		IO.print("Entered updateImg");
		//can.update(graph);
		
        drawingFrame.repaint();
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
			case "setPen":
				drawMode=1;
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
		//mD=true;
		//drawMoreCircles();
		IO.println("gothere2");
	}
	public void mouseReleased (MouseEvent e)
	{
		switch(drawMode)
		{
			case 1: break; // does nothing in this context, but isn't invalid
			case 0:
				drawCircle(e.getX(), e.getY());
				break;
			default: IO.println("Invalid drawMode state detected.");
		}
		IO.println("gothere3");
	}
	public void mouseClicked (MouseEvent e)
	{
		//drawCircle(e.getX(), e.getY());//eventually change this
		IO.println("gothere4");
	}
	public void mouseDragged(MouseEvent e)
	{
		switch(drawMode)
		{
			case 1: break; // does nothing in this context, but isn't invalid
			case 0:
				drawCircle(e.getX(), e.getY());
				break;
			default: IO.println("Invalid drawMode state detected.");
		}
		//drawCircle(e.getX(), e.getY());//OKAY! lets create a drawtype integer thing and a switch case so I can have more than a standard brush.
		IO.println("gothereMouseDrag");
	}
	public void mouseMoved (MouseEvent e)
	{}
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

/*
 *11/4/26
 Last week I tried to get anything to show up on the "drawign pad" I'll call it right now.
 Tried using the Canvas object only to find that it doesn't actually paint anything.
 Then I tried using JPanel since it had a paint method only to find that that method also does nothing.
 I think they're supposed to be overritten in subclasses. but that still raises the question of why make a method that does nothing?
 Especially something like that, since I don't know how to get a pixel to change colors on the screen.
 I don't have much time today to work on things, I jsut want to make sure that's the issue and try to maybe figure out something else. 
 Not seeign anythign on the graphics objects themselves. lets look at that video I found 2 weeks ago. https://www.youtube.com/watch?v=gp9H0WLxKgU
 They're using JLabels and creating imageIcons for it. lets try that.
 HOLY KARP It's working! it's doing a thing!
 **/
